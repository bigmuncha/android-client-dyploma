package com.example.myapplication.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.container.SharedViewModel;

import java.io.File;

public class FileManagerActivity extends AppCompatActivity {

    private SharedViewModel viewModel;


    private static final String rootDir = "/storage/emulated/0";
    private static final String EXTRA_FILE_PATH =
            "com.example.myapplication.filemanager.folder_path";
    private static final String EXTRA_COUNT_FILES=
            "com.example.myapplication.filemanager.count_files";

    public static Intent newIntent(Context packageContext, String path, String count){
        Intent intent = new Intent(packageContext, FileManagerActivity.class);
        intent.putExtra(EXTRA_FILE_PATH,path);
        intent.putExtra(EXTRA_COUNT_FILES,count);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        String folderPath = (String) getIntent().getSerializableExtra(EXTRA_FILE_PATH);
        String countFiles = (String)getIntent().getSerializableExtra(EXTRA_COUNT_FILES);

        Log.d("File Activity", folderPath + " S ");
        Log.d("File Activity", countFiles + " S ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        Fragment fragment1 = fragmentManager.findFragmentById(R.id.send_bar_container);



        if(fragment == null){
            if(folderPath == null) {
                fragment = FileListFragment.newInstance(rootDir);
            }else{
                fragment = FileListFragment.newInstance(folderPath);
            }
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

        if(fragment1 == null){
            if(countFiles == null){
                fragment1 = FileSendBarFragment.newInstance("0");
            }else{
                fragment1 = FileSendBarFragment.newInstance(countFiles);
            }


            fragmentManager.beginTransaction()
                    .add(R.id.send_bar_container,fragment1)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        String curDir = FileListFragment.getCurrentDir();
        Log.d("Back button", curDir);
        if(curDir.equals(rootDir)){
            Intent intent = new Intent(FileManagerActivity.this,MainActivity.class);
            Log.d("Back button", "here");
            startActivity(intent);
        }else{
            FileListFragment.startNewFragment(this,FileItem.extractParentFolder(curDir));
            Log.d("Back button", "HEHREEE");
        }

    }

    public void SendFiles(View view){

    }


}
