

package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> implements Comment_Image_Adapter.RecyclerViewClicklistener {

    private ArrayList<Comment> comments = new ArrayList<>();
    private Context context;
    private RecyclerViewClicklistener itemClickListener;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private SimpleDateFormat sdf;
    private Calendar calendar;

    boolean check;

    public Comment_Adapter(ArrayList<Comment> comment_contructors, Context context) {
        this.comments = comment_contructors;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.item_comment,parent,false);

        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        calendar = Calendar.getInstance();

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       //xử lý sự kiện click likebutton
        Comment cC = comments.get(position);

        //ClickButtonLike(holder.btnLike,holder.txtLikeNumber,comments.get(position).getCommentId(),context,cC);

        holder.txtComment.setText(cC.getComment());

        calendar.setTimeInMillis(cC.getAddedDate());
        holder.txtDateOfComment.setText(sdf.format(calendar.getTime()));


        holder.txtUsername.setText(cC.getUserName());
        holder.txtLikeNumber.setText(String.valueOf(cC.getLike()));


        Picasso.with(context).load(cC.getUserImage()).into(holder.userImage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);

        holder.recyclerView_Comment_Image.setLayoutManager(layoutManager);
        holder.adapter_comment_image = new Comment_Image_Adapter(context,cC.getImage());
        holder.adapter_comment_image.setClickListener(this);
        holder.recyclerView_Comment_Image.setAdapter(holder.adapter_comment_image);
        holder.adapter_comment_image.notifyDataSetChanged();

        //enable hay disale button like
        setViewLiked(holder,position);
    }
    private void setViewLiked(ViewHolder holder,int position){
        if(comments.get(position).isLiked()){
            holder.btnLike.setLiked(true);
        }
        else {
            holder.btnLike.setLiked(false);
        }
    }

    public void ClickButtonLike(LikeButton btnLikethumb, final TextView likeNumber, final String idComment, final Context context , final Comment cC){

        // fix update Button like
        //Xử lý button like
        btnLikethumb.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    @Override
    public void itemClickCommentImage(View view, int position) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtUsername,txtComment,txtDateOfComment,txtLikeNumber;
        RecyclerView recyclerView_Comment_Image;
        Comment_Image_Adapter adapter_comment_image;
        ImageView userImage;
        LikeButton btnLike;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtUsername = itemView.findViewById(R.id.userName);
            txtComment = itemView.findViewById(R.id.Comment);
            txtDateOfComment =  itemView.findViewById(R.id.dateofComment);
            btnLike = itemView.findViewById(R.id.thumb_button);
            txtLikeNumber = itemView.findViewById(R.id.likeNumber);
            userImage= itemView.findViewById(R.id.userImage);
            recyclerView_Comment_Image = itemView.findViewById(R.id.recyclerView_Comment_Image);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickComment(v,getPosition());
            }

        }
    }
    public void setClickListener(RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface RecyclerViewClicklistener {
         void itemClickComment(View view, int position);
    }
}
