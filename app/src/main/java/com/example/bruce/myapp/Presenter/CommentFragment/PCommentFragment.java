package com.example.bruce.myapp.Presenter.CommentFragment;

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

    public void receivedGetDataComment(String locationId, String userGetCommentId, String commentId){
        model.handleGetAllCommentOfLocation(locationId, userGetCommentId, commentId);
    }


    @Override
    public void getAllCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage) {
        callbackToView.getAllCommentOfLocation(resultCode, comments, resultMessage);
    }

    @Override
    public void getNewCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage) {
        callbackToView.getNewCommentOfLocation(resultCode, comments, resultMessage);
    }
}
