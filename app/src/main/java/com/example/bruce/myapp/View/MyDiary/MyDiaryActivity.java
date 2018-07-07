package com.example.bruce.myapp.View.MyDiary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.DiaryAdapter;
import com.example.bruce.myapp.Data.Diary;
import com.example.bruce.myapp.Presenter.MyDiary.PMyDiary;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MyDiaryActivity extends AppCompatActivity implements DiaryAdapter.RecyclerViewClicklistener,
        View.OnClickListener,
        IViewMyDiary {

    private RecyclerView recyclerViewMyDiaries;
    private DiaryAdapter diaryAdapter;
    private PMyDiary pDiary = new PMyDiary(this);
    private RelativeLayout relativeLayoutNotification, relativeLayoutLoading;
    private Button btnAddNewDiary;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        initialize();

        btnAddNewDiary.setOnClickListener(this);

        pDiary.receivedGetAllMyDiary(user.getUid());
    }

    private void initialize(){
        recyclerViewMyDiaries = findViewById(R.id.recyclerViewMyDiaries);
        relativeLayoutNotification = findViewById(R.id.relLayoutNotification);
        relativeLayoutLoading = findViewById(R.id.relLayoutLoading);
        btnAddNewDiary = findViewById(R.id.btnAddDiary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddDiary:
                pDiary.receivedCreateDiary(user.getUid());
                break;
        }
    }

    @Override
    public void onClickItemRecyclerView(View view, Diary diary) {

    }

    @Override
    public void getAllMyDiary(int resultCode, ArrayList<Diary> listMyDiary, String resultMessage) {
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if(listMyDiary.size() != 0){
            relativeLayoutLoading.setVisibility(View.GONE);

            recyclerViewMyDiaries.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            diaryAdapter = new DiaryAdapter(listMyDiary, this);
            recyclerViewMyDiaries.setAdapter(diaryAdapter);
            diaryAdapter.setClickListenerRecyclerView(this);
            diaryAdapter.notifyDataSetChanged();
        }else{
            relativeLayoutNotification.setVisibility(View.VISIBLE);
        }


        Log.i("asdf", listMyDiary.toString());
    }

    @Override
    public void createNewDiary(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();
    }
}
