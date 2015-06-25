package com.orbital.lead.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by joseph on 25/6/2015.
 */
public class WideImageView extends ImageView {

    public WideImageView(Context context) {
        super(context);
    }

    public WideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WideImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = (newWidth * 9)/16;

        //int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        setMeasuredDimension(newWidth, newHeight);
    }


}
