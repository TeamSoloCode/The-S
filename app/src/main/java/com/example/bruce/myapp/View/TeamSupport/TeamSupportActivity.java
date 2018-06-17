package com.example.bruce.myapp.View.TeamSupport;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.bruce.myapp.R;
import com.google.firebase.database.FirebaseDatabase;

public class TeamSupportActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextView txtNameNeedHelp;
    String key;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_support);
        txtNameNeedHelp=findViewById(R.id.txtNameNeedHelp);
        mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Intent intent=getIntent();
        String nameNeedHelp= intent.getStringExtra("NameNeedHelp");
        key= intent.getStringExtra("key");
        userId= intent.getStringExtra("userId");
        txtNameNeedHelp.setText(nameNeedHelp);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        FirebaseDatabase.getInstance().getReference("Help").child(key).child("Team").child(userId).removeValue();
    }
}
