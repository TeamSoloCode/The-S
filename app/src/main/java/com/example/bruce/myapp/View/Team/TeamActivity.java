package com.example.bruce.myapp.View.Team;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Phongchat;
import com.example.bruce.myapp.Presenter.Team.PTeam;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity implements IViewTeam,TeamAdapter.RecyclerViewClicklistener{
    RecyclerView recyclerViewTeam;
    PTeam pTeam=new PTeam(this);
    TeamAdapter teamAdapter;
    Button btnInvite;
    EditText edtInvite;
    ArrayList<UserProfile> listUser;
    Button btnchonRoom;
    LinearLayout linear;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        initialize();
        listUser=new ArrayList<>();
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        teamAdapter = new TeamAdapter(listUser, this);
        recyclerViewTeam.setAdapter(teamAdapter);
        teamAdapter.setClickListener(this);

        //Hiển thị Danh sách User lên recyclerView
        //pTeam.receivedAddListUser(teamAdapter,listUser);

        //Get invitation realtime
        FirebaseDatabase.getInstance().getReference("Invitation").child(user.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        pTeam.receivedGetInviterinformation(user.getUid());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        SharedPreferences sharedPref = this.getSharedPreferences("my_data", MODE_PRIVATE);
        String teamId = sharedPref.getString("teamId","");

        if(!teamId.equals("")){
            btnInvite.setVisibility(View.VISIBLE);
            edtInvite.setVisibility(View.VISIBLE);
            linear.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }

        btnInvite.setOnClickListener(v -> {
            String emailInvited = edtInvite.getText().toString();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //call api invite members in model
            pTeam.receivedInviteMember(userId, emailInvited, this);
        });

        btnchonRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamActivity.this, Phongchat.class);
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                intent.putExtra("userid",user.getUid());
                startActivity(intent);

            }
        });

    }
    private void initialize() {
        recyclerViewTeam=findViewById(R.id.recyclerViewTeam);
        btnInvite = findViewById(R.id.btnInvite);
        edtInvite=findViewById(R.id.edtInvite);
        linear=findViewById(R.id.linear);
        btnchonRoom=findViewById(R.id.btnchonphong);
    }

    private void diaLogInvite(InvitersInfo invitersInfo, Context context) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(invitersInfo.getInvitersName()+ " mời bạn vào nhóm").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setTitle("Thông báo");
        alertDialog.show();
    }

    @Override
    public void getInvitersInfo(int resultCode, List<InvitersInfo> invitersInfo, String resultMessage) {
        Log.i("asdf", invitersInfo.toString());
    }

    @Override
    public void acceptInvitation(int resultCode, String resultMessage) {

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), HistoryAndHobbyActivity.class));
        super.onBackPressed();
    }

    @Override
    public void itemClickMember(View view, int position) {
        UserProfile userProfile = listUser.get(position);
        FirebaseDatabase.getInstance().getReference("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("Email").getValue().toString().equals(userProfile.Email))
                {
                    listUser.remove(position);
                    teamAdapter.notifyDataSetChanged();
                    FirebaseDatabase.getInstance().getReference("TeamUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("member")
                            .child(dataSnapshot.getKey()).removeValue();
                    FirebaseDatabase.getInstance().getReference("CheckTeam").child(dataSnapshot.getKey()).removeValue();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, userProfile.Email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inviteMember(int resultCode, String resultMessage) {
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createTeam(int resultCode, String resultMessage) {

    }

    @Override
    public void hasTeam(int resultCode, String resultMessage) {

    }
}
