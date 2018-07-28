package com.example.bruce.myapp.View.BigMap;

import com.example.bruce.myapp.Data.TouristLocation;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/21/2017.
 */

public interface IViewBigMap {
    ArrayList<TouristLocation> getAllTouristLocation(
            ArrayList<TouristLocation> listTouristLocation,
            int resultCode,
            String resultMessage);
    void sendPing(int resultCode, String resultMessage);
}
