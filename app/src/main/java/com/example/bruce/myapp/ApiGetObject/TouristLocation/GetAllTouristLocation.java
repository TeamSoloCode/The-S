package com.example.bruce.myapp.ApiGetObject.TouristLocation;

import com.example.bruce.myapp.Data.TouristLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 6/5/2018.
 */

public class GetAllTouristLocation {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<TouristLocation> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetAllTouristLocation() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<TouristLocation> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
