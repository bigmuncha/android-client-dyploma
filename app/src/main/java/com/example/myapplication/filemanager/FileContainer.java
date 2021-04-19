package com.example.myapplication.filemanager;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class FileContainer {
    private static FileContainer sFileContainer;
    private static HashMap<String , FileItem> mFiles;

    private FileContainer(Context context){
        mFiles = new HashMap<>();
    }

    public  static FileContainer get(Context context){
        if(sFileContainer == null){
            sFileContainer = new FileContainer(context);
        }
        return sFileContainer;
    }

    public FileItem getFile(String key){
        return mFiles.get(key);
    }
    public  FileItem setFile(String key, FileItem value){
        return mFiles.put(key,value);
    }

    public FileItem removeFile(String key){
        return mFiles.remove(key);
    }

    public  int size(){
        return mFiles.size();
    }

    public boolean empty(){
        return mFiles.size() == 0 ? true :false;
    }

    public boolean isInclude(String key){
        return mFiles.containsKey(key);
    }

    public static HashMap<String ,FileItem> getFiles() {
        return mFiles;
    }
    
    public void printer(){
        for (String path: mFiles.keySet()
             ) {
            Log.d("MAPA > ", path);
        }
    }

}
