package com.example.bruce.myapp.Presenter.DiaryCheckPoint;

import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.Model.MDiaryCheckPoint;
import com.example.bruce.myapp.View.DiaryCheckPoint.IViewDiaryCheckPoint;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class PDiaryCheckPoint implements IDiaryCheckPoint {

    MDiaryCheckPoint model = new MDiaryCheckPoint(this);
    IViewDiaryCheckPoint callbackToView;

    public PDiaryCheckPoint(IViewDiaryCheckPoint callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedAddNewCheckPoint(String userId, String diaryId, CheckPoint checkPoint){
        model.addDiaryCheckPoint(userId,diaryId,checkPoint);
    }

    @Override
    public void addDiaryCheckPoint(int resultCode, String resultMessage) {
        callbackToView.addDiaryCheckPoint(resultCode, resultMessage);
    }

    public void receivedUpdateCheckPoint(String userId, String diaryId, CheckPoint checkPoint){
        model.updateDiaryCheckPoint(userId,diaryId,checkPoint);
    }

    @Override
    public void updateDiaryCheckPoint(int resultCode, String resultMessage) {
        callbackToView.updateDiaryCheckPoint(resultCode, resultMessage);
    }
}
