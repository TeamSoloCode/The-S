package com.example.bruce.myapp.Model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruce.myapp.Api;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.HistoryAndHobby.IHistoryAndHobby;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by BRUCE on 11/17/2017.
 */

public class MHistoryAndHobby {

    IHistoryAndHobby callback;
    public MHistoryAndHobby() {
    }


    public MHistoryAndHobby(IHistoryAndHobby callback) {
        this.callback = callback;
    }

    public void handleEnableGPS(Context context, Activity activity){

        //xin cap quyen su dung GPS cua thiet bi
        if (Build.VERSION.SDK_INT >= 23) {

            callback.EnableGPS_APILater22();
        }
        else
        {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            callback.EnableGPS_API22();
        }
    }

    public void handleGetLocationData(Context context){

        ArrayList<Tourist_Location> tourist_locations = new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Api.url_TouristLocation, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Tourist_Location tourist_location = new Tourist_Location();
                            JSONObject jsonObject = response.getJSONObject(i);
                            tourist_location.location_ID = jsonObject.getInt("id");
                            tourist_location.LocationName=jsonObject.getString("ten");
                            tourist_location.Address=jsonObject.getString("diachi");
                            tourist_location.Longtitude=jsonObject.getDouble("log");
                            tourist_location.Latitude=jsonObject.getDouble("lat");
                            tourist_location.LocationImg=jsonObject.getString("img");
                            tourist_location.BasicInfo=jsonObject.getString("thongtincoban");
                            tourist_location.province_ID=jsonObject.getInt("matinh");
                            tourist_location.star = Float.parseFloat(jsonObject.getString("star"));
                            tourist_location.kindOfLocation = jsonObject.getInt("maloai");
                            tourist_locations.add(tourist_location);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.GetLocationData(tourist_locations);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void handleGetUserData(String userUid){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("User");
        mData.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                callback.GetUserHistory(userProfile.Hobbie);
                callback.UserProfile(userProfile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void handleGetUserBehavior(String userUid){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("User");
        mData.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                callback.getUserBehavior(userProfile.Behavior);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void handleUserHistory(String a , ArrayList<Tourist_Location> tourist_locations){
        ArrayList<Tourist_Location> returnList = new ArrayList<>();
        Set<String> history = new LinkedHashSet<>(Arrays.asList(a.split(",")));

        for(String str : history){
            for(Tourist_Location tl : tourist_locations){
                if(tl.location_ID == Integer.parseInt(str)){
                    returnList.add(0,tl);
                }
            }
        }
        callback.GetUserHistoryLocation(returnList);
    }
    private int countHistory(String i, ArrayList<String> a){

        int count = 0;
        for(String o : a){
            if(o.equals(i)){
                count++;
            }
        }
        return count;
    }
    public void handleRecommendeToUser(String behavior, String history, ArrayList<Tourist_Location> tourist_locations){
        ArrayList<String> historyArray = new ArrayList<>(Arrays.asList(history.split(",")));

        HashMap<String,Integer> mapBehavior= new HashMap<>();
        ArrayList<String> behaviorArray = new ArrayList<>(Arrays.asList(behavior.split(",")));
        Set<String> shortHistory = new LinkedHashSet<>(Arrays.asList(behavior.split(",")));

        for(String str : shortHistory){
            mapBehavior.put(str,countHistory(str,behaviorArray));
        }


        int[] rank = new int[3];
        int e = 0;
        while (e < rank.length && e <= mapBehavior.keySet().size())
        {
            for(String i : mapBehavior.keySet()){
                boolean check = false;
                for(String j : mapBehavior.keySet()){
                    if(mapBehavior.get(i).intValue() < mapBehavior.get(j).intValue()){
                        check = true;
                    }
                }
                if(check == false){
                    rank[e] = Integer.parseInt(i);
                    mapBehavior.remove(i);
                    e++;
                    break;
                }
            }
        }

        ArrayList<Tourist_Location> returnList = new ArrayList<>();
        for(int i = 0 ; i < 3; i++){
            int count = 0;
            for(Tourist_Location tl : tourist_locations){
                if(i == 0){
                    if(tl.getKindOfLocation() == rank[0] && count < 5){
                        returnList.add(tl);
                        count++;
                    }
                }
                if(i == 1){
                    if(tl.getKindOfLocation() == rank[1] && count< 3){
                        returnList.add(tl);
                        count++;
                    }
                }
                if(i == 2){
                    if(tl.getKindOfLocation() == rank[2] && count< 2){
                        returnList.add(tl);
                        count++;
                    }
                }
            }
        }

//        for (String hA : historyArray){
//            for (Tourist_Location tl : returnList){
//                if(tl.getLocation_ID() == Integer.parseInt(hA)){
//                    returnList.remove(tl);
//                }
//            }
//        }
        callback.returnRecommendedList(returnList);
    }

    public void handleTeamCheker(String idUser, String[] menuItem, ListView listView)
    {
        FirebaseDatabase.getInstance().getReference("CheckTeam").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idUser).exists())
                {
                    callback.HasTeam(menuItem,listView);
                }
                else {
                    callback.HasNoTeam(menuItem,listView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    MBigMap bigMap = new MBigMap();
    public void handleLocationNearByList(LatLng myLocation,ArrayList<Tourist_Location> tourist_locations){

        for(Tourist_Location tl : tourist_locations){
            tl.setDistance(bigMap.Radius(myLocation,new LatLng(tl.getLatitude(),tl.getLongtitude())));
        }
        Collections.sort(tourist_locations);
        callback.returnLocationNearByList(tourist_locations);
    }
}
