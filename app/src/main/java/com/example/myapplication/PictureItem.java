package com.example.myapplication;

import android.graphics.Picture;
import android.net.Uri;

import java.net.URI;
import java.util.Date;

public class PictureItem {
    private String mPath;
    private Date mDate;
    private String mName;

    public PictureItem(String path){
        mPath = path;
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getPath() {
        return mPath;
    }

}
