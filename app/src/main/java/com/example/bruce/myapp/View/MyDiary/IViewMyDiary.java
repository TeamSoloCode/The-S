package com.example.bruce.myapp.View.MyDiary;

import com.example.bruce.myapp.Data.Diary;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/5/2018.
 */

public interface IViewMyDiary {
    void getAllMyDiary(int resultCode, ArrayList<Diary> listTeamMember, String resultMessage);
    void getAllMySharedDiary(int resultCode, ArrayList<Diary> listDiary, String resultMessage);
    void createNewDiary(int resultCode, String resultMessage);
}
