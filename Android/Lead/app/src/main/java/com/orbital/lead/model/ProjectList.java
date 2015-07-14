package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joseph on 30/6/2015.
 */
public class ProjectList {
    private ArrayList<Project> list;

    public ProjectList(){
        this.initArrayList();
    }

    public void addProject(Project project) {
        if(this.list == null){
            this.initArrayList();
        }
        this.list.add(project);
    }

    public void addList(ProjectList list){
        if(this.list == null){
            this.initArrayList();
        }

        this.list.addAll(list.getList());
    }

    public void removeProject(String name){
        Iterator<Project> iter = this.list.iterator();
        while (iter.hasNext()) {
            Project j = iter.next();
            if(j.getName().equals(name)){
                iter.remove();
            }
        }
    }

    public Project findProject(String projectID) {
        Iterator<Project> iter = this.list.iterator();
        while (iter.hasNext()) {
            Project p = iter.next();
            if(p.getProjectID().equals(projectID)){
                return p;
            }
        }
        return null;
    }


    public int size(){
        return this.list.size();
    }


    public ArrayList<Project> getList(){
        return this.list;
    }

    public void setList(ArrayList<Project> newList){
        this.list = new ArrayList<Project>(newList);
    }

    private void initArrayList() {
        this.list = new ArrayList<Project>();
    }






}
