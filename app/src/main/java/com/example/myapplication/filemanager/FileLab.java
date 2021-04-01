package com.example.myapplication.filemanager;

import android.content.Context;

import com.example.myapplication.PictureItem;
import com.example.myapplication.PictureLab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLab {
    private static FileLab sFileLab;
    private static final String rootPath = "/storage/emulated/0";
    private static final String TAG = "FILE_LAB";
    private List<FileItem> mFiles;

    public FileLab(Context context) throws IOException {
        mFiles = new ArrayList<>();
        File folder = new File(rootPath);
        File []listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
                FileItem item = new FileItem(listOfFiles[i].getPath());
                mFiles.add(item);
        }
    }

    public static FileLab get(Context context) {
        if(sFileLab == null){
            try {
                sFileLab = new FileLab(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sFileLab;
    }

    public List<FileItem> getFiles(){
        return mFiles;
    }



}
