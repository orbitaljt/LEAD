package com.orbital.lead.controller;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.orbital.lead.R;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerJournalListAdapter;
import com.orbital.lead.logic.CustomLogging;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMainUserJournalList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainUserJournalList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainUserJournalList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ObservableRecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;

    private MainActivity mMainActivity;

    private CustomLogging mLogging;
    private final String TAG_FRAGMENT_LOGIN = this.getClass().getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMainUserJournalList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMainUserJournalList newInstance(String param1, String param2) {
        FragmentMainUserJournalList fragment = new FragmentMainUserJournalList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMainUserJournalList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_user_journal_list, container, false);
    }

    /**
     * Called after onCreateView
     * **/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initLogging();
        this.initRecyclerAdapter();
        this.initRecyclerView(view);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.initMainActivity(activity);
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


    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initMainActivity(Activity activity){
        if(activity instanceof MainActivity){
            this.mMainActivity = (MainActivity) activity;
        }
    }

    private MainActivity getMainActivity(){
        return this.mMainActivity;
    }

    private void initRecyclerView(View v){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        this.mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.scroll);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);

        if(getMainActivity() instanceof ObservableScrollViewCallbacks){
            this.mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) getMainActivity());
        }


    }

    private void initRecyclerAdapter(){
        ArrayList<String> arr = new ArrayList<String>();
        for(int i =1 ; i<=20; i++){
            arr.add("test - " + i);
        }

        this.mRecyclerAdapter = new RecyclerJournalListAdapter(getMainActivity(), arr);
        ((RecyclerJournalListAdapter) mRecyclerAdapter).setOnItemClickListener(new RecyclerJournalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLogging.debug(TAG_FRAGMENT_LOGIN, "RecyclerJournalListAdapter onItemClick position -> " + position);
            }
        });
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
        public void onFragmentMainUserJournalListInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMainUserJournalListInteraction(uri);
        }
    }

}
