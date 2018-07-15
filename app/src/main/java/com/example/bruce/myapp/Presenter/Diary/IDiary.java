package com.example.bruce.myapp.Presenter.Diary;

import com.example.bruce.myapp.Data.CheckPoint;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/1/2018.
 */

public interface IDiary {
    void getAllCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage);
}
