package com.example.myapplication.filemanager;

import androidx.fragment.app.Fragment;

import com.example.myapplication.SingleFragmentActivity;

public class FileListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FileListFragment();
    }
}
