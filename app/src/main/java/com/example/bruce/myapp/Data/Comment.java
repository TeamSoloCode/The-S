package com.example.bruce.myapp.Data;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment {
    private String commentId;
    private String userName;
    private String userImage;
    private String comment;
    private long addedDate;
    private int like;
    private boolean liked;
    private ArrayList<String> image;

    public Comment() {

    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getComment() {
        return comment;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public int getLike() {
        return like;
    }

    public boolean isLiked() {
        return liked;
    }

    public ArrayList<String> getImage() {
        return image;
    }
}





