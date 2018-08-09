package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.GetAllCheckPoint;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.Diary.IDiary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class MDiary {
    IDiary callback;

    public MDiary(IDiary callback){this.callback = callback;}

    public void getAllCheckPoint(String userId, String diaryId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAllCheckPoint(userId, diaryId).enqueue(new Callback<GetAllCheckPoint>() {
            @Override
            public void onResponse(Call<GetAllCheckPoint> call, retrofit2.Response<GetAllCheckPoint> response) {
                if(response.isSuccessful()){
                    callback.getAllCheckPoint(
                            response.body().getResultCode(),
                            response.body().getResultData(),
                            "");
                }
                else{
                    callback.getAllCheckPoint(99,null,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<GetAllCheckPoint> call, Throwable t) {
                callback.getAllCheckPoint(99,null,"Không thể kết nối với máy chủ");
            }
        });
    }

    public void getAllMySharedCheckPoint(String userId, String diaryId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAllMySharedDiarysCheckPoint(userId, diaryId).enqueue(new Callback<GetAllCheckPoint>() {
            @Override
            public void onResponse(Call<GetAllCheckPoint> call, retrofit2.Response<GetAllCheckPoint> response) {
                if(response.isSuccessful()){
                    callback.getAllMySharedCheckPoint(
                            response.body().getResultCode(),
                            response.body().getResultData(),
                            "");
                }
                else{
                    callback.getAllMySharedCheckPoint(99,null,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<GetAllCheckPoint> call, Throwable t) {
                callback.getAllMySharedCheckPoint(99,null,"Không thể kết nối với máy chủ");
            }
        });
    }

    public void shareMyDiary(String userId, String diaryId, String userSharedEmail){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.sharedMyDiary(userId, diaryId, userSharedEmail).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.sharedDiary(
                            response.body().getResultCode(),
                            response.body().getResultMessage().toString());
                }
                else{
                    callback.sharedDiary(
                            99,
                            "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.sharedDiary(
                        99,
                        "Không thể kết nối với máy chủ");
            }
        });
    }
}
