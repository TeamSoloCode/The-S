package com.example.bruce.myapp.Presenter.HistoryAndHobby;

import android.widget.ListView;

import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Data.UserProfile;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/17/2017.
 */

public interface IHistoryAndHobby {
    void EnableGPS_API22();
    void EnableGPS_APILater22();
    ArrayList<Tourist_Location> GetLocationData(ArrayList<Tourist_Location> tourist_locations);
    String GetUserHistory(String history);
    String getUserBehavior(String behavior);
    UserProfile UserProfile(UserProfile User);
    ArrayList<Tourist_Location> GetUserHistoryLocation(ArrayList<Tourist_Location> tourist_locations);
    ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations);
    void returnLocationNearByList(ArrayList<Tourist_Location> touristLocations);
    void HasTeam(String[] menuItem,ListView listView);
    void HasNoTeam(String[] menuItem,ListView listView);
}
