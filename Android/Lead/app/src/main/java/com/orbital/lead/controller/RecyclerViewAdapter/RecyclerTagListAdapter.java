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

import com.gc.materialdesign.views.CheckBox;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;
import com.orbital.lead.model.TagMap;


/**
 * Created by joseph on 16/6/2015.
 */
public class RecyclerTagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //private static final int VIEW_TYPE_HEADER = 0;
    //private static final int VIEW_TYPE_ITEM = 1;

    private final String TAG = this.getClass().getSimpleName();

    private OnItemClickListener mItemClickListener;
    private Parser mParser;
    private CustomLogging mLogging;
    private Logic mLogic;
    private Context mContext;

    private TagMap recentTagMap;
    private TagList mUsedTagList;
    private TagList mTagList;

    private Animation inAnim;
    private Animation outAnim;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private com.gc.materialdesign.views.CheckBox mCheckBox;
        private TableRow mTableRowTag;
        private TextView mTextTag;
        private ImageView mImageOptions;
        //private ProgressBar mLoadingSpinner;
        private ViewAnimator mAnimator;

        private int pos;
        private boolean isChecked;

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        public ListContentHolder(View v){
            super(v);
            this.initTableRowTag(v);
            this.initCheckBox(v);
            this.initTextTag(v);
            this.initImageOption(v);

        }

        public String getTextTag() {
            return this.mTextTag.getText().toString();
        }

        public void setTextTag(String val){
            this.mTextTag.setText(val);
        }

        public void setTagChecked(final boolean checked){
            //this.mCheckBox.setChecked(checked);
            this.mCheckBox.post(new Runnable() {
                @Override
                public void run() {
                    mCheckBox.setChecked(checked);
                    isChecked = checked;
                }
            });

        }

        public void setPosition(int pos) {
            this.pos = pos;
        }

        private void initTableRowTag(View v){
            this.mTableRowTag = (TableRow) v.findViewById(R.id.tableRowTag);
        }

        private void initCheckBox(View v){
            this.mCheckBox = (com.gc.materialdesign.views.CheckBox) v.findViewById(R.id.checkBox);
            this.mCheckBox.setOncheckListener(new CheckBox.OnCheckListener() {
                @Override
                public void onCheck(CheckBox checkBox, boolean b) {
                    isChecked = b;
                    mLogging.debug(TAG, "mCheckBox onCheck Set " + pos + " to " + b);
                    setCurrentTagCheckedStatus(pos, b);
                }
            });
        }

        private void initTextTag(View v){
            this.mTextTag = (TextView) v.findViewById(R.id.text_tag);
            this.mTextTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTagChecked(!isChecked);
                    mLogging.debug(TAG, "mTextTag onClick Set " + pos + " to " + !isChecked);
                    setCurrentTagCheckedStatus(pos, !isChecked);
                }
            });
        }

        private void initImageOption(View v){
            this.mImageOptions = (ImageView) v.findViewById(R.id.image_more_options);
            this.mImageOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mLogic.showJournalPopUpMenu(mContext, v, EnumDialogEditJournalType.EDIT_TAG, getTextTag());
                }
            });
        }

        private void initViewAnimator(View v){
            this.mAnimator = (ViewAnimator) v.findViewById(R.id.animator);
            this.mAnimator.setInAnimation(inAnim);
            this.mAnimator.setOutAnimation(outAnim);
        }




    }

    //MainActivity activity, View headerView,
    public RecyclerTagListAdapter(TagList currentUsedTagList, TagMap recentTagMap){
        this.initLogging();
        this.initLogic();
        this.initParser();
        //this.setHeaderView(headerView);
        this.setUsedTagList(currentUsedTagList);
        this.setRecentTagMap(recentTagMap);
        this.initCompliedTagList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        this.initAnimation();

        //if(viewType == VIEW_TYPE_HEADER){
        //    return new HeaderViewHolder(mHeaderView);
        //}else{
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.recycler_list_row_tag_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListContentHolder vh = new ListContentHolder(v);
        return vh;
        //}

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListContentHolder){
            //position = position - 1;
            if(this.getCompliedTagList() != null){

                Tag mTag = this.getCompliedTagList().getList().get(position);

                ((ListContentHolder) holder).setTextTag(mTag.getName());
                ((ListContentHolder) holder).setTagChecked(mTag.getIsChecked());
                ((ListContentHolder) holder).setPosition(position);

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

        return count;
        */
        if(this.getCompliedTagList() != null){
            return this.getCompliedTagList().size();
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

    private void initCompliedTagList() {
        this.mTagList = new TagList();

        // all currently used tags compile together with the unused tags

        //mLogging.debug(TAG, "================== copy all Tag List into hashmap ==================");
        for(Tag tag : this.getUsedTagList().getList()){
            this.getRecentTagMap().setValue(tag.getName(), tag.getIsChecked());
            //mLogging.debug(TAG, tag.getName() + " => " + tag.getIsChecked());
        }

        /*
        mLogging.debug(TAG, "================== getRecentTagMap Tag List ==================");
        for (Tag tag : getRecentTagMap().getTagList().getList()) {
            mLogging.debug(TAG, tag.getName() + " => " + tag.getIsChecked());
        }
        */

        // display all tags based on the compiled tag map
        this.mTagList.setList(this.getRecentTagMap().getTagList().getList());
    }

    private void setUsedTagList(TagList list){
        this.mUsedTagList = list;
    }


    private void setRecentTagMap(TagMap map) {
        this.recentTagMap = new TagMap(map.getMap());
    }

    private void setCurrentTagCheckedStatus(int position, boolean val) {
        this.getCompliedTagList().getList().get(position).setIsChecked(val);
    }

    private Parser getParser(){
        return this.mParser;
    }

    private Context getContext(){
        return this.mContext;
    }

    private TagList getUsedTagList(){
        return this.mUsedTagList;
    }

    public TagList getCompliedTagList() {
        return this.mTagList;
    }

    public TagMap getRecentTagMap(){
        return this.recentTagMap;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

}
