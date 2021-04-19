package com.example.myapplication.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.FileTransfer;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.container.SharedViewModel;
import com.example.myapplication.wifi.DisplayWiFiListActivity;
import com.example.myapplication.wifi.WifiConnect;

import java.io.File;
import java.io.IOException;

public class FileSendBarFragment extends Fragment {
    private static final String ARG_SEND_BAR = "count_files";
    Button transferButton;
    Button fileCountButton;
    String currentCountFiles;
    private SharedViewModel viewModel;

    public static FileSendBarFragment newInstance(String count){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEND_BAR,count);

        FileSendBarFragment fragment = new FileSendBarFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String count = (String) getArguments().getSerializable(ARG_SEND_BAR);
        currentCountFiles = count;
        Log.d("BAR" , String.valueOf(currentCountFiles));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getCountOfFiles().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                fileCountButton.setText(integer.toString());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sender,container,
                false);
        transferButton = view.findViewById(R.id.transfer_button);
        fileCountButton = view.findViewById(R.id.file_amount);
        fileCountButton.setText(currentCountFiles);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SendFiles(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public void SendFiles(View view) throws IOException {
        FileContainer fileContainer = FileContainer.get(getContext());
        fileContainer.printer();
        WifiConnect wifiConnect = new WifiConnect(getContext());
        String ip = wifiConnect.getRouterIp();
        if(fileContainer.empty()){
            Toast.makeText(getContext(), "Select files", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =
                    new Intent(getActivity(), DisplayWiFiListActivity.class);
            startActivity(intent);
        }
    }
}
