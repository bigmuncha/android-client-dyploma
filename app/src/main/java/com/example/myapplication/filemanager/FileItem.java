package com.example.myapplication.filemanager;

import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileItem {
    private static final String TAG = "FileItem";
    private String mPath;
    private String mName;
    private String mDate;
    private boolean mIsFolder;
    private  String mExtension;
    private long mSize;
    private String mParentFolder;

    public  String createRequest(){
        return mName + "/" + mExtension;
    }

    public FileItem(String path) {
        File f = new File(path);
        mSize = f.length();
        mPath = path;
        mName = extractName(path);
        mIsFolder = f.isDirectory();
        mExtension = extractExtension(mName);
        mDate = recognizeDate(f);
        mParentFolder = extractParentFolder(path);
       // mDate = Files.readAttributes(Paths.get(path),BasicFileAttributes.class).creationTime().toString();
        f=null;
    }

    public static String extractExtension(String name) {
        int i = name.lastIndexOf('.');
        if(i>0){
            return name.substring(i+1);
        }
        return "";
    }

    public static String recognizeDate(File file){
        Date lastModDate = new Date( file.lastModified());
        return String.valueOf(lastModDate);
    }


    public static String extractParentFolder(String path){
        int i = path.lastIndexOf('/');
        if(i>0){
            return path.substring(0,i);
        }
        return null;
    }

    public static String extractName(String path) {
        int i = path.lastIndexOf('/');
        if(i>0){
            return path.substring(i+1);
        }
        return null;
    }
    public long getSize(){
        return mSize;
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


    public String getExtension() {
        return mExtension;
    }

    public String getDate() {
        return mDate;
    }

    public String getParentFolder() {
        return mParentFolder;
    }
}
