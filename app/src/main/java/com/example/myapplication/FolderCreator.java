package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FolderCreator {
    private static final String TAG = "FolderCreator";
    public static  boolean create(Context context) {

        File folder = new File(Environment.getExternalStorageDirectory() + "/OMAR");
        boolean success = true;
        if(!folder.exists()){
            success=folder.mkdirs();
        }
        Log.d(TAG, String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
        Log.d(TAG,folder.getAbsolutePath());
        if(success){
            Log.d(TAG, "success");
            return  true;
        }else{
            Log.d(TAG,"failure");
            return  false;
        }
    }
    public static File temp(String name){
        File baseDir;

        if (Build.VERSION.SDK_INT < 8) {
            baseDir = Environment.getExternalStorageDirectory();
        } else {
            baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }

        if (baseDir == null)
            return Environment.getExternalStorageDirectory();

        File folder = new File(baseDir, name);

        if (folder.exists()) {

            return folder;
        }
        if (folder.isFile()) {
            folder.delete();
        }
        if (folder.mkdirs()) {
            return folder;
        }
        Log.d(TAG,"NO");
        return Environment.getExternalStorageDirectory();

    }
}
