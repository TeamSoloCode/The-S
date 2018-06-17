package com.example.bruce.myapp.Presenter.BigMap;

import com.example.bruce.myapp.Data.TouristLocation;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/21/2017.
 */

public interface IBigMap {
    ArrayList<TouristLocation> getAllTouristLocation(
            ArrayList<TouristLocation> listTouristLocation,
            int resultCode, String resultMessage);
}
