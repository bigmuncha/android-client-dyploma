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
import java.util.Date;

public class FileItem {
    private static final String TAG = "FileItem";
    private String mPath;
    private String mName;
    private String mDate;
    private boolean mIsFolder;
    private  String mExtension;



    public FileItem(String path) {

        mPath = path;
        mName = extractName(path);
        mIsFolder = isFolder(path);
        mExtension = extractExtension(mName);
        mDate = String.valueOf(new Date());
       // mDate = Files.readAttributes(Paths.get(path),BasicFileAttributes.class).creationTime().toString();
    }

    public static String extractExtension(String name) {
        int i = name.lastIndexOf('.');
        if(i>0){
            return name.substring(i+1);
        }
        return "";
    }

    public static boolean isFolder(String path){
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


    public String getExtension() {
        return mExtension;
    }

    public String getDate() {
        return mDate;
    }
}
