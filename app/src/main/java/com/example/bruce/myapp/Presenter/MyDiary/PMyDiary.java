package com.example.bruce.myapp.Presenter.MyDiary;

import com.example.bruce.myapp.Data.Diary;
import com.example.bruce.myapp.Model.MMyDiary;
import com.example.bruce.myapp.View.MyDiary.IViewMyDiary;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/5/2018.
 */

public class PMyDiary implements IMyDiary {
    private MMyDiary mDiary = new MMyDiary(this);
    private IViewMyDiary callbackToView;

    public PMyDiary(IViewMyDiary callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedGetAllMyDiary(String userId){
        mDiary.getAllMyDiary(userId);
    }

    @Override
    public void getAllMyDiary(int resultCode, ArrayList<Diary> listMyDiary, String resultMessage) {
        callbackToView.getAllMyDiary(resultCode, listMyDiary, resultMessage);
    }

    public void receivedCreateDiary(String userId, String diaryName, String diaryImage){
        mDiary.createDiary(userId,diaryName,diaryImage);
    }

    @Override
    public void createNewDiary(int resultCode, String resultMessage) {
        callbackToView.createNewDiary(resultCode, resultMessage);
    }

    public void receivedGetMySharedDiary(String userIdShared){
        mDiary.getAllMyDiaryShared(userIdShared);
    }

    @Override
    public void getAllMySharedDiary(int resultCode, ArrayList<Diary> listDiary, String resultMessage) {
        callbackToView.getAllMySharedDiary(resultCode, listDiary, resultMessage);
    }

}
