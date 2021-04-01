package com.example.myapplication.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class FileListFragment extends Fragment {
    private RecyclerView mFileRecyclerView;
    private FileAdapter mAdapter;
    private static final String rootDir = "/storage/emulated/0";
    private String mCurrentDir;
    private List<FileItem> mFileStack;
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
        mFileStack = new ArrayList<>();
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

    private class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

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
            mFileItem = fileItem;
            mNameTextView.setText(mFileItem.getName());
            mDateTextView.setText(mFileItem.getDate());
            if(mFileItem.isIsFolder()){
                mFileIconImageView.setImageResource(R.drawable.ic_folder);
            }else{
                /*change this*/
                mFileIconImageView.setImageResource(R.drawable.ic_file);
            }
        }

        @Override
        public void onClick(View v) {
            if (mFileItem.isIsFolder()) {
                Intent intent = FileListActivity.newIntent(getActivity(), mFileItem.getPath());
                Log.d("CLICK", mFileItem.getPath());
                startActivity(intent);
            }else{
                Toast.makeText(getContext(), "Its file", Toast.LENGTH_SHORT).show();
                mSelectFileCheckBox.setChecked(true);
                mFileStack.add(mFileItem);
            }
        }
        public List<FileItem> getStackFiles(){
            return mFileStack;
        }
        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
