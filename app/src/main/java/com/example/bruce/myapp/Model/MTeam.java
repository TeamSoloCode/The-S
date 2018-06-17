package com.example.bruce.myapp.Model;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Team.ITeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Admin on 01/12/2017.
 */

public class MTeam {
    ITeam callback;

    public MTeam() {
    }

    public MTeam(ITeam callback) {
        this.callback = callback;
    }

    public void handleAddListUser(TeamAdapter adapter,ArrayList<UserProfile> listUser) {
        FirebaseDatabase.getInstance().getReference("CheckTeam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String idCaptain= dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Captain").getValue().toString();
                FirebaseDatabase.getInstance().getReference("TeamUser").child(idCaptain).child("member")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot1, String s) {
                                //dataSnapshot1 đang đứng ở những key  của member
                                FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                                        {
                                            if(dataSnapshot1.getKey().equals(dataSnapshot2.getKey()) && !dataSnapshot1.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) )
                                            {
                                                UserProfile constructer_userProfile=dataSnapshot2.getValue(UserProfile.class);
                                                listUser.add(constructer_userProfile);
                                                // callback.GetListUser(listUser);
                                                Log.d("dsad","sad");
                                                adapter.notifyDataSetChanged();
//                                    teamAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot1) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot1, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void handleRemoveMember(){

    }

    public void handlePingToMyTeam(String problemName){
        FirebaseDatabase.getInstance().getReference(problemName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        FirebaseDatabase.getInstance().getReference("CheckTeam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists())
                {
                    String idCaptain= dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Captain").getValue().toString();
                    FirebaseDatabase.getInstance().getReference().child("TeamUser").child(idCaptain).child("member").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                if(!dataSnapshot1.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {
                                    FirebaseDatabase.getInstance().getReference(problemName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Team").child(dataSnapshot1.getKey()).setValue("1");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    TextToSpeech textToSpeech;

    public void teamPingListerner(Context context){
        FirebaseDatabase.getInstance().getReference("GasProblem").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseDatabase.getInstance().getReference("GasProblem").child(dataSnapshot.getKey()).child("Team").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    textToSpeech.setLanguage(Locale.getDefault());
                                    switch (Locale.getDefault().toString()){

                                        case "vi_VN":case "vi-vn":
                                            textToSpeech.speak("Xin Chào Mọi Người",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                        default:
                                            textToSpeech.speak("Hello guys",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                    }
                                }
                            });
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("ádasds",dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

        FirebaseDatabase.getInstance().getReference("NeedRepair").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseDatabase.getInstance().getReference("NeedRepair").child(dataSnapshot.getKey()).child("Team").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    textToSpeech.setLanguage(Locale.getDefault());
                                    switch (Locale.getDefault().toString()){

                                        case "vi_VN":case "vi-vn":
                                            textToSpeech.speak("Xin Chào Mọi Người",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                        default:
                                            textToSpeech.speak("Hello guys",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                    }
                                }
                            });
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("ádasds",dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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



        FirebaseDatabase.getInstance().getReference("Crash").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseDatabase.getInstance().getReference("Crash").child(dataSnapshot.getKey()).child("Team").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    textToSpeech.setLanguage(Locale.getDefault());
                                    switch (Locale.getDefault().toString()){

                                        case "vi_VN":case "vi-vn":
                                            textToSpeech.speak("Xin Chào Mọi Người",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                        default:
                                            textToSpeech.speak("Hello guys",textToSpeech.QUEUE_FLUSH,null);
                                            break;
                                    }
                                }
                            });
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("ádasds",dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

    public void SpeechGoogle(String content, Context context) {
        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.speak(content,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }
}
