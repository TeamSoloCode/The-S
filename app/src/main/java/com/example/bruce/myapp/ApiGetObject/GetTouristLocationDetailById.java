package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.TouristLocationDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 6/13/2018.
 */

public class GetTouristLocationDetailById {

    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<TouristLocationDetail> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetTouristLocationDetailById() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<TouristLocationDetail> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
