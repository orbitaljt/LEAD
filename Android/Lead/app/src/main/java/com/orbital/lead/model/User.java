package com.orbital.lead.model;

/**
 * Created by joseph on 14/6/2015.
 */
public class User {

    private String _facebookID;
    private String _userID;
    private String _profilePicID;
    private String _journalListID;
    private String _experienceListID;
    private String _firstName;
    private String _lastName;
    private String _middleName;
    private String _birthday;
    private String _address;
    private String _city;
    private String _state;
    private String _country;
    private String _countryCode;
    private String _username;
    private String _password;
    private String _email;
    private String _createdDate;
    private String _createdTime;
    private String _lastLoginDate;
    private String _lastLoginTime;
    private int _age;

    public User(String fID, String ID, String ppID, String jID, String eID, String fName, String mName, String lName,
                String birth, String addr, String city, String state, String country, String countryCode, String username,
                String pw, String email, String createdDate, String createdTime, String lastLoginDate, String lastLoginTime, int age){

        this._facebookID = fID;
        this._userID = ID;
        this._profilePicID = ppID;
        this._journalListID = jID;
        this._experienceListID = eID;
        this._firstName = fName;
        this._middleName = mName;
        this._lastName = lName;
        this._birthday = birth;
        this._address = addr;
        this._city = city;
        this._state = state;
        this._country = country;
        this._countryCode = countryCode;
        this._username = username;
        this._password = pw;
        this._email = email;
        this._createdDate = createdDate;
        this._createdTime = createdTime;
        this._lastLoginDate = lastLoginDate;
        this._lastLoginTime = lastLoginTime;
        this._age = age;
    }

    // Get
    public String getFacebookID(){
        return this._facebookID;
    }

    public String getUserID(){
        return this._userID;
    }

    public String getProfilePictureID(){
        return this._profilePicID;
    }

    public String getJournalListID(){
        return this._journalListID;
    }

    public String getExperienceListID(){
        return this._experienceListID;
    }

    public String getFirstName(){
        return this._firstName;
    }

    public String getMiddleName(){
        return this._middleName;
    }

    public String getLastName(){
        return this._lastName;
    }

    /**
     * Get birthday format yyyy-mm-dd
     * **/
    public String getBirthday(){
        return this._birthday;
    }

    public String getAddress(){
        return this._address;
    }

    public String getCity(){
        return this._city;
    }

    public String getState(){
        return this._state;
    }

    public String getCountry(){
        return this._country;
    }

    public String getCountryCode(){
        return this._countryCode;
    }

    public String getUsername(){
        return this._username;
    }

    public String getPassword(){
        return this._password;
    }

    public String getEmail(){
        return this._email;
    }

    /**
     * Get created date format yyyy-mm-dd
     * **/
    public String getCreatedDate(){
        return this._createdDate;
    }

    /**
     * Get created time format HH:mm:ss
     * **/
    public String getCreatedTime(){
        return this._createdTime;
    }

    /**
     * Get last login date format yyyy-mm-dd
     * **/
    public String getLastLoginDate(){
        return this._lastLoginDate;
    }

    /**
     * Get last login time format HH:mm:ss
     * **/
    public String getLastLoginTime(){
        return this._lastLoginTime;
    }

    public int getAge(){
        return this._age;
    }

    // Set
    public void setFacebookID(String val){
        this._facebookID = val;
    }

    public void setUserID(String val){
        this._userID = val;
    }

    public void setProfilePicID(String val){
        this._profilePicID = val;
    }

    public void setJournalListID(String val){
        this._journalListID = val;
    }

    public void setExperienceListID(String val){
        this._experienceListID = val;
    }

    public void setFirstName(String val){
        this._firstName = val;
    }

    public void setMiddleName(String val){
        this._middleName = val;
    }

    public void setLastName(String val){
        this._lastName = val;
    }

    /**
     * Set a new birthday format yyyy-mm-dd
     * **/
    public void setBirthday(String val){
        this._birthday = val;
    }

    public void setAddress(String val){
        this._address = val;
    }

    public void setCity(String val){
        this._city = val;
    }

    public void setCountry(String val){
        this._country = val;
    }

    public void setCountryCode(String val){
        this._countryCode = val;
    }

    public void setUsername(String val){
        this._username = val;
    }

    public void setPassword(String val){
        this._password = val;
    }

    public void setEmail(String val){
        this._email = val;
    }

    /**
     * Set a new created date format yyyy-mm-dd
     * **/
    public void setCreatedDate(String val){
        this._createdDate = val;
    }

    /**
     * Set a new created time format HH:mm:ss
     * **/
    public void setCreatedTime(String val){
        this._createdTime = val;
    }

    /**
     * Set a new last login date format yyyy-mm-dd
     * **/
    public void setLastLoginDate(String val){
        this._lastLoginDate = val;
    }

    /**
     * Set a new last login time format HH:mm:ss
     * **/
    public void setLastLoginTime(String val){
        this._lastLoginTime = val;
    }

    public void setAge(int val){
        this._age = val;
    }


}
