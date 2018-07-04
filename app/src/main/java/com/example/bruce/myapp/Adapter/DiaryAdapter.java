package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bruce.myapp.Data.Diary;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/4/2018.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private ArrayList<Diary> diaries;
    Context context;
    RecyclerViewClicklistener clicklistener;

    public DiaryAdapter(ArrayList<Diary> diaries, Context context) {
        this.diaries = diaries;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.item_diary,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Diary diary = diaries.get(position);

        holder.txtDiarysName.setText(diary.getName());
        holder.txtDiscription.setText(diary.getDescription());
        Picasso.with(context).load(diary.getImage()).into(holder.imgMainImage, new Callback() {
            @Override
            public void onSuccess() {

                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgMainImage;
        TextView txtDiarysName;
        TextView txtDiscription;
        ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgMainImage = itemView.findViewById(R.id.imgDiaryMainImage);
            txtDiarysName = itemView.findViewById(R.id.txtDiarysName);
            txtDiscription = itemView.findViewById(R.id.txtDiaryDiscription);
            progressBar= itemView.findViewById(R.id.progress_bar_load_image_comment);
        }

        @Override
        public void onClick(View v) {
            if(clicklistener != null){
                clicklistener.onClickItemRecyclerView(v,diaries.get(getPosition()));
            }
        }
    }
    public void setClickListenerRecyclerView(RecyclerViewClicklistener clickListener){
        this.clicklistener = clickListener;
    }

    public interface RecyclerViewClicklistener{

        void onClickItemRecyclerView(View view, Diary diary);
    }
}
