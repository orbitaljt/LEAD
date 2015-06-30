package com.orbital.lead.model;

import com.orbital.lead.Parser.ParserFacebook;

/**
 * Created by joseph on 29/6/2015.
 */
public class FacebookUserObject {

    private String rawProfilePictureUrl; //facebook given url
    private String modifiedProfilePictureUrl; //simplified with width & height
    private String profilePictureID;
    private EnumPictureType profilePictureType;
    private String facebookID;
    private String birthday;
    private String email;
    private String locationID;
    private String firstName;
    private String middleName;
    private String lastName;

    public FacebookUserObject(String raw, String facebookID, String birth,
                              String email, String locationID,
                              String firstName, String middleName, String lastName){
        this.rawProfilePictureUrl = raw;
        // parse to get modified + pic ID
        this.profilePictureID = ParserFacebook.getFacebookImageID(this.rawProfilePictureUrl);
        this.profilePictureType = ParserFacebook.getPictureType(this.rawProfilePictureUrl);
        this.facebookID = facebookID;
        this.modifiedProfilePictureUrl = Constant.URL_FACEBOOK_PROFILE_PICTURE.replace(Constant.URL_DUMMY_FACEBOOK_USER_ID, this.facebookID);
        this.birthday = birth;
        this.email = email;
        this.locationID = locationID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getRawProfilePictureUrl(){
        return this.rawProfilePictureUrl;
    }

    public String getModifiedProfilePictureUrl(){
        return this.modifiedProfilePictureUrl;
    }

    public String getProfilePictureID(){
        return this.profilePictureID;
    }

    public EnumPictureType getProfilePictureType() {
        return this.profilePictureType;
    }

    public String getFacebookID(){
        return this.facebookID;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLocationID(){
        return this.locationID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setRawProfilePictureUrl(String value){
        this.rawProfilePictureUrl = value;
    }

    public void setModifiedProfilePictureUrl(String value){
        this.modifiedProfilePictureUrl = value;
    }

    public void setProfilePictureID(String value){
        this.profilePictureID = value;
    }

    public void setFacebookID(String value){
        this.facebookID = value;
    }

    public void setBirthday(String value){
        this.birthday = value;
    }

    public void setEmail(String value){
        this.email = value;
    }

    public void setLocationID(String value){
        this.locationID = value;
    }

    public void setFirstName(String value){
        this.firstName = value;
    }

    public void setMiddleName(String value){
        this.middleName = value;
    }

    public void setLastName(String value){
        this.lastName = value;
    }



}
