package com.orbital.lead.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by joseph on 21/6/2015.
 */
public class DrawerListObject {

    public static ArrayList<Holder> list;

    public static void initList(){
        if(list == null){
            list = new ArrayList<Holder>();

            Holder mHolder = null;

            mHolder = new Holder(Constant.DRAWER_LIST_PROFILE_NAME, Constant.DRAWER_LIST_PROFILE_ICON);
            list.add(mHolder);
            mHolder = new Holder(Constant.DRAWER_LIST_BADGE_NAME, Constant.DRAWER_LIST_BADGE_ICON);
            list.add(mHolder);
            mHolder = new Holder(Constant.DRAWER_LIST_MILESTONE_NAME, Constant.DRAWER_LIST_MILESTONE_ICON);
            list.add(mHolder);
            mHolder = new Holder(Constant.DRAWER_LIST_JOURNAL_NAME, Constant.DRAWER_LIST_JOURNAL_ICON);
            //list.add(mHolder);
            //mHolder = new Holder(Constant.DRAWER_LIST_EXPERIENCE_NAME, Constant.DRAWER_LIST_EXPERIENCE_ICON);
            list.add(mHolder);
            mHolder = new Holder(Constant.DRAWER_LIST_SETTING_NAME, Constant.DRAWER_LIST_SETTING_ICON);
            list.add(mHolder);

        }

    }

    public static int getCount(){
        return list.size();
    }

    public static String getTitle(int position){
        if(position >= 0 && position < getCount()){
            return list.get(position).getTitle();
        }else{
            return "";
        }
    }

    public static int getImage(int position){
        if(position >= 0 && position <= getCount()){
            return list.get(position).getImageID();
        }else{
            return 0;
        }
    }





    private static class Holder{
        private String title;
        private int imageID;

        public Holder(String title, int id){
            this.title = title;
            this.imageID = id;
        }

        public String getTitle(){
            return this.title;
        }

        public int getImageID(){
            return this.imageID;
        }
    }
}
