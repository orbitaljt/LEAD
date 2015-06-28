package com.orbital.lead.controller.GridAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.orbital.lead.R;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Picture;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by joseph on 27/6/2015.
 */
public class GridImageAdapter extends BaseAdapter{
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private ArrayList<Picture> mPictureList;
    private GridView mGrid;
    private CustomLogging mLogging;

    private Animation inAnim;
    private Animation outAnim;

    public GridImageAdapter(Context context, GridView grid, ArrayList<Picture> picList){
        this.initLogging();
        mLogging.debug(TAG, "GridImageAdapter");
        this.mContext = context;
        this.mGrid = grid;
        this.mPictureList = picList;
        this.initAnimation();
    }

    @Override
    public int getCount() {
        return this.getPictureList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mLogging.debug(TAG, "getView");
        CheckableLayout checkLayout;
        ImageView checkableImage;

        /*
        View v = null;

        if (convertView == null) {
            checkLayout = new CheckableLayout(this.getContext());
            checkLayout.setLayoutParams(new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT));

            if(v == null){
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.grid_element_layout, null); // inflate custom list view row layout
            }

            checkLayout.addView(v);

        }else{
            checkLayout = (CheckableLayout) convertView;
            v = checkLayout.getChildAt(0);
        }

        ViewHolder holder = new ViewHolder(v);
        String imageUrl = this.getPictureList().get(position).getThumbnailUrl();

        holder.setImage(imageUrl);

        return checkLayout;
*/
        final View root;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.grid_element_layout, null); // inflate cust
        }else{
            root = convertView;
        }

        ViewHolder holder = new ViewHolder(root);
        String imageUrl = this.getPictureList().get(position).getThumbnailUrl();
        holder.setImage(imageUrl);

        return root;
    }

    private class ViewHolder{
        private ImageView _image;
        private ViewAnimator mAnimator;

        //private TextView _listTextName;

        public ViewHolder(View v){
            this.initViewAnimator(v);
            this._image = (ImageView) v.findViewById(R.id.grid_image);
        }

        /*
        public void setListTextName(String value){

            this._listTextName.setText(value);
        }
        */
        public void setImage(final String url){
            this.mAnimator.setDisplayedChild(1);

            //System.out.println("mAnimator.getMeasuredWidth() => "+ mAnimator.getMeasuredWidth());
            Picasso.with(getContext())
                    .load(url)
                    .noFade()
                    .into(this._image, new Callback() {
                        @Override
                        public void onSuccess() {
                            //mLogging.debug(TAG, "Picasso onSuccess with URL => " + url);
                            mAnimator.setDisplayedChild(0); // show actual image
                        }

                        @Override
                        public void onError() {
                            //mLogging.debug(TAG, "Picasso onError with URL => " + url);
                            mAnimator.setDisplayedChild(2); // failed to load
                        }
                    });

            //.error(R.drawable.image_blank_picture_4_to_3)
        }

        public ImageView getImage(){
            return this._image;
        }

        private void initViewAnimator(View v){
            this.mAnimator = (ViewAnimator) v.findViewById(R.id.animator);
            this.mAnimator.setInAnimation(inAnim);
            this.mAnimator.setOutAnimation(outAnim);
        }


    }//end ViewHolder

    private Context getContext() {
        return this.mContext;
    }

    private ArrayList<Picture> getPictureList() {
        return this.mPictureList;
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

    class CheckableLayout extends FrameLayout implements Checkable {
        private final int[] STATE_CHECKED = new int[] { android.R.attr.state_checked };
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        @SuppressWarnings("deprecation")
        public void setChecked(boolean checked) {
            mLogging.debug(TAG, "mChecked => " + mChecked + " -- checked => " + checked);
            if(mChecked != checked){
                mChecked = checked;
                refreshDrawableState();
            }

            //setBackgroundDrawable(checked ? getResources().getDrawable(R.drawable.framelayout_border_pressed) : null);

/*
            mChecked = checked;
            if(checked){
               setBackgroundResource(R.drawable.framelayout_border_pressed);
            }
*/

            //setBackgroundDrawable(checked ? getResources().getDrawable(R.drawable.framelayout_border_pressed) : null);
        }

        @Override
        public boolean isChecked() {
            return mChecked;
        }

        @Override
        public void toggle() {
            setChecked(!mChecked);
        }

        @Override
        public boolean performClick() {
            toggle();
            return super.performClick();
        }

        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            int[] baseState = super.onCreateDrawableState(extraSpace + 1);
            if (mChecked) {
                mergeDrawableStates(baseState, STATE_CHECKED);
            }
            return baseState;
        }

    }




}

