package com.example.bruce.myapp.View.Comment_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.Comment_Adapter;
import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.Presenter.CommentFragment.PCommentFragment;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment_Fragment extends android.support.v4.app.Fragment implements IViewComment_Fragment {
    private ViewGroup mView;
    private RecyclerView recyclerView_Comment;
    private Comment_Adapter adaper;
    private String location_ID;
    private ArrayList<TouristLocation> tls;
    private ArrayList<Comment> comments;
    private HashMap<String, Boolean> hsmComment;

    private FirebaseUser user;
    private DatabaseReference mData;
    private ChildEventListener childEventListenerNewCommentAdded;

    private PCommentFragment pCommentFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pCommentFragment = new PCommentFragment(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //lấy dữ liệu từ intent
        tls = getActivity().getIntent().getParcelableArrayListExtra("tourist_location");
        location_ID = tls.get(0).getLocationId();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.comment_fragment,container,false);

        //goi api lay het comment cua location, mode get all
        //gui null ở param cuối cùng để n biết là mình muốn lấy hết comment
        pCommentFragment.receivedGetDataComment(location_ID, user.getUid(), null);
        return mView;
    }


    @Override
    public void getAllCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage) {
        if(resultCode != 1){
            Toasty.error(getContext(), resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        else if(comments == null){
            return;
        }

        this.comments = comments;
        hsmComment = new HashMap<>();

        //hashmap này để kiểm tra đã hiện thị comment này hay chưa
        for (Comment comment : this.comments){
            hsmComment.put(comment.getCommentId(), true);
        }

        recyclerView_Comment = mView.findViewById(R.id.recyclerView_Comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adaper = new Comment_Adapter(this.comments ,getActivity());
        recyclerView_Comment.setLayoutManager(layoutManager);
        recyclerView_Comment.setAdapter(adaper);

        //set up get comment realtime thong qua api
        getCommentLocationRealtime();
    }

    /**
     * Them comment moi vao list khi có user add vào trên database
     * @param resultCode
     * @param comments
     * @param resultMessage
     */
    @Override
    public void getNewCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage) {
        this.comments.add(comments.get(0));

        //hashmap này để kiểm tra đã hiện thị comment này hay chưa
        hsmComment.put(comments.get(0).getCommentId(), true);
        adaper.notifyDataSetChanged();
    }

    private void getCommentLocationRealtime(){
        mData = FirebaseDatabase.getInstance().getReference("Comment").child(location_ID);
        childEventListenerNewCommentAdded = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //goi api lay het comment cua location, mode get new
                //gui key của commemt mới add để kéo n về
                if(dataSnapshot.getKey() != null){
                    //ko cần hiện mấy cái đã hiệu rồi, vì childAdded lấy hết data ở realtime lúc mới chạy
                    if(hsmComment.get(dataSnapshot.getKey().toString()) == null){
                        pCommentFragment.receivedGetDataComment(location_ID, user.getUid(), dataSnapshot.getKey().toString());
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
        };

        mData.addChildEventListener(childEventListenerNewCommentAdded);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(childEventListenerNewCommentAdded != null){
            mData.removeEventListener(childEventListenerNewCommentAdded);
        }
    }
}