

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

import com.example.bruce.myapp.R;
import com.example.bruce.myapp.Data.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> implements Comment_Image_Adapter.RecyclerViewClicklistener {

    ArrayList<Comment> comment_contructors = new ArrayList<>();
    Context context;
    RecyclerViewClicklistener itemClickListener;
    DatabaseReference mDataLike= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    boolean check;
    public Comment_Adapter(ArrayList<Comment> comment_contructors, Context context) {
        this.comment_contructors = comment_contructors;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.item_comment,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       //xử lý sự kiện click likebutton

        Comment cC = comment_contructors.get(position);

        ClickButtonLike(holder.btnLike,holder.txtLikeNumber,comment_contructors.get(position).getCommentID(),context,cC);

        holder.txtComment.setText(cC.comment);
        holder.txtDateOfComment.setText(cC.date);
        holder.txtUsername.setText(cC.userName);
        holder.txtLikeNumber.setText(String.valueOf(cC.getLikeCount()));


        Picasso.with(context).load(cC.userImage).into(holder.userImage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);

        holder.recyclerView_Comment_Image.setLayoutManager(layoutManager);
        holder.adapter_comment_image = new Comment_Image_Adapter(context,cC.commentImages);
        holder.adapter_comment_image.setClickListener(this);
        holder.recyclerView_Comment_Image.setAdapter(holder.adapter_comment_image);
        holder.adapter_comment_image.notifyDataSetChanged();
        setViewLiked(holder,position);




    }
    public void setViewLiked(ViewHolder holder,int position){
        mDataLike.child("Likes").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(comment_contructors.get(position).getCommentID().equals(dataSnapshot.getKey())){
                    check=true;
                    holder.txtUsername.setText(comment_contructors.get(position).getUserName());
                    holder.txtComment.setText(comment_contructors.get(position).getComment());
                    if(check==true){
                        holder.btnLike.setLiked(true);
                    }
                    else{
                        holder.btnLike.setLiked(false);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ClickButtonLike(LikeButton btnLikethumb, final TextView likeNumber, final String idComment, final Context context , final Comment cC){

        // fix update Button like
        //Xử lý button like
        btnLikethumb.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mDataLike.child("Likes").child(mAuth.getCurrentUser().getUid()).child(idComment).push().setValue(idComment);


                mDataLike.child("Comments").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comment comment_contructor = dataSnapshot.getValue(Comment.class);
                        if(comment_contructor.date.equals(cC.date)&&comment_contructor.userID.equals(cC.userID)){

                            mDataLike.child("Comments").child(dataSnapshot.getKey()).child("like").setValue(comment_contructor.getLikeCount() + 1);
                            likeNumber.setText(String.valueOf(comment_contructor.getLikeCount() + 1));
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                mDataLike.child("Likes").child(mAuth.getCurrentUser().getUid()).child(idComment).removeValue();


                mDataLike.child("Comments").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comment comment_contructor = dataSnapshot.getValue(Comment.class);
                        if(comment_contructor.date.equals(cC.date)&&comment_contructor.userID.equals(cC.userID)){
                            likeNumber.setText(String.valueOf(comment_contructor.getLikeCount()-1));
                            mDataLike.child("Comments").child(dataSnapshot.getKey()).child("like").setValue(comment_contructor.getLikeCount() - 1);


                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return comment_contructors.size();
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
