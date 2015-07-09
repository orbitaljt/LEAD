package com.orbital.lead.controller.ViewPagerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.R;
import com.orbital.lead.model.Picture;

import java.util.ArrayList;

/**
 * Created by joseph on 30/6/2015.
 */
public class PagerImageAdapter extends PagerAdapter {
    private ArrayList<Picture> list;
    private LayoutInflater inflater;
    private DisplayImageOptions options;


    public PagerImageAdapter(Context context, ArrayList<Picture> list){
        this.inflater = LayoutInflater.from(context);
        this.options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.image_blank_picture_16_to_9)
                .resetViewBeforeLoading(true)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        this.list = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.dialog_viewpager_picture_item_layout, view, false);

        ImageView imagePopup = (ImageView) imageLayout.findViewById(R.id.image_popup);
        final ViewAnimator animator = (ViewAnimator) imageLayout.findViewById(R.id.animator);

        animator.setDisplayedChild(1);

        ImageLoader.getInstance()
                .displayImage(list.get(position).getThumbnailUrl(), imagePopup, options,
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
                                animator.setDisplayedChild(2);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                //holder.progressBar.setVisibility(View.GONE);
                                //mLogging.debug(TAG, "onLoadingComplete");
                                animator.setDisplayedChild(0);
                            }
                        },
                        new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                //mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                            }
                        });

        view.addView(imageLayout, 0);
        return imageLayout;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}

