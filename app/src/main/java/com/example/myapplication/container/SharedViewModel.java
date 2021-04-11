package com.example.myapplication.container;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> countOfFiles = new MutableLiveData<Integer>();

    public void setCountOfFiles(Integer integer){
        countOfFiles.setValue(integer);
    }
    public LiveData<Integer> getCountOfFiles(){
        return countOfFiles;
    }
}
