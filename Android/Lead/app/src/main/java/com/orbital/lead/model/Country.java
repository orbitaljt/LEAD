package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Terence on 7/19/2015.
 */
public class Country implements Parcelable {

    private String country = "";
    private String countryCode = "";
    private String region = "";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.countryCode);
        dest.writeString(this.region);
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>(){
        public Country createFromParcel(Parcel pc){
            return new Country(pc);
        }
        public Country[] newArray(int size){
            return new Country[size];
        }
    };

    public Country() {
    }

    public Country(String country, String region, String countryCode) {

        this.country = country;
        this.region = region;
        this.countryCode = countryCode;
    }

    public Country(Country country){
        this.country = country.getCountry();
        this.countryCode = country.getCountryCode();
        this.region = country.getRegion();
    }

    public String getRegion() {
        return this.region;
    }

    public String getCountry(){
        return this.country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }



    private Country(Parcel pc){
        this.country =  pc.readString();
        this.countryCode =  pc.readString();
        this.region = pc.readString();
    }

}
