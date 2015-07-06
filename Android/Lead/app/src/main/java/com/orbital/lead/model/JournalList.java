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

    public int size(){
        return this.mJournalArray.size();
    }

    public ArrayList<Journal> getList(){
        if(this.mJournalArray == null)
            initArray();
        return this.mJournalArray;
    }
    private void initArray(){
        this.mJournalArray = new ArrayList<Journal>();
    }

}
