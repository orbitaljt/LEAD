package com.orbital.lead.controller.RecyclerViewAdapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.FormatTime;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;
import com.orbital.lead.widget.FourThreeImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by joseph on 16/6/2015.
 */
public class RecyclerJournalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //private static final int VIEW_TYPE_HEADER = 0;
    //private static final int VIEW_TYPE_ITEM = 1;

    private final String TAG = this.getClass().getSimpleName();

    private OnItemClickListener mItemClickListener;
    private Parser mParser;
    private CustomLogging mLogging;
    private Logic mLogic;
    private Context mContext;
    //private MainActivity activity;

    private JournalList mJournalList;
    private User mCurrentUser;
    //private View mHeaderView;

    private Animation inAnim;
    private Animation outAnim;

    private DisplayImageOptions mOptions;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TableRow mTableRowContent;
        private TextView mTextTitle;
        private TextView mTextSubTitle;
        private FourThreeImageView mImagePicture;
        private ProgressBar mLoadingSpinner;
        private ViewAnimator mAnimator;

        private int imageWidth = 0;

        public ListContentHolder(View v){
            super(v);
            this.initTableRowContent(v);
            this.mTextTitle = (TextView) v.findViewById(R.id.text_view_title);
            this.mTextSubTitle = (TextView) v.findViewById(R.id.text_view_subtitle);
            this.initThumbnailImageView(v);
            this.mLoadingSpinner = (ProgressBar) v.findViewById(R.id.loading_spinner);
            this.initViewAnimator(v);
        }

        public String getTextTitle() {
            return this.mTextTitle.getText().toString();
        }

        public void setTextTitle(String val){
            this.mTextTitle.setText(val);
        }

        public void setTextSubTitle(String val){
            this.mTextSubTitle.setText(val);
        }

        public void setThumbnailImage(String url){
            mLogging.debug(TAG, "setThumbnailImage");
            this.mAnimator.setDisplayedChild(1);

            ImageLoader.getInstance()
                    .displayImage(url, this.mImagePicture, mOptions,
                            new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    //holder.progressBar.setProgress(0);
                                    //holder.progressBar.setVisibility(View.VISIBLE);
                                    mLogging.debug(TAG, "onLoadingStarted");
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    mLogging.debug(TAG, "onLoadingFailed");
                                    mAnimator.setDisplayedChild(2);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    mLogging.debug(TAG, "onLoadingComplete");
                                    mAnimator.setDisplayedChild(0);
                                }
                            },
                            new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                    //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                    });



            /*
            Picasso.with(context)
                    .load(url)
                    .error(R.drawable.image_blank_picture_16_to_9)
                    .noFade()
                    .into(this.mImagePicture, new Callback() {
                        @Override
                        public void onSuccess() {
                            mLogging.debug(TAG, "Picasso onSuccess");
                            mAnimator.setDisplayedChild(0); // show actual image
                        }

                        @Override
                        public void onError() {
                            mLogging.debug(TAG, "Picasso onError");
                            mAnimator.setDisplayedChild(2);
                        }
                    });

                    */

        }

        public TableRow getTableRowContent() {
            return this.mTableRowContent;
        }

        private ImageView getImagePicture() {
            return this.mImagePicture;
        }
       // private SelectableRoundedImageView getTopImageView(){
       //     return this.mCardTopImage;
       // }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        private void initTableRowContent(View v){
            this.mTableRowContent = (TableRow) v.findViewById(R.id.tableRow_content);
        }

        private void initThumbnailImageView(View v){
            this.mImagePicture = (FourThreeImageView) v.findViewById(R.id.image_picture);
        }

        private void initViewAnimator(View v){
            this.mAnimator = (ViewAnimator) v.findViewById(R.id.animator);
            this.mAnimator.setInAnimation(inAnim);
            this.mAnimator.setOutAnimation(outAnim);
        }

    }

    //MainActivity activity, View headerView,
    public RecyclerJournalListAdapter(JournalList list, User currentUser){
        this.initLogging();
        this.initLogic();
        this.initParser();
        //this.setHeaderView(headerView);
        this.setJournalList(list);
        this.setCurrentUser(currentUser);
        this.initDisplayImageOptions();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        this.initAnimation();

        //if(viewType == VIEW_TYPE_HEADER){
        //    return new HeaderViewHolder(mHeaderView);
        //}else{
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_row_user_journal_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListContentHolder vh = new ListContentHolder(v);
        return vh;
        //}

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListContentHolder){
            //position = position - 1;
            if(getJournalList() != null && getCurrentUser() != null){
                final Journal j = this.getJournalList().get(position);
                String title = j.getTitle();
                String pictureCoverID = j.getPictureCoverID();
                String pictureCoverType = j.getPictureCoverType().toString();
                String journalDate = FormatDate.parseDate(j.getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
                String journalTime = FormatTime.parseTime(j.getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME);
                String userID = this.getCurrentUser().getUserID();
                String pictureUrl = mParser.createPictureCoverUrl(pictureCoverID, pictureCoverType, userID);

                ((ListContentHolder) holder).setThumbnailImage(pictureUrl);
                ((ListContentHolder) holder).setTextTitle(title);
                ((ListContentHolder) holder).setTextSubTitle(journalDate + " at " + journalTime);

                ((ListContentHolder) holder).getTableRowContent().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLogging.debug(TAG, "mCardParent clicked");
                        mLogic.displaySpecificJournalActivity(getContext(), j);
                        //getContext().getFragmentMainUserJournalList().displaySpecificJournalActivity(j);
                    }
                });

                ((ListContentHolder) holder).getImagePicture().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLogging.debug(TAG, "mCardTopImage clicked");
                        mLogic.displaySpecificJournalActivity(getContext(), j);
                        //getContext().getFragmentMainUserJournalList().displaySpecificJournalActivity(j);
                    }
                });

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

        if(this.getJournalList() != null){
            count += this.getJournalList().size();
        }

        return count;
        */
        if(this.getJournalList() != null){
            return this.getJournalList().size();
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

    private void setJournalList(JournalList list){
        this.mJournalList = list;
    }

    private void setCurrentUser(User user){
        this.mCurrentUser = user;
    }

    private Parser getParser(){
        return this.mParser;
    }

    /*
    private MainActivity getMainActivity(){
        return this.activity;
    }
*/
    private Context getContext(){
        return this.mContext;
    }

    private JournalList getJournalList(){
        return this.mJournalList;
    }

    private User getCurrentUser(){
        return this.mCurrentUser;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

    private void initDisplayImageOptions() {
        this.mOptions = CustomApplication.getDisplayImageOptions();
    }

}
