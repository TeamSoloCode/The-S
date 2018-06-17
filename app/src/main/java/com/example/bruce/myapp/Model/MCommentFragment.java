package com.example.bruce.myapp.Model;

import android.util.Log;

import com.example.bruce.myapp.Adapter.Comment_Adapter;
import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.CommentFragment.ICommentFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/29/2017.
 */

public class MCommentFragment {
    DatabaseReference mData;

    ICommentFragment callback;

    public MCommentFragment(ICommentFragment callback) {
        this.callback = callback;
    }

    public void handleGetDataComment(int location_ID, Comment_Adapter adapter,ArrayList<Comment> comments){

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Comment comment_contructor = dataSnapshot.getValue(Comment.class);
                comment_contructor.commentImages = new ArrayList<>();
                if(location_ID == comment_contructor.locationID) {

                    mData.child("Img_Comment").child(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            comment_contructor.commentImages.add(dataSnapshot.getValue().toString());
                            adapter.notifyDataSetChanged();
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

                    mData.child("User").child(comment_contructor.userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserProfile constructer_userProfile=dataSnapshot.getValue(UserProfile.class);
                            comment_contructor.userImage=constructer_userProfile.Image;
                            comments.add(comment_contructor);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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

}
