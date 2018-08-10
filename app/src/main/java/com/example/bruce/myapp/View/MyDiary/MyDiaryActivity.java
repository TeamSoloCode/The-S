package com.example.bruce.myapp.View.MyDiary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.DiaryAdapter;
import com.example.bruce.myapp.Data.Diary;
import com.example.bruce.myapp.Presenter.MyDiary.PMyDiary;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Diary.DiaryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class MyDiaryActivity extends AppCompatActivity implements DiaryAdapter.RecyclerViewClicklistener,
        View.OnClickListener,
        IViewMyDiary {

    private RecyclerView recyclerViewMyDiaries;
    private DiaryAdapter diaryAdapter;
    private PMyDiary pDiary = new PMyDiary(this);
    private RelativeLayout relativeLayoutNotification, relativeLayoutLoading;
    private Button btnAddNewDiary, btnMainImage, btnAddnewDiary;
    private SpotsDialog loadDaTaDialog;
    private ArrayList<Diary> listDiary;
    private String shareMode;
    private EditText edtDiarysname;
    private Dialog info;


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        initialize();

        btnAddNewDiary.setOnClickListener(this);

        loadDaTaDialog = new SpotsDialog(this);
        loadDaTaDialog.show();

        //get all user's diary
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
                info = setUpDialogCreateNewDiary();
                info.show();
                break;
            case R.id.btnAddDiaryImage:
                //up hình xong lấy hình trên firebase xuông chổ này;
                //đưa zo biên mainImage xong
                break;

            case R.id.btnAddNewDiary:
                if(edtDiarysname.length() != 0){
                    if(info != null && info.isShowing()){
                        info.dismiss();
                    }
                    //để hình zo đây
                    String mainImage = "";
                    loadDaTaDialog.show();
                    pDiary.receivedCreateDiary(user.getUid(), edtDiarysname.getText().toString(), mainImage);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnMnAddNewDiary:
                info = setUpDialogCreateNewDiary();
                info.show();
                break;
            case R.id.btnMnAllSharedDiary:
                loadDaTaDialog.show();
                shareMode = "share";
                pDiary.receivedGetMySharedDiary(user.getUid());
                break;
            case R.id.btnMnAllMyDiary:
                loadDaTaDialog.show();
                shareMode = null;
                //get all user's diary
                pDiary.receivedGetAllMyDiary(user.getUid());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Dialog setUpDialogCreateNewDiary(){
        final Dialog info = new Dialog(this);
        //info.requestWindowFeature(Window.FEATURE_NO_TITLE); -- bo title cua dialog
        info.setContentView(R.layout.dialog_diary_profile);
        btnMainImage = info.findViewById(R.id.btnAddDiaryImage);
        btnAddnewDiary = info.findViewById(R.id.btnAddNewDiary);
        edtDiarysname = info.findViewById(R.id.edtDiarysName);

        btnAddnewDiary.setOnClickListener(this);
        btnMainImage.setOnClickListener(this);
        return info;
    }

    @Override
    public void onClickItemRecyclerView(View view, Diary diary) {
        Intent intent = new Intent(this,DiaryActivity.class);
        intent.putExtra("diaryId", diary.getId());
        intent.putExtra("shareMode", shareMode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLongClickItemRecyclerView(View view, Diary diary) {

    }

    @Override
    public void getAllMyDiary(int resultCode, ArrayList<Diary> listMyDiary, String resultMessage) {
        this.listDiary = listMyDiary;

        loadDaTaDialog.dismiss();
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if(listMyDiary == null){
            return;
        }

        if(this.listDiary.size() != 0){
            relativeLayoutLoading.setVisibility(View.GONE);

            recyclerViewMyDiaries.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            diaryAdapter = new DiaryAdapter(this.listDiary, this);
            recyclerViewMyDiaries.setAdapter(diaryAdapter);
            diaryAdapter.setClickListenerRecyclerView(this);
            diaryAdapter.notifyDataSetChanged();

        }else{
            relativeLayoutNotification.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getAllMySharedDiary(int resultCode, ArrayList<Diary> listDiary, String resultMessage) {
        loadDaTaDialog.dismiss();
        this.listDiary = listDiary;
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if(listDiary == null){
            return;
        }

        if(listDiary.size() == 0) {
            Toasty.info(this, "You don't have any shared diary", Toast.LENGTH_SHORT).show();
            return;
        }

        if(this.listDiary.size() != 0) {
            recyclerViewMyDiaries.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            diaryAdapter = new DiaryAdapter(this.listDiary, this);
            recyclerViewMyDiaries.setAdapter(diaryAdapter);
            diaryAdapter.setClickListenerRecyclerView(this);
            diaryAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void createNewDiary(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();

        //get all user's diary
        pDiary.receivedGetAllMyDiary(user.getUid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diary_menu_item, menu);
        if(shareMode != null){
            if(shareMode.equals("share")){
                menu.findItem(0).setVisible(false);
            }
        }
        return true;
    }


}
