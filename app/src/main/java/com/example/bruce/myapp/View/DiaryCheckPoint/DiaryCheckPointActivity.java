package com.example.bruce.myapp.View.DiaryCheckPoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Presenter.DiaryCheckPoint.PDiaryCheckPoint;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class DiaryCheckPointActivity extends AppCompatActivity implements View.OnClickListener,IViewDiaryCheckPoint {

    private Button btnSaveChanges, btnAddImage;
    private EditText edtDiscription;
    private RecyclerView recyclerViewCheckPointImages;

    private FirebaseUser user;

    private PDiaryCheckPoint pDiaryCheckPoint;
    private String diaryId;
    private GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_check_point);
        diaryId = getIntent().getStringExtra("diaryId");
        initialize();
        pDiaryCheckPoint = new PDiaryCheckPoint(this);
        btnSaveChanges.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        gpsTracker = new GPSTracker(this);
    }

    private void initialize(){
        btnAddImage = findViewById(R.id.btnAddCheckPointsPic);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        edtDiscription = findViewById(R.id.edtCheckPointDiscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveChanges:
                CheckPoint checkPoint = new CheckPoint();
                if(edtDiscription.length() > 0){
                    checkPoint.setDescription(edtDiscription.getText().toString());
                }
                if(gpsTracker.canGetLocation()){
                    checkPoint.setLat(gpsTracker.getLatitude());
                    checkPoint.setLog(gpsTracker.getLongtitude());
                }
                else {
                    gpsTracker.showSettingAlert();
                    return;
                }
                checkPoint.setDeletePlag(false);
                //checkPoint.setImages();
                pDiaryCheckPoint.receivedAddNewCheckPoint(
                        user.getUid(),
                        diaryId,
                        checkPoint);
                break;
        }
    }

    @Override
    public void addDiaryCheckPoint(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
