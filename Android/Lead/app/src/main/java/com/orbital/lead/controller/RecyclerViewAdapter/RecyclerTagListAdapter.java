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
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;


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
            this.initTextTag(v);
            this.initImageOption(v);
            this.mCheckBox = (com.gc.materialdesign.views.CheckBox) v.findViewById(R.id.checkBox);

        }

        public String getTextTitle() {
            return this.mTextTag.getText().toString();
        }

        public void setTextTagName(String val){
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

        private void initTableRowTag(View v){
            this.mTableRowTag = (TableRow) v.findViewById(R.id.tableRowTag);
        }

        private void initTextTag(View v){
            this.mTextTag = (TextView) v.findViewById(R.id.text_tag);
            this.mTextTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTagChecked(!isChecked);
                }
            });
        }

        private void initImageOption(View v){
            this.mImageOptions = (ImageView) v.findViewById(R.id.image_more_options);
            this.mImageOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mLogic.showJournalPopUpMenu(mContext, v, EnumDialogEditJournalType.EDIT_TAG, getTextTitle());
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
    public RecyclerTagListAdapter(TagList overallTaglist, TagList currentUsedTagList){
        this.initLogging();
        this.initLogic();
        this.initParser();
        //this.setHeaderView(headerView);
        this.setCurrentUsedTagList(currentUsedTagList);
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
            if(this.getTagList() != null){
                /*
                final Journal j = this.getTagList().get(position);
                String title = j.getTitle();
                String pictureCoverID = j.getPictureCoverID();
                String pictureCoverType = j.getPictureCoverType().toString();
                String journalDate = FormatDate.parseDate(j.getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
                String journalTime = FormatTime.parseTime(j.getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME);
                String userID = this.getCurrentUser().getUserID();
                String pictureUrl = mParser.createPictureCoverUrl(pictureCoverID, pictureCoverType, userID);
                */
                Tag mTag = this.getTagList().getList().get(position);

                ((ListContentHolder) holder).setTextTagName(mTag.getName());
                ((ListContentHolder) holder).setTagChecked(mTag.getIsChecked());

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

        if(this.getTagList() != null){
            count += this.getTagList().size();
        }

        return count;
        */
        if(this.getTagList() != null){
            return this.getTagList().size();
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


    private void setCurrentUsedTagList(TagList list){
        this.mTagList = list;
    }

    private Parser getParser(){
        return this.mParser;
    }

    private Context getContext(){
        return this.mContext;
    }

    private TagList getTagList(){
        return this.mTagList;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

}
