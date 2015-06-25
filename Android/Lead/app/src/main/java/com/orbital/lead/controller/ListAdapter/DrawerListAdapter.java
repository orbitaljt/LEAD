package com.orbital.lead.controller.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.model.DrawerListObject;

import java.util.ArrayList;

/**
 * Created by joseph on 21/6/2015.
 */
public class DrawerListAdapter extends ArrayAdapter<String>{

    public DrawerListAdapter(Context context, int textViewResourceId, ArrayList<String> obj){
        super(context, textViewResourceId, obj);
        DrawerListObject.initList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.drawer_list_row_layout, null); // inflate custom list view row layout
        }

        ViewHolder holder = new ViewHolder(v);

        String title = DrawerListObject.getTitle(position);
        int imageID = DrawerListObject.getImage(position);

        holder.setListTextName(title);
        holder.setListIcon(imageID);

        return v;
    }


    @Override
    public int getCount(){
        return DrawerListObject.getCount();
    }

    @Override
    public String getItem(int position) {
        return DrawerListObject.getTitle(position);
    }

    private class ViewHolder{
        private ImageView _listIcon;
        private TextView _listTextName;

        public ViewHolder(View v){
            this._listIcon = (ImageView) v.findViewById(R.id.drawer_list_icon);
            this._listTextName = (TextView) v.findViewById(R.id.drawer_list_text);
        }

        public void setListTextName(String value){
            this._listTextName.setText(value);
        }

        public void setListIcon(int id){
            this._listIcon.setImageResource(id);
        }



    }//end ViewHolder

}
