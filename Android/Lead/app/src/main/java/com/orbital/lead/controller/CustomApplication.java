package com.orbital.lead.controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orbital.lead.R;
import com.orbital.lead.model.EnumAndroidVersion;

/**
 * Created by joseph on 29/6/2015.
 */
public class CustomApplication extends Application{

    private static EnumAndroidVersion version = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        checkAndroidVersion();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.memoryCacheExtraOptions(576, 324);
        config.diskCacheExtraOptions(576, 324, null);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions getDisplayImageOptions(){
        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_image_blank)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        return mOptions;
    }

    public static void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT < 19) {
            version = EnumAndroidVersion.LESS_THAN_19;
        }else {
            version = EnumAndroidVersion.MORE_THAN_EQUAL_19;
        }
    }

    public static EnumAndroidVersion getAndroidVersion() {
        if(version == null) {
            checkAndroidVersion();
        }
        return version;
    }
}
