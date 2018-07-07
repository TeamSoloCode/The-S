package com.example.bruce.myapp.ApiPostObject;

import java.util.ArrayList;

public class PostComment {
    private String comment;
    private ArrayList<String> listImage;
    public PostComment() {
    }

    public ArrayList<String> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<String> listImage) {
        this.listImage = listImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
