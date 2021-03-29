package com.example.myapplication.imagelist;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterForImages  extends RecyclerView<RecyclerView.ViewHolder> {

    private ArrayList<CreateList> galleryList;
    private Context context;

    public AdapterForImages(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
