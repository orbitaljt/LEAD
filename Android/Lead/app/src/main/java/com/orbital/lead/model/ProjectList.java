package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

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

    public void setList(ProjectList list){
        if(list == null){
            this.initArrayList();
            return;
        }
        this.list = new ArrayList<Project>(list.getList());
        /*
        if(this.list == null){
            this.initArrayList();
        }

        this.list.addAll(list.getList());
        */
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

    public void resetList() {
        Iterator<Project> iter = this.list.iterator();
        while (iter.hasNext()) {
            Project p = iter.next();
            p.setIsSelected(false);
        }
    }

    public void updateProject(Project updatedProject) {
        ListIterator<Project> iter = this.list.listIterator();
        while (iter.hasNext()) {
            Project p = iter.next();
            if(p.getProjectID().equals(updatedProject.getProjectID())){
                p.setIsSelected(updatedProject.getIsSelected());
            }
        }

        /*
        System.out.println(" updatedProject.getIsSelected() => " + updatedProject.getIsSelected());
        System.out.println("=============updating========");

        int index = this.list.indexOf(updatedProject);
        this.list.set(index, updatedProject);

        System.out.println("index => " + index);
        System.out.println(" updatedProject.getName() => " + updatedProject.getName());
        System.out.println(" updatedProject.getIsSelected() => " + updatedProject.getIsSelected());
        */
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
