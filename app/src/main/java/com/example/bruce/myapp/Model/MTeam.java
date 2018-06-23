package com.example.bruce.myapp.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.GetInvitersInfo;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.Team.ITeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

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

    /**
     * Người dùng tạo team
     * @param userId
     */
    public void createTeam(String userId, String teamsName){

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.postCreateTeam(userId, teamsName).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.createTeam(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.createTeam(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.createTeam(99, "Không thể kết nối với máy chủ");
            }
        });
    }

     /**
     * Kiểm tra trạng thái đội của người dùng
     * @param userId
     */
    public void hasTeam(String userId , Context context){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.hasTeam(userId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                SharedPreferences sharedPref = context.getSharedPreferences("my_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if(response.isSuccessful()){
                    if(response.body().getResultCode() == 111){
                        editor.putString("teamId", response.body().getResultMessage().toString());
                        editor.apply();
                        callback.hasTeam(response.body().getResultCode(),response.body().getResultMessage().toString());
                    }
                    else{
                        callback.hasTeam(response.body().getResultCode(), response.body().getResultMessage().toString());
                        editor.putString("teamId", "");
                        editor.apply();
                    }
                }
                else{
                    callback.hasTeam(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.hasTeam(99, "Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Lây thông tin của người mời mình vào đội
     * @param userId
     */
    public void getInvitersInformation(String userId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.inviterInfo(userId).enqueue(new Callback<GetInvitersInfo>() {
            @Override
            public void onResponse(Call<GetInvitersInfo> call, retrofit2.Response<GetInvitersInfo> response) {
                if(response.isSuccessful()){
                    callback.getInvitersInfo(
                            response.body().getResultCode(),
                            response.body().getResultData(),
                            "");
                }
                else{
                    callback.getInvitersInfo(99, null,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<GetInvitersInfo> call, Throwable t) {
                callback.getInvitersInfo(99, null,"Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Mời thành viên vào đội mình tạo
     * @param userId
     * @param userInvitedEmail
     * @param context
     */
    public void handleInviteMember(String userId, String userInvitedEmail, Context context){

        SharedPreferences sharedPref = context.getSharedPreferences("my_data", MODE_PRIVATE);

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.inviteMember(sharedPref.getString("teamId",""),userId, userInvitedEmail).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.inviteMember(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.inviteMember(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.inviteMember(99, "Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Chấp nhận lời mời từ người mời
     * @param userId
     * @param teamId
     */
    public void handleAcceptTheInvitation(String userId, String teamId){

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.acceptTheInvitation(userId, teamId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.acceptInvitation(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.acceptInvitation(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.acceptInvitation(99, "Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Kiểm tra user có phải leader hay không
     * @param teamId
     * @param userId
     * @param context
     */
    public void handleIsLeader(String teamId, String userId, Context context){

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.isLeader(userId, teamId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.acceptInvitation(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.acceptInvitation(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.acceptInvitation(99, "Không thể kết nối với máy chủ");
            }
        });
    }

    public void handleGetAllTeamsMember(){

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
