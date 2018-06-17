package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiGetObject.TouristLocation.GetTouristLocationDetailById;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.InformationFragment.IInformationFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 11/25/2017.
 */

public class MInformation_Fragment {
    private IInformationFragment callback;

    public MInformation_Fragment(IInformationFragment callback) {

        this.callback = callback;
    }

    public void getTouristLocationById(String locationId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getTouristLocationById(locationId).enqueue(new Callback<GetTouristLocationDetailById>() {
            @Override
            public void onResponse(Call<GetTouristLocationDetailById> call, retrofit2.Response<GetTouristLocationDetailById> response) {
                if(response.isSuccessful()){
                    if(response.body().getResultCode() == 1){
                        callback.getTouristLocationById(response.body().getResultData(),1,"");
                    }
                    else {
                        callback.getTouristLocationById(null,
                                response.body().getResultCode(),
                                response.body().getResultMessage().toString());
                    }
                }
                else{
                    callback.getTouristLocationById(null,99,"Không thể kết nối với máy chủ");
                }

            }

            @Override
            public void onFailure(Call<GetTouristLocationDetailById> call, Throwable t) {
                callback.getTouristLocationById(null,99,"Không thể kết nối với máy chủ");
            }
        });
    }
}
