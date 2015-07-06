package com.orbital.lead.model;

/**
 * Created by joseph on 2/7/2015.
 */
public class Project {

    private String id;
    private String projectListID;
    private String title;

    private boolean isSelected;

    public Project(String title, boolean selected) {
        this.title = title;
        this.isSelected = selected;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsSelected(boolean selected) {
        this.isSelected = selected;
    }


}
