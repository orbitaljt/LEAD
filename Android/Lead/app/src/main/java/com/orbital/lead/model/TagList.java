package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joseph on 30/6/2015.
 */
public class TagList {
    private ArrayList<Tag> list;

    public TagList(){
        this.list = new ArrayList<Tag>();
    }

    public void getTag(Tag newTag){
        if(this.list != null){
            this.list.add(newTag);
        }
    }

    public void removeTag(String name){
        Iterator<Tag> iter = this.list.iterator();
        while (iter.hasNext()) {
            Tag j = iter.next();
            if(j.getName().equals(name)){
                iter.remove();
            }
        }
    }

    public int size(){
        return this.list.size();
    }

    public ArrayList<Tag> getList(){
        return this.list;
    }

    public void setList(ArrayList<Tag> newList){
        this.list = new ArrayList<Tag>(newList);
        newList = null;
    }






}
