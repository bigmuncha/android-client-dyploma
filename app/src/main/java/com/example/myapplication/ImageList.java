package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(Drawable.createFromPath("/storage/emulated/0/DCIM/Camera/OmarPhoto.jpg"));
    }
}