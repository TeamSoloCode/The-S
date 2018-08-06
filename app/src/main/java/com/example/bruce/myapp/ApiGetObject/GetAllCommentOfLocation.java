package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/6/2018.
 */

public class GetAllCommentOfLocation {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<Comment> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetAllCommentOfLocation() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<Comment> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
