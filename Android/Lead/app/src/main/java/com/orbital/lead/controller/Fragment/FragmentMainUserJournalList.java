package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerDividerItemDecoration;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerJournalListAdapter;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentMainUserJournalList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainUserJournalList extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ObservableRecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private FloatingActionButton mFab;

    private MainActivity mMainActivity;
    private Logic mLogic;

    private CustomLogging mLogging;
    private final String TAG = this.getClass().getSimpleName();

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
        View rootView = inflater.inflate(R.layout.fragment_main_user_journal_list, container, false);

        this.initLogging();
        this.initLogic();
        this.initRecyclerAdapter(null);
        this.initRecyclerView(rootView);
        this.initFloatingActionButton(rootView);

        if(getMainActivity() instanceof ObservableScrollViewCallbacks){
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(getRecyclerView(), new Runnable() {
                    @Override
                    public void run() {
                        getRecyclerView().scrollVerticallyToPosition(initialPosition);
                    }
                });

            }
        }

           // getRecyclerView().setTouchInterceptionViewGroup((ViewGroup) getMainActivity().findViewById(R.id.root));

        getRecyclerView().setScrollViewCallbacks((ObservableScrollViewCallbacks) getMainActivity());



        return rootView;
    }

    /**
     * Called after onCreateView
     * **/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mLogging.debug(TAG, "onViewCreated");
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
    public void onResume(){
        super.onResume();
        this.mLogging.debug(TAG, "onResume");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.mLogging.debug(TAG, "onDetach");
        mListener = null;
    }

    @Override
    public void onPause(){
        super.onPause();
        this.mLogging.debug(TAG, "onPause");
    }

    public void updateJournalList(JournalList list){
        this.mLogging.debug(TAG, "updateJournalList");
        this.initRecyclerAdapter(list);
        this.refreshRecyclerView();
    }

    public void displaySpecificJournalActivity(Journal journal){
        this.mLogic.displaySpecificJournalActivity(getMainActivity(), journal);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic(){
        this.mLogic = Logic.getInstance();
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
        this.mRecyclerView.setHasFixedSize(false);
        this.mRecyclerView.addItemDecoration(new RecyclerDividerItemDecoration(getMainActivity(), RecyclerDividerItemDecoration.VERTICAL_LIST));
        this.mRecyclerView.setAdapter(this.getListAdapter());

        if(getMainActivity() instanceof ObservableScrollViewCallbacks){
            this.mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) getMainActivity());
        }
    }

    private ObservableRecyclerView getRecyclerView(){
        return this.mRecyclerView;
    }

    private void refreshRecyclerView(){
        this.mRecyclerView.setAdapter(null);
        this.mRecyclerView.setAdapter(this.getListAdapter());
    }

    private void initRecyclerAdapter(JournalList list){
        this.mLogging.debug(TAG, "initRecyclerAdapter");
        View headerView = LayoutInflater.from(getMainActivity()).inflate(R.layout.blank_view_header, null);
        this.mRecyclerAdapter = new RecyclerJournalListAdapter(getMainActivity(), headerView, list, getMainActivity().getCurrentUser());
        ((RecyclerJournalListAdapter) mRecyclerAdapter).setOnItemClickListener(new RecyclerJournalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLogging.debug(TAG, "RecyclerJournalListAdapter onItemClick position -> " + position);
            }
        });
    }

    private RecyclerView.Adapter getListAdapter(){
        return this.mRecyclerAdapter;
    }

    private void initFloatingActionButton(View v){
        this.mFab = (FloatingActionButton) v.findViewById(R.id.fragment_journal_list_fab);
        this.mFab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mLogging.debug(TAG, "FAB is clicked");
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

    /*
    public interface onFragmentUpdateListener {
        public void updateFragmentMainUserJournalList(JournalList list);
    }
    */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMainUserJournalListInteraction(uri);
        }
    }
}
