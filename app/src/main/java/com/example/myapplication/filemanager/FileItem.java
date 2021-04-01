package com.example.myapplication.filemanager;

import android.os.FileUtils;
import android.util.Log;

import java.io.File;

public class FileItem {
    private static final String TAG = "FileItem";
    private String mPath;
    private String mName;
    private boolean mIsFolder;
    private  String mExtension;

    public FileItem(String path){
        mPath = path;
        mName = extractName(path);
        mIsFolder = recognizeFolder(path);
        mExtension = extractExtension(mName);
    }

    public static String extractExtension(String name) {
        int i = name.lastIndexOf('.');
        if(i>0){
            return name.substring(i+1);
        }
        return "";
    }

    public static boolean recognizeFolder(String path) {
        File f = new File(path);
        return f.isDirectory();
    }

    public static String extractName(String path) {
        int i = path.lastIndexOf('/');
        if(i>0){
            return path.substring(i+1);
        }
        return null;
    }

    public String getPath() {
        return mPath;
    }

    public String getName() {
        return mName;
    }

    public boolean isIsFolder() {
        return mIsFolder;
    }

    public void setIsFolder(boolean mIsFolder) {
        this.mIsFolder = mIsFolder;
    }

    public String getExtension() {
        return mExtension;
    }
}
