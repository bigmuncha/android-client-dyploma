package com.example.myapplication.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.SingleFragmentActivity;

public class FileListActivity extends AppCompatActivity {


    private static final String rootDir = "/storage/emulated/0";

    private static final String EXTRA_FILE_PATH =
            "com.example.myapplication.filemanager.folder_path";
    public static Intent newIntent(Context packageContext, String path){
        Intent intent = new Intent(packageContext,FileListActivity.class);
        intent.putExtra(EXTRA_FILE_PATH,path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String folderPath = (String) getIntent().getSerializableExtra(EXTRA_FILE_PATH);
        Log.d("Activity", folderPath + "  ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

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

    }

}
