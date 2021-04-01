package com.example.myapplication.filemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.PictureItem;
import com.example.myapplication.R;

import java.util.List;

public class FileListFragment extends Fragment {
    private RecyclerView mFileRecyclerView;
    private FileAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_list,container,
                false);
        mFileRecyclerView = view.findViewById(R.id.picture_recycler_view);
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FileLab fileLab = FileLab.get(getActivity());
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

        }
    }
}
