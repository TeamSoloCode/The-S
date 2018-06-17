package com.example.bruce.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.bruce.myapp.Adapter.RoomChatAdapter2;
import com.example.bruce.myapp.Data.ChatRoom;
import com.example.bruce.myapp.View.Team.TeamActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Phongchat extends AppCompatActivity {
    ListView listViewRoom;
    ArrayList<ChatRoom>listRoom;
    RoomChatAdapter2 RoomAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef,myRefRoom,myRefRoomChat;

    Button btnchat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongchat);
        addControl();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("ChatMessage");
        myRefRoom=database.getReference("Roomchat");
        myRefRoomChat=database.getReference("Roomchat");
        addRoomChat();
        getItemRoom();
        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ChatRoom chatRoom=new ChatRoom(user.getDisplayName(),user.getUid());
                myRefRoomChat.child(user.getDisplayName()).removeValue();
                myRefRoomChat.child(user.getDisplayName()).setValue(chatRoom);
                Intent intent = new Intent(Phongchat.this, ActivityChat.class);
                intent.putExtra("roomname",user.getUid());
                startActivity(intent);

            }
        });
    }

    private void addRoomChat() {
        myRefRoom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatRoom chatRoom=dataSnapshot.getValue(ChatRoom.class);
                listRoom.add(chatRoom);
                listViewRoom.setAdapter(RoomAdapter);
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

    }



    public void getItemRoom(){
        listViewRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentroom=new Intent(Phongchat.this,ActivityChat.class);
                intentroom.putExtra("roomname", listRoom.get(i).getUserId());
                startActivity(intentroom);
            }
        });

    }
    public void addControl(){
        btnchat=findViewById(R.id.btnchats);
        listRoom=new ArrayList<>();
        listViewRoom=findViewById(R.id.listRoom);
        RoomAdapter=new RoomChatAdapter2(Phongchat.this,R.layout.item_chatroom,listRoom);

    }


}
