package com.example.bruce.myapp.ApiGetObject;

import com.example.bruce.myapp.Data.TeamMember;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by BRUCE on 6/24/2018.
 */

public class GetAllTeamMember {
    @SerializedName("resultCode")
    @Expose
    private int resultCode;

    @SerializedName("resultData")
    @Expose
    private ArrayList<TeamMember> resultData;

    @SerializedName("resultMessage")
    @Expose
    private Object resultMessage;

    public GetAllTeamMember() {
    }

    public int getResultCode() {
        return resultCode;
    }

    public ArrayList<TeamMember> getResultData() {
        return resultData;
    }

    public Object getResultMessage() {
        return resultMessage;
    }
}
