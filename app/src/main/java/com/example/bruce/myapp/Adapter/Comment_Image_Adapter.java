package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.bruce.myapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 10/31/2017.
 */

public class Comment_Image_Adapter extends RecyclerView.Adapter<Comment_Image_Adapter.ViewHolder> {
    Context context;
    ArrayList<String> listImageComment;
    RecyclerViewClicklistener itemClickListener;

    public Comment_Image_Adapter(Context context, ArrayList<String> listImageComment) {
        this.context = context;
        this.listImageComment = listImageComment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.item_comment_image, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String image_link = listImageComment.get(position);
        Picasso.with(context).load(image_link).into(holder.imageView_Comment, new Callback() {
            @Override
            public void onSuccess() {

                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        holder.btnDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listImageComment.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listImageComment.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_Comment;
        ImageView btnDelImg;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView_Comment = itemView.findViewById(R.id.image_Comment);
            progressBar = itemView.findViewById(R.id.progress_bar_load_image_comment);
            btnDelImg = itemView.findViewById(R.id.btndelimg);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.itemClickCommentImage(v, getPosition());
            }
        }
    }

    public void setClickListener(RecyclerViewClicklistener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface RecyclerViewClicklistener {
        public void itemClickCommentImage(View view, int position);
    }
}
