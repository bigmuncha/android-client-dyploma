package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FolderCreator {
    private static final String TAG = "FolderCreator";
    public static  boolean create(Context context,String Name) {

        File folder = new File(Environment.getExternalStorageDirectory() + "/" + Name);
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
}
