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
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.Picture;

import java.util.ArrayList;

/**
 * Created by joseph on 27/6/2015.
 */
public class GridAlbumsAdapter extends BaseAdapter{
    private final String TAG = this.getClass().getSimpleName();

    //private PictureActivity mPictureActivity;
    private Context mContext;
    private AlbumList mAlbumList;
    private CustomLogging mLogging;
    private Parser mParser;

    private Animation inAnim;
    private Animation outAnim;
    private DisplayImageOptions mOptions;

    public GridAlbumsAdapter(AlbumList albumList){
        this.initLogging();
        this.initParser();
        mLogging.debug(TAG, "GridAlbumsAdapter");

        this.mAlbumList = albumList;
        this.initDisplayImageOptions();

    }

    @Override
    public int getCount() {
        if(this.getAlbumList() == null){
            return 0;
        }
        return this.getAlbumList().size();
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
        //CheckableLayout checkLayout;
        //ImageView checkableImage;

        this.mContext = parent.getContext();
        this.initAnimation();

        View root;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.grid_album_item_layout, null);
        }else{
            root = convertView;
        }

        ViewHolder holder = new ViewHolder(root);
        String imageUrl = this.getAlbumList().getList().get(position).getThumbnailUrl(); // get album picture cover thumbnail url
        String albumTitle = this.getAlbumList().getList().get(position).getTitle();
        String numOfPictures = this.getParser().convertIntegerToString(this.getAlbumList().getList().get(position).getNumberOfPicture());

        holder.setImage(imageUrl);
        holder.setTextTitle(albumTitle);
        holder.setTextSubtitle(Constant.GRID_ALBUM_NUMBER_OF_PICTURES_FORMAT.replace(Constant.GRID_DUMMY_NUMBER_OF_PICTURES, numOfPictures));

        return root;
    }

    private class ViewHolder{
        private ImageView _image;
        private ViewAnimator mAnimator;
        private TextView textTitle;
        private TextView textSubtitle;

        public ViewHolder(View v){
            this.initViewAnimator(v);
            this._image = (ImageView) v.findViewById(R.id.grid_image);
            this.textTitle = (TextView) v.findViewById(R.id.text_album_title);
            this.textSubtitle = (TextView) v.findViewById(R.id.text_album_subtitle);
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
                                   // mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                    //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });
        }

        public void setTextTitle(String val) {
            if(getParser().isStringEmpty(val)){
                this.textTitle.setText(Constant.GRID_ALBUM_DEFAULT_UNTITLED);

            }else{
                this.textTitle.setText(val);
            }

        }

        public void setTextSubtitle(String val) {
            this.textSubtitle.setText(val);
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
/*
    private PictureActivity getPictureActivity() {
        return this.mPictureActivity;
    }

*/

    private AlbumList getAlbumList() {
        return this.mAlbumList;
    }

    private void initDisplayImageOptions() {
        this.mOptions = CustomApplication.getDisplayImageOptions();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initParser(){
        this.mParser = Parser.getInstance();
    }

    private void initAnimation(){
        this.inAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        this.outAnim = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
    }

    private Parser getParser(){
        return this.mParser;
    }



}

