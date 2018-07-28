package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.GetAllTouristLocation;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.BigMap.IBigMap;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 11/21/2017.
 */

public class MBigMap {
    private IBigMap callback;
    public MBigMap() {

    }

    public MBigMap(IBigMap callback) {

        this.callback = callback;
    }

    /**
     * Send ping to my team
     * @param userId
     * @param teamId
     * @param pingCode
     */
    public void sendPing(String userId, String teamId, int pingCode){

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.sendPing(userId, teamId, pingCode).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    callback.sendPing(response.body().getResultCode(), response.body().getResultMessage().toString());
                }
                else{
                    callback.sendPing(99, "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                callback.sendPing(99, "Không thể kết nối với máy chủ");
            }
        });

    }

    public void getAllTouristLocation(){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAllTouristLocation().enqueue(new Callback<GetAllTouristLocation>() {
            @Override
            public void onResponse(Call<GetAllTouristLocation> call, retrofit2.Response<GetAllTouristLocation> response) {
                if(response.isSuccessful()){
                    if(response.body().getResultCode() == 1){
                        callback.getAllTouristLocation(response.body().getResultData(),1,"");
                    }
                    else {
                        callback.getAllTouristLocation(null,
                                response.body().getResultCode(),
                                response.body().getResultMessage().toString());
                    }
                }
                else{
                    callback.getAllTouristLocation(null,
                            99,
                            "Không thể kết nối với máy chủ");
                }
            }

            @Override
            public void onFailure(Call<GetAllTouristLocation> call, Throwable t) {
                callback.getAllTouristLocation(null,
                        99,
                        "Không thể kết nối với máy chủ");
            }
        });
    }


    public double Radius(LatLng mLocation, LatLng touristLocation) {
        double distance;
        double a = touristLocation.latitude - mLocation.latitude;
        double b = touristLocation.longitude - mLocation.longitude;
        double c = Math.sqrt(a*a + b*b);
        distance = c*(111.2)*1000;
        return distance;
    }
}
