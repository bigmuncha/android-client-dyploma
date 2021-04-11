package com.example.myapplication.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Stack;

public class FileListFragment extends Fragment {
    private RecyclerView mFileRecyclerView;
    private FileAdapter mAdapter;
    private static final String rootDir = "/storage/emulated/0";
    private String mCurrentDir;
    Button button;
    private     Stack<FileItem> mFileStack;
    private static final String ARG_FOLDER_PATH = "folder_path";



    public static FileListFragment newInstance(String path){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOLDER_PATH,path);

        FileListFragment fragment = new FileListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        mFileStack = new Stack<>();
        super.onCreate(savedInstanceState);

        String dir = (String) getArguments().getSerializable(ARG_FOLDER_PATH);
        mCurrentDir = dir;
        Log.d("Create", mCurrentDir);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_list,container,
                false);
        mFileRecyclerView = view.findViewById(R.id.picture_recycler_view);
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUI(mCurrentDir);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(mCurrentDir);
    }

    private void updateUI(String path) {
        FileLab fileLab = FileLab.get(getActivity(), mCurrentDir);
        List<FileItem> files = fileLab.getFiles();

        if(mAdapter == null){
            mAdapter = new FileAdapter(files);
            mFileRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<FileHolder>{

        private List<FileItem> mFiles;
        public FileAdapter(List<FileItem> files){
            mFiles = files;
        }

        @NonNull
        @Override
        public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FileHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FileHolder holder, int position) {
            FileItem fileItem = mFiles.get(position);
            holder.bind(fileItem);
        }

        @Override
        public int getItemCount() {
            return mFiles.size();
        }
    }

    private class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mFileIconImageView;
        private TextView mNameTextView;
        private TextView mDateTextView;
        private CheckBox mSelectFileCheckBox;
        private FileItem mFileItem;

        public FileHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_file,parent,false));
            mNameTextView = itemView.findViewById(R.id.file_name);
            mFileIconImageView = itemView.findViewById(R.id.file_icon);
            mDateTextView = itemView.findViewById(R.id.file_date);
            mSelectFileCheckBox = itemView.findViewById(R.id.file_checkBox);
            itemView.setOnClickListener(this);
        }

        public void bind(FileItem fileItem){
            FileContainer fileContainer = FileContainer.get(getContext());

            mFileItem = fileItem;
            mNameTextView.setText(mFileItem.getName());
            mDateTextView.setText(mFileItem.getDate());
            mSelectFileCheckBox.setChecked(false);



            mSelectFileCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                      //  button.setText(String.valueOf(countFile + 1));
                        fileContainer.setFile(mFileItem.getPath(),mFileItem);
                    }else {
                        //button.setText(String.valueOf(countFile - 1));
                        fileContainer.removeFile(mFileItem.getPath());
                    }
                }
            });

            mFileIconImageView.setImageResource(makeIcon(mFileItem));

            if(fileContainer.isInclude(mFileItem.getPath())){
                if(!mSelectFileCheckBox.isChecked()) {
                    mSelectFileCheckBox.setChecked(true);
                }
                Log.d("CHECKER", mFileItem.getPath());
            }
        }

        private int makeIcon(FileItem mFileItem) {
            if(mFileItem.isIsFolder()){
                return R.drawable.ic_folder;
            }else if(mFileItem.getExtension().equals("jpg")|| mFileItem.getExtension().equals( "png")){
                return R.drawable.ic_image;
            }else if(mFileItem.getExtension().equals("mp4")){
                return R.drawable.ic_video;
            }else if(mFileItem.getExtension().equals("mp3")){
                return R.drawable.ic_audio;
            }else{
                return R.drawable.ic_file;
            }
        }

        @Override
        public void onClick(View v) {
            if (mFileItem.isIsFolder()) {
                Intent intent = FileListActivity.newIntent(getActivity(), mFileItem.getPath());
                Log.d("CLICK", mFileItem.getPath());
                startActivity(intent);
            }else{
                Toast.makeText(getContext(), "Its file " + mFileItem.getExtension(), Toast.LENGTH_SHORT).show();
              /*  Log.d("File","FILE" + mFileItem.getName());
                mSelectFileCheckBox.setChecked(true);
                mFileStack.push(mFileItem);

                try {
                    FileTransfer.SendOneFile("192.168.43.133",FileTransfer.getTransferPort(),mFileStack.pop());
                } catch (IOException e) {
                    e.printStackTrace();
                }
               */
            }
        }
        public List<FileItem> getStackFiles(){
            return mFileStack;
        }

    }
}
