package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.Presenter.DiaryCheckPoint.IDiaryCheckPoint;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class MDiaryCheckPoint {
    private IDiaryCheckPoint callback;
    private MDiaryCheckPoint(){}

    public MDiaryCheckPoint(IDiaryCheckPoint callback){this.callback = callback;}

    /**
     * Add chack point
     * @param userId
     * @param diaryId
     * @param checkPoint
     */
    public void addDiaryCheckPoint(String userId, String diaryId, CheckPoint checkPoint){
        Gson gson = new Gson();

        String jsonData = gson.toJson(checkPoint);

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.addDiaryCheckPoint(userId, diaryId, jsonData).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.addDiaryCheckPoint(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.addDiaryCheckPoint(99,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.addDiaryCheckPoint(99,"Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Update check points
     * @param userId
     * @param diaryId
     * @param checkPoint
     */
    public void updateDiaryCheckPoint(String userId, String diaryId, CheckPoint checkPoint){
        Gson gson = new Gson();

        String jsonData = gson.toJson(checkPoint);

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.updateDiaryCheckPoint(userId, diaryId, jsonData).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.updateDiaryCheckPoint(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.updateDiaryCheckPoint(99,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.updateDiaryCheckPoint(99,"Không thể kết nối với máy chủ");
            }
        });
    }
}
