package com.orbital.lead.model;

/**
 * Created by joseph on 2/7/2015.
 */
public class Tag {

    private String name;
    private boolean isChecked;

    public Tag(String name, boolean checked){
        this.name = name;
        this.isChecked = checked;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIsChecked(boolean checked){
        this.isChecked = checked;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIsChecked(){
        return this.isChecked;
    }

}
