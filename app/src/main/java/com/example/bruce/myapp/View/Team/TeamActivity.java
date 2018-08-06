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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.Data.TeamMember;
import com.example.bruce.myapp.Phongchat;
import com.example.bruce.myapp.Presenter.Team.PTeam;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class TeamActivity extends AppCompatActivity implements IViewTeam,TeamAdapter.RecyclerViewClicklistener{
    RecyclerView recyclerViewTeam;
    PTeam pTeam = new PTeam(this);
    TeamAdapter teamAdapter;
    Button btnInvite;
    EditText edtInvite;
    Button btnchonRoom;
    LinearLayout linear;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String teamId;
    private SpotsDialog loadDaTaDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        initialize();
        loadDaTaDialog = new SpotsDialog(this);
        loadDaTaDialog.show();
        //Hiển thị Danh sách User lên recyclerView
        //pTeam.receivedAddListUser(teamAdapter,listUser);
        //Get invitation realtime
//        FirebaseDatabase.getInstance().getReference("Invitation").child(user.getUid())
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        //pTeam.receivedGetInviterinformation(user.getUid());
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


        SharedPreferences sharedPref = this.getSharedPreferences("my_data", MODE_PRIVATE);
         teamId = sharedPref.getString("teamId","");
        if(!teamId.equals("")){
            //Thức hiện gọi api kiểm tra user có phải lá leader của team hay không
            pTeam.receivedIsLeader(user.getUid(), teamId, this);
            //thực hiện goi api lấy danh sách thành viên trong team về
            pTeam.receivedGetAllTeamMember(user.getUid(), teamId);
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
        recyclerViewTeam = findViewById(R.id.recyclerViewTeam);
        btnInvite = findViewById(R.id.btnInvite);
        edtInvite=findViewById(R.id.edtInvite);
        linear=findViewById(R.id.linear);
        btnchonRoom=findViewById(R.id.btnchonphong);
    }

    /**
     * Hiển thị dialog khi có người mời vào đội
     * @param invitersInfo
     * @param context
     * @return
     */
    private AlertDialog diaLogInvite(InvitersInfo invitersInfo, Context context) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(invitersInfo.getInvitersName()+ " mời bạn vào nhóm").setCancelable(false)
                .setPositiveButton("Check my list invitation", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setTitle("Thông báo");

        return alertDialog;
    }

    @Override
    public void getInvitersInfo(int resultCode, List<InvitersInfo> invitersInfo, String resultMessage) {

    }

    @Override
    public void acceptInvitation(int resultCode, String resultMessage) {

    }

    @Override
    public void isLeader(int resultCode, String resultMessage) {
        if(resultCode == 117) {
            btnInvite.setVisibility(View.VISIBLE);
            edtInvite.setVisibility(View.VISIBLE);
            linear.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
    }

    @Override
    public void getAllTeamMember(int resultCode, ArrayList<TeamMember> listTeamMember, String resultMessage) {
        loadDaTaDialog.dismiss();
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        teamAdapter = new TeamAdapter(listTeamMember, this);
        recyclerViewTeam.setAdapter(teamAdapter);
        teamAdapter.setClickListener(this);
        teamAdapter.notifyDataSetChanged();
    }

    @Override
    public void leaveMyTeam(int resultCode, String resultMessage) {
        loadDaTaDialog.dismiss();
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
        Intent intent = new Intent(TeamActivity.this,HistoryAndHobbyActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), HistoryAndHobbyActivity.class));
        super.onBackPressed();
    }

    @Override
    public void itemClickMember(View view, TeamMember teamMember) {
        Toast.makeText(this, teamMember.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inviteMember(int resultCode, String resultMessage) {
        if(resultCode == 002){
            Toasty.success(this, resultMessage, Toast.LENGTH_LONG).show();
        }
        else{
            Toasty.error(this, resultMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void createTeam(int resultCode, String resultMessage) {

    }

    @Override
    public void hasTeam(int resultCode, String resultMessage) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnLeaveTeam:
                loadDaTaDialog.show();
                pTeam.receivedLeaveMyTeam(user.getUid(),teamId,this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
