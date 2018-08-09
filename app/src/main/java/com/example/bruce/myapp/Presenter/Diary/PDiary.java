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

    public void receivedGetAllMySharedCheckPoint(String userId, String diaryId){
        model.getAllMySharedCheckPoint(userId, diaryId);
    }
    @Override
    public void getAllMySharedCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage) {
        callbackToView.getAllMySharedCheckPoint(resultCode, listCheckPoint, resultMessage);
    }

    public void receivedSharedDiary(String userId, String diaryId, String userSharedEmail){
        model.shareMyDiary(userId, diaryId, userSharedEmail);
    }
    @Override
    public void sharedDiary(int resultCode, String resultMessage) {
        callbackToView.sharedDiary(resultCode,resultMessage);
    }
}
