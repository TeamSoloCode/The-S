package com.example.bruce.myapp;

import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiGetObject.GetAllTeamMember;
import com.example.bruce.myapp.ApiGetObject.GetAllTouristLocation;
import com.example.bruce.myapp.ApiGetObject.GetInvitersInfo;
import com.example.bruce.myapp.ApiGetObject.GetTouristLocationDetailById;

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

    //Get location detail
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
    //------Comment
    /**
     * Post comment
     * @param locationId
     * @param userId
     * @param comment
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("AddComment")
    Call<CommonResponse> postComment(@Field("locationId") String locationId,
                                    @Field("userId") String userId,
                                    @Field("comment") String comment);


    //------Team

    /**
     * Create team
     * @param userId
     * @param teamsName
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("CreateTeam")
    Call<CommonResponse> postCreateTeam(@Field("userId") String userId, @Field("teamsName") String teamsName);

    /**
     * Has team checker
     * @param userId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("HasTeam")
    Call<CommonResponse> hasTeam(@Field("userId") String userId);

    /**
     * Invite user to my team
     * @param teamId
     * @param userId
     * @param userInvitedEmail
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("InviteMember")
    Call<CommonResponse> inviteMember(@Field("teamId") String teamId,
                                      @Field("userId") String userId,
                                      @Field("userInvitedEmail") String userInvitedEmail);
    /**
     * Lấy thông tin của người mời mình vào đội
     * @param userId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("GetInviterInfo")
    Call<GetInvitersInfo> inviterInfo(@Field("userId") String userId);

    /**
     * Đồng ý lời mời vào đội
     * @param userId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("AcceptTheInvitation")
    Call<CommonResponse> acceptTheInvitation(@Field("userId") String userId,
                                             @Field("teamId") String teamId);


    /**
     * Kiểm tra user có phải leader hay không
     * @param userId
     * @param teamId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("IsLeader")
    Call<CommonResponse> isLeader(@Field("userId") String userId,
                                  @Field("teamId") String teamId);

    /**
     * Lấy danh sách thành viên trong team
     * @param userId
     * @param teamId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("GetAllMember")
    Call<GetAllTeamMember> getAllTeamsMember(@Field("userId") String userId,
                                             @Field("teamId") String teamId);

    /**
     * Lấy danh sách thành viên trong team
     * @param userId
     * @param teamId
     * @return
     */
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("LeaveTeam")
    Call<CommonResponse> leaveMyTeam(@Field("userId") String userId,
                                       @Field("teamId") String teamId);

    //------Diary
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("CreateDiary")
    Call<CommonResponse> createNewDiary(@Field("userId") String userId);
}
