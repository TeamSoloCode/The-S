package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.Diary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/4/2018.
 */

public class GetAllMyDiaries {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<Diary> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetAllMyDiaries() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<Diary> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
