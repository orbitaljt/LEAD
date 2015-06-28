package com.orbital.lead.controller.GridAdapter;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.orbital.lead.R;

/**
 * Created by joseph on 27/6/2015.
 */
public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {

    private GridView mGrid;

    public MultiChoiceModeListener(GridView grid) {
        this.mGrid = grid;
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.setTitle("Select Items");
        mode.setSubtitle("One item selected");
        return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return true;
    }

    public void onDestroyActionMode(ActionMode mode) {
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                          boolean checked) {
        int selectCount = mGrid.getCheckedItemCount();
        switch (selectCount) {
            case 1:
                mode.setSubtitle("One item selected");
                break;
            default:
                mode.setSubtitle("" + selectCount + " items selected");
                break;
        }

        //View v = mGrid.getChildAt(position);
        //ImageView img = (ImageView) v.findViewById(R.id.image_test);

        if(checked){
            //img.setVisibility(View.VISIBLE);
            //FrameLayout root = (FrameLayout) v.findViewById(R.id.grid_element_frame_layout);
            //root.setBackgroundResource(R.drawable.framelayout_border_pressed);
        }else{
            //img.setVisibility(View.INVISIBLE);
        }

    }

}