package com.example.bruce.myapp.Direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by BRUCE on 5/7/2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
