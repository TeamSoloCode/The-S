package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.InvitersInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by BRUCE on 6/21/2018.
 */

public class GetInvitersInfo {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private List<InvitersInfo> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetInvitersInfo() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public List<InvitersInfo> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
