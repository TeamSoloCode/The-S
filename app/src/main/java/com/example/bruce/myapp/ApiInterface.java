package com.example.bruce.myapp;

import com.example.bruce.myapp.ApiCommonRespone.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.TouristLocation.GetAllTouristLocation;
import com.example.bruce.myapp.ApiGetObject.TouristLocation.GetTouristLocationDetailById;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by BRUCE on 6/5/2018.
 */

public interface ApiInterface {
    //---------------------------------Get API-----------------------------------------------------

    //------TouristLocation
    /**
     * Lấy hết các địa điểm du lịch
     * @return JSON list tourist location
     */
    @GET("GetAllTouristLocation")
    Call<GetAllTouristLocation> getAllTouristLocation();

    @GET("GetTouristLocationDetailById")
    Call<GetTouristLocationDetailById> getTouristLocationById(@Query("id") String locationId);

    //---------------------------------Post API-----------------------------------------------------
    //------Rating
    /**
     * Rating post api
     * @param locationId
     * @param userId
     * @param stars
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("Rating")
    Call<CommonResponse> postRating(@Field("locationId") String locationId,
                                    @Field("userId") String userId,
                                    @Field("stars") float stars);
    //Comment

}
