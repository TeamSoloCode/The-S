package com.example.bruce.myapp.View.MyDiary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bruce.myapp.Adapter.DiaryAdapter;
import com.example.bruce.myapp.Data.Diary;
import com.example.bruce.myapp.R;

public class MyDiaryActivity extends AppCompatActivity implements DiaryAdapter.RecyclerViewClicklistener {

    private RecyclerView recyclerViewMyDiaries;
    private DiaryAdapter diaryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        initialize();


    }

    private void initialize(){
        recyclerViewMyDiaries = findViewById(R.id.recyclerViewMyDiaries);
    }

    @Override
    public void onClickItemRecyclerView(View view, Diary diary) {

    }
}
