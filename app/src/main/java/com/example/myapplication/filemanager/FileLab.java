package com.example.myapplication.filemanager;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLab {
    private static FileLab sFileLab;
    private static final String rootPath = "/storage/emulated/0";
    private static final String TAG = "FILE_LAB";
    private List<FileItem> mFiles;

    private FileLab(Context context,String newDir) throws IOException {
        mFiles = new ArrayList<>();
        File folder = new File( newDir);
        File []listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
                FileItem item = new FileItem(listOfFiles[i].getPath());
                mFiles.add(item);
        }
    }

    public static FileLab getReloadFileLab(Context context, String path) throws IOException {
        sFileLab = new FileLab(context,path);
        return sFileLab;
    }

    public static FileLab get(Context context,String path) {
       // if(sFileLab == null){
            try {
                sFileLab = new FileLab(context,path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
        return sFileLab;
    }

    public List<FileItem> getFiles(){
        return mFiles;
    }



}
