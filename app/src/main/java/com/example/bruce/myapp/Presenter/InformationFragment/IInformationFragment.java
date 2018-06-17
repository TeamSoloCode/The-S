package com.example.bruce.myapp.Presenter.InformationFragment;

import com.example.bruce.myapp.Data.TouristLocationDetail;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/25/2017.
 */

public interface IInformationFragment {
    void getTouristLocationById(
            ArrayList<TouristLocationDetail> detail,
            int resultCode,
            String resultMessage);
}
