package com.example.bruce.myapp.Presenter.CommentFragment;

import com.example.bruce.myapp.Data.Comment;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/29/2017.
 */

public interface ICommentFragment {
    void getAllCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage);
    void getNewCommentOfLocation(int resultCode, ArrayList<Comment> comments, String resultMessage);
}
