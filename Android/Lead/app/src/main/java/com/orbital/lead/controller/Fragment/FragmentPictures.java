package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.PictureActivity;
import com.orbital.lead.controller.Activity.ViewPagerAdapter.PagerImageAdapter;
import com.orbital.lead.controller.GridAdapter.GridAlbumsAdapter;
import com.orbital.lead.controller.GridAdapter.GridPicturesAdapter;
import com.orbital.lead.controller.GridAdapter.MultiChoiceModeListener;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;
import com.orbital.lead.widget.WrapContentHeightViewPager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPictures.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPictures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPictures extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ALBUM = "album";
   // private static final String ARG_PICTURE_LIST = "picturelist";
    //private static Context mContext;

    // TODO: Rename and change types of parameters
    private Album mParamAlbum;
   // private ArrayList<Picture> mParamPictureList;

    private OnFragmentInteractionListener mListener;
    private CustomLogging mLogging;
    private Logic mLogic;

    //private PictureActivity mPictureActivity;
    private GridView mGridView;
    private GridPicturesAdapter mGridPicturesAdapter;
    private PictureList mPictureList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album Album object.
     * //@param picList Picture ArrayList.
     * @return A new instance of fragment FragmentAlbum.
     */
    // TODO: Rename and change types and number of parameters
    //Context context,
    public static FragmentPictures newInstance(Album album) {
        FragmentPictures fragment = new FragmentPictures();

        //mContext = context;
        //ArrayList<Picture> list = new ArrayList<Picture>(picList);

        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        //args.putParcelableArrayList(ARG_PICTURE_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPictures() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initLogging();
        if (getArguments() != null) {
            mParamAlbum = getArguments().getParcelable(ARG_ALBUM);
            //mParamPictureList = getArguments().getParcelableArrayList(ARG_PICTURE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mLogging.debug(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pictures, container, false);

        this.setPictureList(this.mParamAlbum.getPictureList());

        //if(!mParamAlbum.getIsFromFacebook()){
            this.initLogic();
            this.initGridView(rootView);

        //}else{

        //}




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

    /*
    public PictureActivity getPictureActivity(){
        return this.mPictureActivity;
    }
    */
    public void updateGridPicturesAdapter(PictureList list) {
        this.getPictureList().getList().addAll(list.getList());
        this.mGridPicturesAdapter.notifyDataSetChanged();
    }


    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }


    private void initGridView(View v){
        this.mLogging.debug(TAG, "initGridView");
        this.mGridView = (GridView) v.findViewById(R.id.grid_album_picture);
        //getActivity(),
        if(this.mGridPicturesAdapter == null){
            this.initGridPicturesAdapter();
        }
        this.mGridView.setAdapter(this.getGridPicturesAdapter());
        this.mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        this.mGridView.setMultiChoiceModeListener(new MultiChoiceModeListener(this.getGridView()));
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLogging.debug(TAG, "onItemSelected -> " + position);
                String url = getPictureList().getList().get(position).getThumbnailUrl();
                mLogging.debug(TAG, "getThumbnailUrl -> " + url);
                showDialogPicture(getActivity(), getPictureList().getList(), position);
            }
        });

    }

    private void initGridPicturesAdapter(){
        this.mGridPicturesAdapter = new GridPicturesAdapter(this.getPictureList());
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
    public void showDialogPicture(Context mContext, ArrayList<Picture> list, int currentPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(mContext instanceof PictureActivity){ //may come from FragmentAlbum
            LayoutInflater inflater = ((PictureActivity) mContext).getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.dialog_viewpager_picture, null);

            WrapContentHeightViewPager pager = (WrapContentHeightViewPager) dialogView.findViewById(R.id.pager_picture);
            pager.setAdapter(new PagerImageAdapter(mContext, list));
            pager.setCurrentItem(currentPosition);

            builder.setView(dialogView);
            builder.create().setCanceledOnTouchOutside(true);
            builder.create().show();
        }

    }

    private Logic getLogic() {
        return this.mLogic;
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
        public void onFragmentPicturesInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentPicturesInteraction(uri);
        }
    }




}
