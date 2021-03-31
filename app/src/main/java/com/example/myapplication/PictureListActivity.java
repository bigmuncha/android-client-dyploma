package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class PictureListActivity extends  SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new PictureListFragment();
    }
}
