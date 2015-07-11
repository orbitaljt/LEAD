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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TableRow mTableRowProject;
        private TextView mTextProject;
        private ImageView mImageOptions;
        //private ProgressBar mLoadingSpinner;
        private ViewAnimator mAnimator;

        private boolean isChecked;

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
            this.initImageOption(v);
        }

        public String getTextTitle() {
            return this.mTextProject.getText().toString();
        }

        public void setTextProjectTitle(String val){
            this.mTextProject.setText(val);
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
                }
            });
        }

        private void initImageOption(View v){
            this.mImageOptions = (ImageView) v.findViewById(R.id.image_more_options);
            this.mImageOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLogic.showJournalPopUpMenu(mContext, v, EnumDialogEditJournalType.EDIT_PROJECT, getTextTitle());
                }
            });
        }

    }

    //MainActivity activity, View headerView,
    public RecyclerProjectListAdapter(ProjectList list){
        this.initLogging();
        this.initLogic();
        this.initParser();
        //this.setHeaderView(headerView);
        this.setProjectList(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        this.initAnimation();

        //if(viewType == VIEW_TYPE_HEADER){
        //    return new HeaderViewHolder(mHeaderView);
        //}else{
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.recycler_list_row_project_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListContentHolder vh = new ListContentHolder(v);
        return vh;
        //}

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListContentHolder){
            //position = position - 1;
            if(this.getProjectList() != null){
                /*
                final Journal j = this.getProjectList().get(position);
                String title = j.getTitle();
                String pictureCoverID = j.getPictureCoverID();
                String pictureCoverType = j.getPictureCoverType().toString();
                String journalDate = FormatDate.parseDate(j.getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
                String journalTime = FormatTime.parseTime(j.getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME);
                String userID = this.getCurrentUser().getUserID();
                String pictureUrl = mParser.createPictureCoverUrl(pictureCoverID, pictureCoverType, userID);
                */
                Project mProject = this.getProjectList().getList().get(position);

                mLogging.debug(TAG, "mProject.getTitle() => " + mProject.getTitle());

                ((ListContentHolder) holder).setTextProjectTitle(mProject.getTitle());

            }
        }


    }

    @Override
    public int getItemCount() {
        /*
        int count = 0;
        if(mHeaderView != null){
            count++;
        }

        if(this.getProjectList() != null){
            count += this.getProjectList().size();
        }

        return count;
        */
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


    private void initParser(){
        this.mParser = Parser.getInstance();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }


    /*
    private void setMainActivity(MainActivity activity){
        this.activity = activity;
    }
    */

    /*
    private void setHeaderView(View v){
        this.mHeaderView = v;
    }
    */

    private void setProjectList(ProjectList list){
        this.mProjectList = list;
    }

    private Parser getParser(){
        return this.mParser;
    }

    private Context getContext(){
        return this.mContext;
    }

    private ProjectList getProjectList(){
        return this.mProjectList;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

}
