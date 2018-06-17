package com.example.bruce.myapp.View.Comment_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.myapp.Adapter.Comment_Adapter;
import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.Presenter.CommentFragment.PCommentFragment;
import com.example.bruce.myapp.R;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment_Fragment extends android.support.v4.app.Fragment implements IViewComment_Fragment {
    ViewGroup mView;

    RecyclerView recyclerView_Comment;
    Comment_Adapter adaper;
    ArrayList<Comment> comments = new ArrayList<>();
    String location_ID;
    PCommentFragment pCommentFragment = new PCommentFragment(this);
    ArrayList<TouristLocation> tls;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = (ViewGroup) inflater.inflate(R.layout.comment_fragment,container,false);
//        recyclerView_Comment = mView.findViewById(R.id.recyclerView_Comment);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        adaper = new Comment_Adapter(comments,getActivity());
//        recyclerView_Comment.setLayoutManager(layoutManager);
//        recyclerView_Comment.setAdapter(adaper);
//        pCommentFragment.receivedGetDataComment(location_ID,adaper,comments);
        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lấy dữ liệu từ intent
        tls = getActivity().getIntent().getParcelableArrayListExtra("tourist_location");
        location_ID = tls.get(0).getLocationId();

    }

    private void setUpRecyclerView(Comment_Adapter adaper,RecyclerView recyclerView_Comment,ArrayList<Comment> comments){

    }


}