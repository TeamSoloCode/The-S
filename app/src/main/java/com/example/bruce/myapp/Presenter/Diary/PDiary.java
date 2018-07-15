package com.example.bruce.myapp.Presenter.Diary;

import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.Model.MDiary;
import com.example.bruce.myapp.View.Diary.IViewDiary;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/1/2018.
 */

public class PDiary implements IDiary {
    MDiary model = new MDiary(this);
    IViewDiary callbackToView;

    public PDiary(IViewDiary callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedGetAllCheckPoint(String userId, String diaryId){
        model.getAllCheckPoint(userId, diaryId);
    }

    @Override
    public void getAllCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage) {
        callbackToView.getAllCheckPoint(resultCode, listCheckPoint, resultMessage);
    }
}
