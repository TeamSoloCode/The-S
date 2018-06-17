package com.example.bruce.myapp.Presenter.HistoryAndHobby;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Model.MHistoryAndHobby;
import com.example.bruce.myapp.View.HistoryAndHobby.IViewHistoryAndHobby;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/17/2017.
 */

public class PHistoryAndHobby implements IHistoryAndHobby {

    private MHistoryAndHobby modelHistoryAndHobby = new MHistoryAndHobby(this);
    public IViewHistoryAndHobby callbackToView;

    public PHistoryAndHobby(IViewHistoryAndHobby callbackToView) {

        this.callbackToView = callbackToView;
    }


    public void receivedEnableGPS(Context context, Activity activity){

        modelHistoryAndHobby.handleEnableGPS(context, activity);
    }

    public void receivedGetLocationData(Context context){

        modelHistoryAndHobby.handleGetLocationData(context);
    }
    public void receivedGetUserData(String uId){

        modelHistoryAndHobby.handleGetUserData(uId);
    }

    public void receivedGetUsetBehavior(String uId){

        modelHistoryAndHobby.handleGetUserBehavior(uId);
    }
    //0971391638
    public void receivedGetUserHistoryLocation(String a , ArrayList<Tourist_Location> tourist_locations){

        modelHistoryAndHobby.handleUserHistory(a,tourist_locations);
    }

    public void receivedRecommendeToUser(String behavior,String history, ArrayList<Tourist_Location> tourist_locations){

        modelHistoryAndHobby.handleRecommendeToUser(behavior,history,tourist_locations);

    }
    public void receivedTeamChecker(String idUser, String[] menuItem, ListView listView){
        modelHistoryAndHobby.handleTeamCheker(idUser,menuItem,listView);
    }

    public void receivedLocationNearByList(LatLng myLocation, ArrayList<Tourist_Location> tourist_locations){
        modelHistoryAndHobby.handleLocationNearByList(myLocation,tourist_locations);
    }
    @Override
     public void EnableGPS_API22() {

        callbackToView.EnableGPS_API22();
    }

    @Override
    public void EnableGPS_APILater22() {

        callbackToView.EnableGPS_APILater22();
    }

    @Override
    public ArrayList<Tourist_Location> GetLocationData(ArrayList<Tourist_Location> tourist_locations) {

        return callbackToView.GetLocationData(tourist_locations);
    }

    @Override
    public String GetUserHistory(String history) {

        return callbackToView.GetUserHistory(history);
    }

    @Override
    public String getUserBehavior(String behavior) {

        return callbackToView.getUserBehavior(behavior);
    }

    @Override
    public UserProfile UserProfile(UserProfile User) {
        return callbackToView.UserProfile(User);
    }

    @Override
    public ArrayList<Tourist_Location> GetUserHistoryLocation(ArrayList<Tourist_Location> tourist_locations) {

        return callbackToView.GetUserHistoryLocation(tourist_locations);
    }

    @Override
    public ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations) {
        return callbackToView.returnRecommendedList(tourist_locations);
    }

    @Override
    public void returnLocationNearByList(ArrayList<Tourist_Location> touristLocations) {
        callbackToView.returnLocationNearByList(touristLocations);
    }

    @Override
    public void HasTeam(String[] menuItem, ListView listView) {
        callbackToView.HasTeam(menuItem,listView);
    }

    @Override
    public void HasNoTeam(String[] menuItem, ListView listView) {
        callbackToView.HasNoTeam(menuItem,listView);
    }

}
