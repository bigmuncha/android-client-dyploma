package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PictureListFragment extends Fragment {

    private RecyclerView mPictureRecyclerView;
    private PictureAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_list,container,
                false);
        mPictureRecyclerView = view.findViewById(R.id.picture_recycler_view);
        mPictureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        PictureLab pictureLab = PictureLab.get(getActivity());
        pictureLab.printPhotos();
        List<PictureItem> pictures = pictureLab.getPictures();

        if(mAdapter == null){
            mAdapter = new PictureAdapter(pictures);
            mPictureRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class PictureAdapter extends RecyclerView.Adapter<PictureHolder>{

        private List<PictureItem> mPictures;
        public PictureAdapter(List<PictureItem> pictures){
            mPictures = pictures;

        }

        @NonNull
        @Override
        public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PictureHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PictureHolder holder, int position) {
            PictureItem picture = mPictures.get(position);
            holder.bind(picture);
        }

        @Override
        public int getItemCount() {
            return mPictures.size();
        }
    }






    private class PictureHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mDateTextView;
        private ImageView mPictureImageView;
        private PictureItem mPicture;

        public PictureHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_picture,parent,false));

            mDateTextView = itemView.findViewById(R.id.picture_data);
            mPictureImageView = itemView.findViewById(R.id.picture_picture);
            itemView.setOnClickListener(this);
        }

        public void bind(PictureItem pictureItem){
            mPicture = pictureItem;
            //меняй
            //mPictureImageView.setImageDrawable(Drawable.createFromPath(mPicture.getPath()));
            mDateTextView.setText(mPicture.getPath());
        }

        @Override
        public void onClick(View v) {
            //меняй
        }
    }
}
