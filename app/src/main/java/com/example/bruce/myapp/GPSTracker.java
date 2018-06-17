package com.example.bruce.myapp;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by BRUCE on 5/3/2017.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    boolean canGetLocation;
    Location location;

    double latitude;
    double longtitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;


    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {
                canGetLocation = false;
            } else {
                this.canGetLocation = true;
                if (isNetworkEnable) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                }
                if (isGPSEnable) {
                    if (locationManager == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longtitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    public  double getLatitude()
    {
        if(location!=null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }
    public double getLongtitude()
    {
        if(location != null)
        {
            longtitude = location.getLongitude();
        }
        return longtitude;
    }

    public String Address(double Lat, double Lg)
    {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{

            List<Address> addressList = geocoder.getFromLocation(Lat,Lg,1);
            if(addressList.size()>0 && addressList != null){

                //hien thi ten thanh pho (o day tao cho ra het dia chi lun)
                int i = 0;
                do{

                    address += ", " + addressList.get(0).getAddressLine(i);
                    i++;
                }
                while(addressList.get(0).getAddressLine(i) != null);
            }

        }
        catch(IOException io)
        {
            io.printStackTrace();
         //   Toast.makeText(context, "Can't get my location", Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }
    public void showSettingAlert(){
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is setting");

        alertDialog.setMessage("GPS is not enable. Do you want to go to menu ?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
