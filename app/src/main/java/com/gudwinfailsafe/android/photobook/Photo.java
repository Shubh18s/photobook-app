package com.gudwinfailsafe.android.photobook;

import android.preference.MultiSelectListPreference;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shubhdeep on 13-07-2016.
 */
public class Photo {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mDescription;


    public Photo(){
        this(UUID.randomUUID());
    }

    public Photo(UUID id){
        mId=id;
        mDate=new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPhotoFilename(){
        return "IMG_"+getId().toString()+".jpg";
    }

}
