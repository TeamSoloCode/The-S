package com.example.bruce.myapp;

import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.myapp.Data.CheckPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by BRUCE on 4/26/2017.
 */

public class MiniMapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    View mView;
    boolean count = false;
    private String mode;
    private CheckPoint checkPoint;
    public MiniMapsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataFromIntent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.mini_map_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragment = getActivity().getFragmentManager();
        final MapFragment mf = (MapFragment) fragment.findFragmentById(R.id.map);

        mf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        if(mode != null && mode.equals("update")){
            mMap.addMarker(new MarkerOptions().title(checkPoint.getDescription()).icon(null).position(checkPoint.toPosition()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(checkPoint.toPosition(), 16));
        }
        else{
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if(count == false) {
                        LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 14));
                        count = !count;
                    }

                }
            });
        }


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private void getDataFromIntent(){
        mode = getActivity().getIntent().getStringExtra("mode");
        ArrayList<CheckPoint> passData = getActivity().getIntent().getParcelableArrayListExtra("checkpoint");
        if(passData != null){
            checkPoint = passData.get(0);
        }

    }
}
