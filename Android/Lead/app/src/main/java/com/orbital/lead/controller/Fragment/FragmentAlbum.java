package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orbital.lead.R;
import com.orbital.lead.controller.GridAdapter.GridAlbumsAdapter;
import com.orbital.lead.controller.GridAdapter.GridPicturesAdapter;
import com.orbital.lead.controller.GridAdapter.MultiChoiceModeListener;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAlbum.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAlbum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbum extends Fragment {
    private final String TAG = this.getClass().getSimpleName();


    public static final int REQUEST_OPEN_FRAGMENT_PICTURES = 1;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CustomLogging mLogging;
    private Logic mLogic;

    private AlbumList mAlbumList;
    private GridAlbumsAdapter mGridAlbumsAdapter;
    private GridView mGridView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAlbum.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAlbum newInstance(String param1, String param2) {
        FragmentAlbum fragment = new FragmentAlbum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAlbum() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initLogging();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.mLogging.debug(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        this.initLogic();
        this.initGridView(rootView);

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public void updateGridAlbum(AlbumList list){
        this.setAlbumList(list);
        this.initGridAlbumAdapter();
        this.getGridView().setAdapter(this.mGridAlbumsAdapter);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }

    private void initGridAlbumAdapter(){
        this.mGridAlbumsAdapter = new GridAlbumsAdapter(this.getAlbumList());
    }

    private void initGridView(View v){
        this.mLogging.debug(TAG, "initGridView");
        this.mGridView = (GridView) v.findViewById(R.id.grid_albums);
        //getActivity(),
        if(mGridAlbumsAdapter == null){
            this.initGridAlbumAdapter();
        }
        this.mGridView.setAdapter(mGridAlbumsAdapter);
        this.mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        this.mGridView.setMultiChoiceModeListener(new MultiChoiceModeListener(this.getGridView()));
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLogging.debug(TAG, "onItemClick -> " + position);

                //String url = getParamPictureList().get(position).getThumbnailUrl();
                //mLogging.debug(TAG, "getThumbnailUrl -> " + url);
                //showDialogPicture(getActivity(), getParamPictureList(), position);
                Album selectedAlbum = getAlbumList().getAlbum(position);
                if(selectedAlbum != null) {
                    mListener.onFragmentAlbumInteraction(FragmentAlbum.REQUEST_OPEN_FRAGMENT_PICTURES, selectedAlbum);
                }else{
                    mLogging.debug(TAG, "onItemClick -> Selected album is null");
                }

            }
        });

    }

    private GridView getGridView() {
        return this.mGridView;
    }

    private AlbumList getAlbumList() {
        return this.mAlbumList;
    }

    private void setAlbumList(AlbumList list) {
        this.mAlbumList = list;
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
        public void onFragmentAlbumInteraction(int request, Album selectedAlbum);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentAlbumInteraction(0, null);
        }
    }


}
