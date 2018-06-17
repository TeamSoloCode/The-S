package com.example.bruce.myapp;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.ChatMessageAdapter;
import com.example.bruce.myapp.Data.ChatRoom;
import com.example.bruce.myapp.Data.MessageChats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class ActivityChat extends AppCompatActivity {
    EditText edtMessage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRefChat,myRefUserChat,myRefRoomChat,myRefRoomMember;


    RecyclerView recyclerChat;
    ArrayList<MessageChats>listMessage;
    ChatMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String roomname=getIntent().getStringExtra("roomname");
        addControl();

        DateFormat df = new SimpleDateFormat(" d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRefChat=firebaseDatabase.getReference("ChatMessage");
        myRefUserChat=firebaseDatabase.getReference("UserChat");
        myRefRoomChat=firebaseDatabase.getReference("Roomchat");
        myRefRoomMember=firebaseDatabase.getReference("RoomChatMember");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMessage.getText().toString().equals("")){
                    edtMessage.setError(getString(R.string.Please_Enter_your_Message));
                }else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid=user.getUid();
                    String key =myRefChat.push().getKey();
                    String roomname=getIntent().getStringExtra("roomname");
                    MessageChats messageChats=new MessageChats(edtMessage.getText().toString(),user.getDisplayName(),date,true,userid,String.valueOf(user.getPhotoUrl()));
                    myRefChat.child(roomname).child(key).setValue(messageChats);
                    myRefUserChat.child(userid).child(key).setValue(messageChats);
                    edtMessage.setText("");
                }
            }
        });
        showMessage(roomname);
        addRoomMember();
    }

    private void addRoomMember() {
        myRefChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Toast.makeText(ActivityChat.this, data.getKey()+"", Toast.LENGTH_SHORT).show();
                    for(DataSnapshot data1 :data.getChildren()){
                        MessageChats messageChats=data1.getValue(MessageChats.class);
                        if(!data.getKey().equals(messageChats.getUserid())){

                            myRefRoomMember.child(data.getKey()).child(messageChats.getUserid()).setValue(messageChats.getUser());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showMessage(String room){

        myRefChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String key=data.getKey();
                    if(room.equals(key)){
                        for(DataSnapshot data2:data.getChildren()){
                            MessageChats messageChats=data2.getValue(MessageChats.class);
                            messageChats.setIsme(false);
                            listMessage.add(messageChats);
                            recyclerChat.setAdapter(adapter);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addControl(){
        recyclerChat=findViewById(R.id.recycleView_ListChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityChat.this,LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);

        recyclerChat.setLayoutManager(layoutManager);
        edtMessage=findViewById(R.id.edtMessage11);
        listMessage=new ArrayList<>();
        adapter=new ChatMessageAdapter(listMessage,ActivityChat.this);
        recyclerChat.setAdapter(adapter);
    }

}
