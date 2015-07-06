package com.orbital.lead.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by joseph on 25/6/2015.
 */
public class SquareImageView extends ImageView {

    int newWidth = 0;
    int newHeight = 0;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = newWidth;

        //int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        setMeasuredDimension(newWidth, newHeight);
    }

    public int getNewWidth(){
        return newWidth;
    }


}
