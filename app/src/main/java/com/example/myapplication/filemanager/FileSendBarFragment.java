package com.example.myapplication.filemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.FileTransfer;
import com.example.myapplication.R;

public class FileSendBarFragment extends Fragment {
    private static final String ARG_SEND_BAR = "send_bar";
    Button transferButton;
    Button fileCountButton;

    public static FileSendBarFragment newInstance(){
        Bundle args = new Bundle();
        //args.putSerializable(ARG_SEND_BAR,0);
        FileSendBarFragment fragment = new FileSendBarFragment();
        //fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // int count = (Integer) getArguments().getSerializable(ARG_SEND_BAR);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sender,container,
                false);
        transferButton = view.findViewById(R.id.transfer_button);
        fileCountButton = view.findViewById(R.id.file_amount);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFiles(view);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View getCountButton(){
        return fileCountButton;
    }

    public void SendFiles(View view){
        FileContainer fileContainer = FileContainer.get(getContext());
        fileContainer.printer();
        FileTransfer.SendMultipleFiles("192.168.43.133",FileTransfer.getTransferPort(), FileContainer.getFiles());
    }
}
