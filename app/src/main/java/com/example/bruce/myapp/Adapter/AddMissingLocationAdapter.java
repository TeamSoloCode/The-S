package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bruce.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddMissingLocationAdapter extends RecyclerView.Adapter<AddMissingLocationAdapter.ViewHolder>{
    Context context;
    ArrayList<String> listImageAdd;
    RecyclerViewClicklistener itemClickListener;

    public AddMissingLocationAdapter(Context context, ArrayList<String> listImageComment) {
        this.context = context;
        this.listImageAdd = listImageComment;
    }
    public AddMissingLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.adapter_add_missing_location,parent,false);

        return  new AddMissingLocationAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String image_link = listImageAdd.get(position);
        //.fit giảm độ phân giải ảnh tương đương kích thước ảnh
        Picasso.with(context).load(image_link).fit().into(holder.imgAdd);
    }

    @Override
    public int getItemCount() {
        return listImageAdd.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgAdd,imgRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAdd = itemView.findViewById(R.id.image_add_missing_location);
            imgRemove= itemView.findViewById(R.id.imgRemove);
            imgRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickRemoveImg(v,getPosition());
            }
        }

    }
    public void setClickListener(RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface RecyclerViewClicklistener {
        void itemClickRemoveImg(View view, int position);
    }
}
