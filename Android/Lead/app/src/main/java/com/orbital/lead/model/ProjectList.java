package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joseph on 30/6/2015.
 */
public class ProjectList {
    private ArrayList<Project> list;

    public ProjectList(){
        this.list = new ArrayList<Project>();
    }

    public void getProject(Project newProject){
        if(this.list != null){
            this.list.add(newProject);
        }
    }

    public void removeProject(String name){
        Iterator<Project> iter = this.list.iterator();
        while (iter.hasNext()) {
            Project j = iter.next();
            if(j.getTitle().equals(name)){
                iter.remove();
            }
        }
    }

    public int size(){
        return this.list.size();
    }

    public ArrayList<Project> getList(){
        return this.list;
    }

    public void setList(ArrayList<Project> newList){
        this.list = new ArrayList<Project>(newList);
        newList = null;
    }






}
