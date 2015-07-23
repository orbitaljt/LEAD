package com.orbital.lead.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joseph on 22/6/2015.
 */
public class JournalList {

    private ArrayList<Journal> mJournalArray;

    public JournalList(){
        this.initArray();
    }

    public void addJournal(Journal journal){
        this.getList().add(journal);
    }

    public void removeJournal(String journalID){
        Iterator<Journal> iter = this.getList().iterator();
        while (iter.hasNext()) {
            Journal j = iter.next();
            if(j.getJournalID().equals(journalID)){
                iter.remove();
            }
        }
    }

    public Journal get(int position){
        return getList().get(position);
    }

    public Journal get(String journalID) {
        Iterator<Journal> iter = this.getList().iterator();
        while (iter.hasNext()) {
            Journal j = iter.next();
            if(j.getJournalID().equals(journalID)){
                return j;
            }
        }
        return null;
    }

    public int size(){
        return this.mJournalArray.size();
    }

    public ArrayList<Journal> getList(){
        if(this.mJournalArray == null)
            initArray();
        return this.mJournalArray;
    }

    public void updateJournal(Journal updatedJournal) {
        Iterator<Journal> iter = this.getList().iterator();
        while (iter.hasNext()) {
            Journal j = iter.next();
            if(j.getJournalID().equals(updatedJournal.getJournalID())){
                j.setTagList(updatedJournal.getTagList());
                j.setTitle(updatedJournal.getTitle());
                j.setContent(updatedJournal.getContent());
                j.setJournalDate(updatedJournal.getJournalDate());
                j.setAlbum(updatedJournal.getAlbum());
                j.setIsPublished(updatedJournal.getIsPublished());
                j.setCountryCode(updatedJournal.getCountryCode());
                j.setLastModifiedDate(updatedJournal.getLastModifiedDate());
                j.setLastModifiedTime(updatedJournal.getLastModifiedTime());
                j.setPictureCoverID(updatedJournal.getPictureCoverID());
                j.setProject(updatedJournal.getProject());
            }
        }
    }

    public TagSet getAllTags() {
        return this.retrieveAllJournalTags();
    }

    private void initArray(){
        this.mJournalArray = new ArrayList<Journal>();
    }

    private TagSet retrieveAllJournalTags() {
        TagSet set = new TagSet();
        Iterator<Journal> iter = this.getList().iterator();
        while (iter.hasNext()) {
            Journal j = iter.next();
            set.add(j.getTagList());
        }
        return set;
    }
}
