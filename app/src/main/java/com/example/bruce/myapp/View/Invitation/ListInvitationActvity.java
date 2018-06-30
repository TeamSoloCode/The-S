package com.example.bruce.myapp.View.Invitation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.ListInvitationAdapter;
import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.Data.TeamMember;
import com.example.bruce.myapp.Presenter.Team.PTeam;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Team.IViewTeam;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ListInvitationActvity extends AppCompatActivity implements IViewTeam, ListInvitationAdapter.RecyclerViewClicklistener {

    RecyclerView recyclerViewInvitation;
    private ListInvitationAdapter listInvitationAdapter;
    private PTeam pTeam = new PTeam(this);
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ChildEventListener childEventListenerInvitation;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_invitation);
        initialize();
        //Get invitation realtime
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Invitation").child(user.getUid());
        childEventListenerInvitation = new ChildEventListener() {
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
        };
        mDatabaseRef.addChildEventListener(childEventListenerInvitation);
    }

    private void initialize() {
        recyclerViewInvitation = findViewById(R.id.recyclerViewInvitation);
    }

    @Override
    public void getInvitersInfo(int resultCode, List<InvitersInfo> invitersInfo, String resultMessage) {
        ArrayList<InvitersInfo> listInvitersInfo = (ArrayList<InvitersInfo>) invitersInfo;
        recyclerViewInvitation.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listInvitationAdapter = new ListInvitationAdapter(listInvitersInfo, this);
        recyclerViewInvitation.setAdapter(listInvitationAdapter);
        listInvitationAdapter.setClickListener(ListInvitationActvity.this);
        listInvitationAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemClickMember(View view, String teamId) {
        pTeam.receivedAcceptInvitation(user.getUid(), teamId);
    }

    @Override
    public void acceptInvitation(int resultCode, String resultMessage) {
        if(resultCode == 002){
            Toasty.success(this, resultMessage, Toast.LENGTH_LONG).show();
        }
        else if(resultCode == 111){
            Toasty.warning(this, resultMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(childEventListenerInvitation);
    }

    @Override
    public void createTeam(int resultCode, String resultMessage) {

    }

    @Override
    public void hasTeam(int resultCode, String resultMessage) {

    }

    @Override
    public void inviteMember(int resultCode, String resultMessage) {

    }

    @Override
    public void isLeader(int resultCode, String resultMessage) {

    }

    @Override
    public void getAllTeamMember(int resultCode, ArrayList<TeamMember> listTeamMember, String resultMessage) {

    }

    @Override
    public void markMemberLocation(HashMap<String, MarkerOptions> listMemberLocation) {

    }

    @Override
    public void markMemberLocationOnChanged(Marker listMemberMarker) {

    }

    @Override
    public void leaveMyTeam(int resultCode, String resultMessage) {

    }
}
