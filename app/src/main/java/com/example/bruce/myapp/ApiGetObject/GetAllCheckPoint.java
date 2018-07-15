package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.CheckPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class GetAllCheckPoint {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<CheckPoint> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetAllCheckPoint() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<CheckPoint> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
