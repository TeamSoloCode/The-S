package com.example.bruce.myapp.ApiCommonResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by BRUCE on 6/14/2018.
 */

public class CommonResponse {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
