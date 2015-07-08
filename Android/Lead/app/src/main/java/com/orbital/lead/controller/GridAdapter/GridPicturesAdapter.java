package com.orbital.lead.controller.GridAdapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;

import java.util.ArrayList;

/**
 * Created by joseph on 27/6/2015.
 */
public class GridPicturesAdapter extends BaseAdapter{
    private final String TAG = this.getClass().getSimpleName();

    //private PictureActivity mPictureActivity;
    private Context mContext;
    private PictureList mPictureList;
    private CustomLogging mLogging;

    private Animation inAnim;
    private Animation outAnim;
    private DisplayImageOptions mOptions;

    public GridPicturesAdapter(PictureList picList){
        this.initLogging();
        mLogging.debug(TAG, "GridPicturesAdapter");

        this.mPictureList = picList;
        this.initDisplayImageOptions();

    }

    @Override
    public int getCount() {
        if(this.getPictureList() == null){
            return 0;
        }
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

        this.mContext = parent.getContext();
        this.initAnimation();

        View root;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.grid_picture_item_layout, null);
        }else{
            root = convertView;
        }

        ViewHolder holder = new ViewHolder(root);
        String imageUrl = this.getPictureList().getList().get(position).getThumbnailUrl();

        mLogging.debug(TAG, "this.getPictureList().get(position).getThumbnailUrl() => " + this.getPictureList().getList().get(position).getThumbnailUrl());
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

        public void setImage(final String url){
            this.mAnimator.setDisplayedChild(1);

            ImageLoader.getInstance()
                    .displayImage(url, this.getImage(), mOptions,
                            new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    //holder.progressBar.setProgress(0);
                                    //holder.progressBar.setVisibility(View.VISIBLE);
                                    //mLogging.debug(TAG, "onLoadingStarted");
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    //mLogging.debug(TAG, "onLoadingFailed");
                                    mAnimator.setDisplayedChild(2);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                   // mLogging.debug(TAG, "onLoadingComplete");
                                    mAnimator.setDisplayedChild(0);
                                }
                            },
                            new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    //mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                    //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });
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

    private PictureList getPictureList() {
        return this.mPictureList;
    }

    private void initDisplayImageOptions() {
        this.mOptions = CustomApplication.getDisplayImageOptions();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }



}

