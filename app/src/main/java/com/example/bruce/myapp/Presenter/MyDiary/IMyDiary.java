package com.example.bruce.myapp.Presenter.MyDiary;

import com.example.bruce.myapp.Data.Diary;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/5/2018.
 */

public interface IMyDiary {
    void getAllMyDiary(int resultCode, ArrayList<Diary> listTeamMember, String resultMessage);
    void createNewDiary(int resultCode, String resultMessage);
}
