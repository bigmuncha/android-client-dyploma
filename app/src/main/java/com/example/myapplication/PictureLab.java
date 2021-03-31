package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureLab {
    private static PictureLab sPictureLab;
    private static String path = "/storage/emulated/0/DCIM/Camera";
    private static final String  TAG ="PICTURE_LAB";
    private List<PictureItem> mPictures;

    public static PictureLab get(Context context){
        if(sPictureLab == null){
            sPictureLab = new PictureLab(context);
        }
        return sPictureLab;
    }

    public void printPhotos(){
        for (PictureItem picture: mPictures) {
            Log.d(TAG,picture.getPath());
        }
    }

    private PictureLab(Context context) {
        mPictures = new ArrayList<>();
        File folder = new File(path);
        File []listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile()){
                PictureItem item = new PictureItem(listOfFiles[i].getPath());
                mPictures.add(item);
            }
        }
    }

    public List<PictureItem> getPictures(){
        return mPictures;
    }

    public PictureItem getPicture(String path){
        for (PictureItem picture: mPictures){
            if(picture.getPath().equals(path)){
                return picture;
            }
        }
        return null;
    }


}
