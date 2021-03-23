package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;



public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mData;
    private ItemClickListener mClickListener;
    MyAdapter(Context context, List<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String wifi = mData.get(position);
        holder.myTextView.setText(wifi);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView myTextView;

        public ViewHolder( View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.wifiCurrent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) mClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    String getItem(int id){
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
