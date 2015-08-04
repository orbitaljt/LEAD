package com.orbital.lead.model;

/**
 * Created by Terence on 7/19/2015.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;


public class CountryList {
    private ArrayList<Country> list;

    public CountryList(){
        this.initArrayList();
    }

    public void addCountry(Country country) {
        if(this.list == null){
            this.initArrayList();
        }
        this.list.add(country);
    }

    public void setList(CountryList list){
        this.list = new ArrayList<Country>(list.getList());
        /*
        if(this.list == null){
            this.initArrayList();
        }

        this.list.addAll(list.getList());
        */
    }

    /*
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
    \

    public void resetList() {
        Iterator<Country> iter = this.list.iterator();
        while (iter.hasNext()) {
            Country p = iter.next();
            //p.setIsSelected(false);
        }
    }

    public void updateCountry(Country updatedCountry) {

        ListIterator<Country> iter = this.list.listIterator();
        while (iter.hasNext()) {
            Country p = iter.next();
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

    }
    */


    public int size(){
        return this.list.size();
    }


    public ArrayList<Country> getList(){
        return this.list;
    }

    public void setList(ArrayList<Country> newList){
        this.list = new ArrayList<Country>(newList);
    }

    private void initArrayList() {
        this.list = new ArrayList<Country>();
    }






}
