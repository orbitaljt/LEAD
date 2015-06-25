package com.orbital.lead.controller.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.joooonho.SelectableRoundedImageView;
import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.FormatTime;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by joseph on 16/6/2015.
 */
public class RecyclerJournalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private final String TAG = this.getClass().getSimpleName();

    private OnItemClickListener mItemClickListener;
    private Parser mParser;
    private CustomLogging mLogging;
    private MainActivity activity;

    private JournalList mJournalList;
    private User mCurrentUser;
    private View mHeaderView;

    private Animation inAnim;
    private Animation outAnim;


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardParent;
        private TextView mTextTitle;
        private TextView mTextSubTitle;
        private SelectableRoundedImageView mCardTopImage;
        private SelectableRoundedImageView mCardFailedImage;
        private ProgressBar mLoadingSpinner;
        private ViewAnimator mAnimator;
        private String journalImageUrl = "";


        public ListContentHolder(View v){
            super(v);
            this.initCardViewParent(v);
            this.mTextTitle = (TextView) v.findViewById(R.id.text_view_title);
            this.mTextSubTitle = (TextView) v.findViewById(R.id.text_view_subtitle);
            this.initTopImageView(v);
            this.mCardFailedImage = (SelectableRoundedImageView) v.findViewById(R.id.cardview_failed_image);
            this.mLoadingSpinner = (ProgressBar) v.findViewById(R.id.loading_spinner);
            this.initViewAnimator(v);
        }

        public void setCardTextTitle(String val){
            this.mTextTitle.setText(val);
        }

        public void setCardTextSubTitle(String val){
            this.mTextSubTitle.setText(val);
        }

        public void setCardTopImage(Context context, String url){
            this.mAnimator.setDisplayedChild(1);
            this.journalImageUrl = url;
            //.error(R.drawable.image_blank_picture)
            //.transform(new RoundedTransformation(10, 0))
            Picasso.with(context)
                    .load(url)
                    .noFade()
                    .into(this.mCardTopImage, new Callback() {
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
        }

        public CardView getCardViewParent(){
            return this.mCardParent;
        }

        public String getJournalImageUrl(){
            return this.journalImageUrl;
        }

        private SelectableRoundedImageView getTopImageView(){
            return this.mCardTopImage;
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }


        private void initCardViewParent(View v){
            this.mCardParent = (CardView) v.findViewById(R.id.card_view_journal);
            this.mCardParent.setPreventCornerOverlap(false);

        }

        private void initTopImageView(View v){
            this.mCardTopImage = (SelectableRoundedImageView) v.findViewById(R.id.cardview_top_image);

            /*
            this.mCardTopImage.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mLogging.debug(TAG, "right => " + right);
                    mLogging.debug(TAG, "left => " + left);
                    mLogging.debug(TAG, "top => " + top);
                    mLogging.debug(TAG, "bottom => " + bottom);

                    int width = right - left;
                    int height = bottom - top;

                    //resizeTopImageView(width, height);
                }
            });
            */
        }

        private void initViewAnimator(View v){
            this.mAnimator = (ViewAnimator) v.findViewById(R.id.animator);
            this.mAnimator.setInAnimation(inAnim);
            this.mAnimator.setOutAnimation(outAnim);
        }





        private void resizeTopImageView(int currentWidth, int currentHeight){
            int newWidth = currentWidth;
            //int newHeight = (width * 9)/16;
            int newHeight = 100;
            mLogging.debug(TAG, "set imageview new width height => " + newWidth + ", " + newHeight);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
            mCardTopImage.setLayoutParams(layoutParams);

        }




    }

    public RecyclerJournalListAdapter(MainActivity activity, View headerView, JournalList list, User currentUser){
        this.initLogging();
        this.initParser();
        this.setHeaderView(headerView);
        this.setMainActivity(activity);
        this.setJournalList(list);
        this.setCurrentUser(currentUser);
        this.initAnimation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER){
            return new HeaderViewHolder(mHeaderView);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_row_user_journal_list_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ListContentHolder vh = new ListContentHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListContentHolder){
            position = position - 1;
            if(getJournalList() != null && getCurrentUser() != null){
                final Journal j = this.getJournalList().get(position);
                String title = j.getTitle();
                String pictureCoverID = j.getPictureCoverID();
                String pictureCoverType = j.getPictureCoverType().toString();
                String journalDate = FormatDate.parseDate(j.getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
                String journalTime = FormatTime.parseTime(j.getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME);
                String userID = this.getCurrentUser().getUserID();
                String pictureUrl = mParser.createPictureCoverUrl(pictureCoverID, pictureCoverType, userID);

                ((ListContentHolder) holder).setCardTopImage(getMainActivity(), pictureUrl);
                ((ListContentHolder) holder).setCardTextTitle(title);
                ((ListContentHolder) holder).setCardTextSubTitle(journalDate + " at " + journalTime);
                ((ListContentHolder) holder).getCardViewParent().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLogging.debug(TAG, "mCardParent clicked");
                        getMainActivity().getFragmentMainUserJournalList().displaySpecificJournalActivity(j);
                    }
                });

                ((ListContentHolder) holder).getTopImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLogging.debug(TAG, "mCardTopImage clicked");
                        getMainActivity().getFragmentMainUserJournalList().displaySpecificJournalActivity(j);
                    }
                });

            }
        }


    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(mHeaderView != null){
            count++;
        }

        if(this.getJournalList() != null){
            count += this.getJournalList().size();
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
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

    private void setMainActivity(MainActivity activity){
        this.activity = activity;
    }

    private void setHeaderView(View v){
        this.mHeaderView = v;
    }

    private void setJournalList(JournalList list){
        this.mJournalList = list;
    }

    private void setCurrentUser(User user){
        this.mCurrentUser = user;
    }

    private Parser getParser(){
        return this.mParser;
    }

    private MainActivity getMainActivity(){
        return this.activity;
    }

    private JournalList getJournalList(){
        return this.mJournalList;
    }

    private User getCurrentUser(){
        return this.mCurrentUser;
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getMainActivity(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getMainActivity(),android.R.anim.fade_out);
    }
}
