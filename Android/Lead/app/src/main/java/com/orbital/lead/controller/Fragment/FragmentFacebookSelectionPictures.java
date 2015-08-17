package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.PictureActivity;
import com.orbital.lead.controller.GridAdapter.GridPicturesAdapter;
import com.orbital.lead.controller.GridAdapter.MultiChoiceModeListener;
import com.orbital.lead.controller.ViewPagerAdapter.PagerImageAdapter;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.FacebookLogic;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;
import com.orbital.lead.widget.WrapContentHeightViewPager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFacebookSelectionPictures.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFacebookSelectionPictures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFacebookSelectionPictures extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    public static final int REQUEST_UPLOAD_FACEBOOK_IMAGE_FILE = 222;

    private static final String ARG_ALBUM = "album";

    private Album mParamAlbum;

    private OnFragmentInteractionListener mListener;
    private CustomLogging mLogging;
    private Logic mLogic;
    private Parser mParser;
    private FacebookLogic mFacebookLogic;

    private GridView mGridView;
    private FloatingActionButton mFabAddPicture;
    private AlertDialog mDialogPicturePreview;
    private AlertDialog mDialogUploadImageProgress;
    private GridPicturesAdapter mGridPicturesAdapter;
    private PictureList mPictureList;


    // Views in uploadImageProgress Dialog
    private TextView textDialogUpdateProgress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album Album object.
     * @return A new instance of fragment FragmentAlbum.
     */
    //Context context,
    public static FragmentFacebookSelectionPictures newInstance(Album album) {
        FragmentFacebookSelectionPictures fragment = new FragmentFacebookSelectionPictures();

        //mContext = context;
        //ArrayList<Picture> list = new ArrayList<Picture>(picList);

        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        //args.putParcelableArrayList(ARG_PICTURE_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFacebookSelectionPictures() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initLogging();
        if (getArguments() != null) {
            mParamAlbum = getArguments().getParcelable(ARG_ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mLogging.debug(TAG, "onCreateView");
        // Inflate the layout for this fragmentmm
        View rootView = inflater.inflate(R.layout.fragment_pictures, container, false);

        if(this.mParamAlbum != null){
            this.setPictureList(this.mParamAlbum.getPictureList());
        }

        this.initLogic();
        this.initParser();
        this.initFacebookLogic();
        this.initGridView(rootView, this.getPictureList());
        this.initFabAddPicture(rootView);
        this.hideFabAddPicture();

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //this.initActivity(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateGridPicturesAdapter(PictureList list) {
        mLogging.debug(TAG, "updateGridPicturesAdapter list.size() => " + list.size());
        this.getPictureList().overrideList(list);
        this.mGridPicturesAdapter.notifyDataSetChanged();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    public void updateUploadImageProgressValue(int progressValue) {
        if(this.mDialogUploadImageProgress == null) {
            this.showUploadImageProgressDialog(getActivity());
        }

        if(this.textDialogUpdateProgress != null) {
            String text = Constant.STRING_UPDATE_PROGRESS_FORMAT.replace(Constant.DUMMY_NUMBER,
                    getParser().convertIntegerToString(progressValue));
            this.textDialogUpdateProgress.setText(text);
        }
    }

    public void closeUploadImageProgressDialog() {
        if(this.mDialogUploadImageProgress != null && this.mDialogUploadImageProgress.isShowing()) {
            this.mLogging.debug(TAG, "Closing upload image progress dialog");
            this.mDialogUploadImageProgress.dismiss();
        }
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }

    private void initParser() {
        this.mParser = Parser.getInstance();
    }

    private void initFacebookLogic() {
        this.mFacebookLogic = FacebookLogic.getInstance();
    }


    private void initGridView(View v, PictureList list){
        this.mLogging.debug(TAG, "initGridView");
        this.mGridView = (GridView) v.findViewById(R.id.grid_album_picture);

        if(this.mGridPicturesAdapter == null){
            this.initGridPicturesAdapter(list);
        }
        this.mGridView.setAdapter(this.getGridPicturesAdapter());
        this.mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        this.mGridView.setMultiChoiceModeListener(new MultiChoiceModeListener(this.getGridView()));
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = getPictureList().getList().get(position).getAcutalUrl();

                mLogging.debug(TAG, "getAcutalUrl -> " + url);
                mLogging.debug(TAG, "mParamAlbum.getAlbumID() -> " + mParamAlbum.getAlbumID());

                if(mListener != null) {
                    mListener.onFragmenFacebookSelectiontPicturesInteraction(REQUEST_UPLOAD_FACEBOOK_IMAGE_FILE, url);
                }


                //showPicturePreviewDialog(getActivity(), getPictureList().getList(), position);
            }
        });

    }

    private void initGridPicturesAdapter(PictureList list){
        this.mGridPicturesAdapter = new GridPicturesAdapter(list);
    }

    private void initFabAddPicture(View v) {
        this.mFabAddPicture = (FloatingActionButton) v.findViewById(R.id.fab_add_picture);
    }

    private void hideFabAddPicture() {
        if(this.mFabAddPicture != null) {
            this.mFabAddPicture.setVisibility(View.GONE);
        }
    }

    private Parser getParser() {
        if(this.mParser == null) {
            this.initParser();
        }
        return this.mParser;
    }

    private Album getParamAlbum(){
        return this.mParamAlbum;
    }

    private PictureList getPictureList() {
        if(this.mPictureList == null) {
            this.mPictureList = new PictureList();
        }
        return this.mPictureList;
    }

    private GridView getGridView() {
        return this.mGridView;
    }

    private GridPicturesAdapter getGridPicturesAdapter() {
        return this.mGridPicturesAdapter;
    }

    /*============= Display Dialogs =================*/
    private void showPicturePreviewDialog(Context mContext, ArrayList<Picture> list, int currentPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(mContext instanceof PictureActivity){ //may come from FragmentAlbum
            LayoutInflater inflater = ((PictureActivity) mContext).getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.dialog_viewpager_picture, null);

            Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
            Button btnSetCover = (Button) dialogView.findViewById(R.id.btnSetCover);

            WrapContentHeightViewPager pager = (WrapContentHeightViewPager) dialogView.findViewById(R.id.pager_picture);
            pager.setAdapter(new PagerImageAdapter(mContext, list));
            pager.setCurrentItem(currentPosition);

            builder.setView(dialogView);

            this.mDialogPicturePreview = builder.create();

            this.mDialogPicturePreview.setCanceledOnTouchOutside(true);
            this.mDialogPicturePreview.setCancelable(true);
            this.mDialogPicturePreview.show();


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogPicturePreview.dismiss();
                }
            });

        }

    }

    private void showUploadImageProgressDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(mContext instanceof PictureActivity) {

            LayoutInflater inflater = ((PictureActivity) mContext).getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.dialog_upload_file_progress_layout, null);
            this.textDialogUpdateProgress = (TextView) dialogView.findViewById(R.id.text_update_progress);

            builder.setView(dialogView);

            this.mDialogUploadImageProgress = builder.create();
            this.mDialogUploadImageProgress.setCanceledOnTouchOutside(true);
            this.mDialogUploadImageProgress.setCancelable(true);
            this.mDialogUploadImageProgress.show();

        }
    }



    private Logic getLogic() {
        return this.mLogic;
    }

    private FacebookLogic getFacebookLogic() {
        return this.mFacebookLogic;
    }

    private void setPictureList(PictureList list){
        this.mPictureList = list;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmenFacebookSelectiontPicturesInteraction(int requestType, String url);
    }

    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmenFacebookSelectiontPicturesInteraction(0);
        }
    }
    */



}
