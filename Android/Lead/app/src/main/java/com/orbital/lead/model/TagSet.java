package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by joseph on 12/7/2015.
 */
public class TagSet{

    private HashSet<String> tagSet;

    public TagSet(){
        this.initTagSet();
    }

    public TagSet(HashSet<String> set) {
        this.initTagSet();
        this.getSet().addAll(set);
    }

    public void add(String name) {
        this.getSet().add(name);
    }

    public void add(TagSet addSet) {
        this.add(addSet.getTagList());
    }

    public void add(TagList list) {
        if(this.getSet() == null) {
            this.initTagSet();
        }

        if(list != null){
            for(Tag tag : list.getList()) {
                this.getSet().add(tag.getName());
            }
        }
    }

    public void add(Tag tag){
        if(this.getSet() == null) {
            this.initTagSet();
        }

        this.setValue(tag.getName());
    }

    public HashSet<String> getSet() {
        return this.tagSet;
    }

    public TagList getTagList() {
        TagList list = new TagList();

        Iterator iter = this.getSet().iterator();
        while(iter.hasNext()){
            Tag tag = new Tag(iter.next().toString());
            list.addTag(tag);
        }

        return list;
    }

    public List<String> getList() {
        return new ArrayList<>(this.getSet());
    }

    /*
    public boolean isTagNameExist(String name) {
        return this.getSet().contains(name);
    }
    */

    public void setValue(String name){
        if(this.getSet() == null) {
            this.initTagSet();
        }
        this.getSet().add(name);
    }

    public void removeTag(Tag tag) {
        this.getSet().remove(tag.getName());
    }


    private void initTagSet() {
        this.tagSet = new HashSet<String>();
    }




}
