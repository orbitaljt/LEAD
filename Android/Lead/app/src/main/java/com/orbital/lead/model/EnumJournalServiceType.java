package com.orbital.lead.model;

/**
 * Created by joseph on 19/6/2015.
 */
public enum EnumJournalServiceType {
    GET_ALL_JOURNAL ("get_all_journal"),
    UPDATE_SPECIFIC_JOURNAL ("update_specific_journal"),
    DELETE_SPECIFC_JOURNAL ("delete_specific_journal"),
    INSERT_NEW_JOURNAL ("insert_new_journal"),
    GET_NEW_JOURNAL_ALBUM_ID("get_new_journal_album_id");

    private final String ext;
    private EnumJournalServiceType(String s){
        ext = s;
    }

    public static EnumJournalServiceType fromString(String text) {
        if (text != null) {
            for (EnumJournalServiceType b : EnumJournalServiceType.values()) {
                if (text.equalsIgnoreCase(b.ext)) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return ext;
    }

}
