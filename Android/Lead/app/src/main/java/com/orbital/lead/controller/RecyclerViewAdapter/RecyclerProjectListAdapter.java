package com.orbital.lead.controller.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;


/**
 * Created by joseph on 16/6/2015.
 */
public class RecyclerProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //private static final int VIEW_TYPE_HEADER = 0;
    //private static final int VIEW_TYPE_ITEM = 1;

    private final String TAG = this.getClass().getSimpleName();

    private OnItemClickListener mItemClickListener;
    private Parser mParser;
    private CustomLogging mLogging;
    private Logic mLogic;
    private Context mContext;

    private ProjectList mProjectList;

    private Animation inAnim;
    private Animation outAnim;

    private Project storeInitSelectedProject;

    private String selectedProjectID;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TableRow mTableRowProject;
        private TextView mTextProject;
        private ImageView mImageOptions;
        private ImageView mImageSelected;
        //private ProgressBar mLoadingSpinner;
        private ViewAnimator mAnimator;

        private boolean isSelect;
        private int pos;

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        public ListContentHolder(View v){
            super(v);
            this.initTableRowProject(v);
            this.initTextProjectTitle(v);
            this.initImageSelected(v);
            this.initImageOption(v);
        }

        public String getTextTitle() {
            return this.mTextProject.getText().toString();
        }

        public void setTextProjectTitle(String val){
            this.mTextProject.setText(val);
        }

        public void setPosition(int pos) {
            this.pos = pos;
        }

        public void setIsSelected(boolean val) {
            this.isSelect = val;
        }

        public void toggleImageSelected(boolean selected){
            if(selected){
                mLogging.debug(TAG, "Position " + pos + " image show");
                this.showImageSelected();
            }else{
                mLogging.debug(TAG, "Position " + pos + " image hide");
                this.hideImageSelected();
            }
        }

        public void showImageSelected(){
            this.mImageSelected.setVisibility(View.VISIBLE);
        }

        public void hideImageSelected(){
            this.mImageSelected.setVisibility(View.INVISIBLE);
        }

        private void initTableRowProject(View v){
            this.mTableRowProject = (TableRow) v.findViewById(R.id.tableRowProject);
        }

        private void initTextProjectTitle(View v){
            this.mTextProject = (TextView) v.findViewById(R.id.text_project);
            this.mTextProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // select as this project
                    isSelect = !isSelect;
                    mLogging.debug(TAG, "mTextProject onClick => " + isSelect);
                    toggleImageSelected(isSelect);

                    selectCurrentProject(pos, isSelect);

                }
            });
        }

        private void initImageSelected(View v) {
            this.mImageSelected = (ImageView) v.findViewById(R.id.image_selected);
        }

        private void initImageOption(View v){
            this.mImageOptions = (ImageView) v.findViewById(R.id.image_more_options);
            this.mImageOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLogic.showJournalPopUpMenu(mContext, v, EnumDialogEditJournalType.EDIT_PROJECT, null, getProjectList(), pos);
                }
            });
        }



    }

    //MainActivity activity, View headerView,
    public RecyclerProjectListAdapter(ProjectList list, String selectedProjectID){
        this.initLogging();
        this.initLogic();
        this.initParser();
        this.setProjectList(list);

        for(Project p : this.getProjectList().getList()) {
            if(p.getProjectID().equals(selectedProjectID)){
                p.setIsSelected(true);
            }
        }

       // this.setSelectedProjectID(selectedProjectID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        this.initAnimation();

        View v = LayoutInflater.from(this.mContext).inflate(R.layout.recycler_list_row_project_layout, parent, false);
        ListContentHolder vh = new ListContentHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListContentHolder){

            if(this.getProjectList() != null){
                Project mProject = this.getProjectList().getList().get(position);
                ((ListContentHolder) holder).setTextProjectTitle(mProject.getName());
                ((ListContentHolder) holder).setPosition(position);
                ((ListContentHolder) holder).setIsSelected(mProject.getIsSelected());
                ((ListContentHolder) holder).toggleImageSelected(mProject.getIsSelected());

                /*
                if(mProject.getProjectID().equals(this.getSelectedProjectID())){
                    ((ListContentHolder) holder).showImageSelected();
                    storeInitSelectedProject = mProject;
                }else{
                    ((ListContentHolder) holder).hideImageSelected();
                }
                */

            }
        }


    }

    @Override
    public int getItemCount() {
        if(this.getProjectList() != null){
            return this.getProjectList().size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        //return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        return 0;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public Project getSelectedProject() {
        for(Project p : this.getProjectList().getList()) {
            if(p.getIsSelected()){
                return new Project(p);
            }
        }
        return null;
    }

    private void initParser(){
        this.mParser = Parser.getInstance();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }



    private void setProjectList(ProjectList list){
        this.mProjectList = new ProjectList();
        this.mProjectList.setList(list);
    }

    /*
    private void setSelectedProjectID(String val) {
        this.selectedProjectID = val;
    }
    */

    private Parser getParser(){
        return this.mParser;
    }

    private Context getContext(){
        return this.mContext;
    }

    private ProjectList getProjectList(){
        return this.mProjectList;
    }

    private String getSelectedProjectID() {
        return this.selectedProjectID;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

    private void selectCurrentProject(int position, boolean val) {
        for(int i=0; i< this.getProjectList().size(); i++){
            if(i != position){
                this.getProjectList().getList().get(i).setIsSelected(false);
            }else{
                if(val){
                    this.getProjectList().getList().get(i).setIsSelected(val);
                }else{
                    this.getProjectList().getList().get(i).setIsSelected(false);
                }
            }
        }
        this.notifyDataSetChanged();
    }



}
