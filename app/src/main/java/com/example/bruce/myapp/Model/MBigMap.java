package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiGetObject.GetAllTouristLocation;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.BigMap.IBigMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 11/21/2017.
 */

public class MBigMap {
    private IBigMap callback;
    private DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    public MBigMap() {

    }

    public MBigMap(IBigMap callback) {

        this.callback = callback;
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
