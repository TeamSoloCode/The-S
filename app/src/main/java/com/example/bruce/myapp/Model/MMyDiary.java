package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.GetAllMyDiaries;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.MyDiary.IMyDiary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 7/1/2018.
 */

public class MMyDiary {
    private IMyDiary callback;

    public MMyDiary() {}

    public MMyDiary(IMyDiary callback){this.callback = callback;}

    /**
     * Create new diary
     * @param userId
     */
    public void createDiary(String userId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.createNewDiary(userId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.createNewDiary(response.body().getResultCode(),response.body().getResultMessage().toString());
                }
                else{
                    callback.createNewDiary(99,"Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.createNewDiary(99,"Không thể kết nối với máy chủ");
            }
        });
    }

    /**
     * Get all my diaries
     * @param userId
     */
    public void getAllMyDiary(String userId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAllMyDiary(userId).enqueue(new Callback<GetAllMyDiaries>() {
            @Override
            public void onResponse(Call<GetAllMyDiaries> call, retrofit2.Response<GetAllMyDiaries> response) {
                if(response.isSuccessful()){
                    callback.getAllMyDiary(response.body().getResultCode(),
                            response.body().getResultData(),
                            "");
                }
                else{
                    callback.getAllMyDiary(99,
                            null,
                            "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<GetAllMyDiaries> call, Throwable t) {
                callback.getAllMyDiary(99,
                        null,
                        "Không thể kết nối với máy chủ");
            }
        });
    }
}
