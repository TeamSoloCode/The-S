package com.example.bruce.myapp.View.Diary;

import com.example.bruce.myapp.Data.CheckPoint;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/1/2018.
 */

public interface IViewDiary {
    void getAllCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage);
    void getAllMySharedCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage);
    void sharedDiary(int resultCode, String resultMessage);
}