package com.example.bruce.myapp.Data;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment {
    public String commentID;
    public String userID;
    public int locationID;
    public String userName;
    public String userImage;
    public String comment;
    public String date;
    public   int like = 0;
    public ArrayList<String> commentImages;

    public Comment() {

    }

    public Comment(String commentID, String userID, int locationID, String userName, String userImage, String comment, String date, int likeCount) {
        this.commentID = commentID;
        this.userID = userID;
        this.locationID = locationID;
        this.userName = userName;
        this.userImage = userImage;
        this.comment = comment;
        this.date = date;
        this.like = likeCount;
    }

    public Comment(ArrayList<String> commentImages) {
        this.commentImages = commentImages;
    }

    public Comment(String commentID, String userID, int locationID, String userName, String userImage, String comment, String date, int likeCount, ArrayList<String> commentImages) {
        this.commentID = commentID;
        this.userID = userID;
        this.locationID = locationID;
        this.userName = userName;
        this.userImage = userImage;
        this.comment = comment;
        this.date = date;
        this.like = likeCount;
        this.commentImages = commentImages;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikeCount() {
        return like;
    }

    public void setLikeCount(int likeCount) {
        this.like = likeCount;
    }
}





