package com.example.bruce.myapp.Presenter.CommentFragment;

import com.example.bruce.myapp.Adapter.Comment_Adapter;
import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Model.MCommentFragment;
import com.example.bruce.myapp.View.Comment_Fragment.IViewComment_Fragment;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/29/2017.
 */

public class PCommentFragment implements ICommentFragment {

    MCommentFragment model = new MCommentFragment(this);
    IViewComment_Fragment callbackToView;

    public PCommentFragment(IViewComment_Fragment callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedGetDataComment(int location_ID, Comment_Adapter adapter, ArrayList<Comment> comments){

        model.handleGetDataComment(location_ID,adapter,comments);
    }


}
