package com.example.bruce.myapp.Presenter.BigMap;

import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.Model.MBigMap;
import com.example.bruce.myapp.View.BigMap.IViewBigMap;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/21/2017.
 */

public class PBigMap implements IBigMap {

    private MBigMap modelBigMap = new MBigMap(this);
    private IViewBigMap callbackToView;

    public PBigMap(IViewBigMap callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedGetAllTouristLocation(){
        modelBigMap.getAllTouristLocation();
    }

    @Override
    public ArrayList<TouristLocation> getAllTouristLocation(ArrayList<TouristLocation> listTouristLocation, int resultCode, String resultMessage) {
        return callbackToView.getAllTouristLocation(listTouristLocation, resultCode, resultMessage);
    }

    public void receivedSendPing(String userId, String teamId, int pingCode){
        modelBigMap.sendPing(userId, teamId, pingCode);
    }

    @Override
    public void sendPing(int resultCode, String resultMessage) {

    }
}
