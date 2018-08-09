package com.example.bruce.myapp.View.Diary;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.Direction.DirectionFinder;
import com.example.bruce.myapp.Direction.DirectionFinderListener;
import com.example.bruce.myapp.Direction.Route;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Presenter.Diary.PDiary;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.DiaryCheckPoint.DiaryCheckPointActivity;
import com.example.bruce.myapp.View.MyDiary.MyDiaryActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DiaryActivity extends FragmentActivity implements OnMapReadyCallback,
        DirectionFinderListener, View.OnClickListener, GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMyLocationChangeListener, IViewDiary {

    private GoogleMap mMap;
    private ProgressDialog progressDialog;

    private ArrayList<CheckPoint> listCheckPoint;
    private HashMap<String, CheckPoint> hashMapCheckPoint;

    //firebase
    private FirebaseUser user;
    private ImageButton imgAddCheckPoint;
    private ImageButton imgShareDiary;

    //direction
    private List<Marker> originMarkers;
    private List<Marker> destinationMarkers;
    private List<Polyline> polylinePaths;
    private List<Circle> circle = new ArrayList<>();

    private String diaryId;
    private String shareMode;
    private PDiary pDiary;
    private GPSTracker gpsTracker;

    private LatLng mLocation = new LatLng(0,0);
    private String origin;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialize();

        user = FirebaseAuth.getInstance().getCurrentUser();
        diaryId = getIntent().getStringExtra("diaryId");
        shareMode = getIntent().getStringExtra("shareMode");
        pDiary = new PDiary(this);
        hashMapCheckPoint = new HashMap<>();

        if(shareMode != null){
            if(shareMode.equals("share")){
                //goi api lay check point dc chia se
                pDiary.receivedGetAllMySharedCheckPoint(user.getUid(), diaryId);
                imgAddCheckPoint.setVisibility(View.GONE);
                imgShareDiary.setVisibility(View.GONE);
            }
        }
        else{
            //goi api lấy tất cả check point của user về
            pDiary.receivedGetAllCheckPoint(user.getUid(), diaryId);
        }

        imgAddCheckPoint.setOnClickListener(this);
        imgShareDiary.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        gpsTracker = new GPSTracker(this);

        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingAlert();
            mLocation = null;
        }
        else{
          mLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongtitude());
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    private void initialize(){
        imgAddCheckPoint = findViewById(R.id.imgAddCheckPoint);
        imgShareDiary = findViewById(R.id.imgShareDiary);
    }

    private void drawDiaryPolylineOnMap(ArrayList<LatLng> listLatLng){
        polylinePaths = new ArrayList<>();

        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.GRAY).
                width(10);
        for (LatLng latLng : listLatLng) {
            polylineOptions.add(latLng);
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    @Override
    public void getAllCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage) {
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        if(listCheckPoint == null){
            return;
        }

        ArrayList<LatLng> listLatLng = new ArrayList<>();
        hashMapCheckPoint = new HashMap<>();

        for(CheckPoint checkPoint : listCheckPoint){

            hashMapCheckPoint.put(checkPoint.getId(), checkPoint);

            LatLng checkPointLocation = new LatLng(checkPoint.getLat(), checkPoint.getLog());
            listLatLng.add(checkPointLocation);

            switch (checkPoint.getKind()){
                case 0:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(null))
                            .setTag(checkPoint.getId());
                    break;
                case 2:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                            .setTag(checkPoint.getId());
                    break;
                case 3:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                            .setTag(checkPoint.getId());
                    break;
                case 4:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                            .setTag(checkPoint.getId());
                    break;
                case 5:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                            .setTag(checkPoint.getId());
                    break;
            }

        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listCheckPoint.size() - 1), 14));
        //drawDiaryPolylineOnMap(listLatLng);
        this.listCheckPoint = listCheckPoint;
    }

    @Override
    public void getAllMySharedCheckPoint(int resultCode, ArrayList<CheckPoint> listCheckPoint, String resultMessage) {
        if(resultCode != 1){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        //Ẩn 2 nut add với save đi vì ng dc chia sẽ chi dc xem
        imgShareDiary.setVisibility(View.GONE);
        imgAddCheckPoint.setVisibility(View.GONE);

        ArrayList<LatLng> listLatLng = new ArrayList<>();
        hashMapCheckPoint.clear();

        for(CheckPoint checkPoint : listCheckPoint){

            hashMapCheckPoint.put(checkPoint.getId(), checkPoint);

            LatLng checkPointLocation = new LatLng(checkPoint.getLat(), checkPoint.getLog());
            listLatLng.add(checkPointLocation);

            switch (checkPoint.getKind()){
                case 0:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(null))
                            .setTag(checkPoint.getId());
                    break;
                case 2:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                            .setTag(checkPoint.getId());
                    break;
                case 3:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                            .setTag(checkPoint.getId());
                    break;
                case 4:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                            .setTag(checkPoint.getId());
                    break;
                case 5:
                    mMap.addMarker(new MarkerOptions().position(checkPointLocation)
                            .title(checkPoint.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                            .setTag(checkPoint.getId());
                    break;
            }

        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listCheckPoint.size() - 1), 14));
        //drawDiaryPolylineOnMap(listLatLng);
        this.listCheckPoint.clear();
        this.listCheckPoint = listCheckPoint;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        final Dialog info = new Dialog(this);

        //info.requestWindowFeature(Window.FEATURE_NO_TITLE); -- bo title cua dialog
        info.setContentView(R.layout.dialog_bigmap);
        info.show();

        Button btnDirection = info.findViewById(R.id.btnDirection);
        Button btnInformation = info.findViewById(R.id.btnInformation);

        btnDirection.setText("Direction to this point");
        btnInformation.setText("Edit this point");

        btnDirection.setOnClickListener(v -> {
            info.dismiss();
            if(mLocation != null){
                origin = mLocation.latitude +", "+ mLocation.longitude;
                destination = marker.getPosition().latitude+ ", "+marker.getPosition().longitude;
                //tim duong
                sendRequest(origin, destination);
            }
        });

        btnInformation.setOnClickListener(v ->{
            info.dismiss();
            Intent intent = new Intent(this, DiaryCheckPointActivity.class);
            ArrayList<CheckPoint> passData = new ArrayList<>();
            passData.add(hashMapCheckPoint.get(marker.getTag()));
            intent.putParcelableArrayListExtra("checkpoint", passData);
            intent.putExtra("diaryId", diaryId);
            intent.putExtra("mode","update");

            if(shareMode.equals("share")){
                intent.putExtra("shareMode","share");
            }

            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgAddCheckPoint:
                Intent intent = new Intent(this, DiaryCheckPointActivity.class);
                intent.putExtra("diaryId", diaryId);
                intent.putExtra("mode","add");
                startActivity(intent);
                finish();
                break;
            case R.id.imgShareDiary:
                final Dialog info = new Dialog(this);

                //info.requestWindowFeature(Window.FEATURE_NO_TITLE); -- bo title cua dialog
                info.setContentView(R.layout.dialog_shared_diary);
                info.show();

                Button btnShare = info.findViewById(R.id.btnShare);
                EditText edtEmailShared = info.findViewById(R.id.edtEmailShared);

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.dismiss();
                        if(edtEmailShared.length() <= 0){
                            Toasty.info(getApplicationContext(), "Please enter the email will be share !!!").show();
                            return;
                        }
                        pDiary.receivedSharedDiary(user.getUid(), diaryId, edtEmailShared.getText().toString());
                    }
                });

                break;
        }
    }

    @Override
    public void sharedDiary(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    //ham tim duong
    private void sendRequest(String origin,String destination) {
        if (origin == "") {
            Toasty.info(this, "Can't get your locaion!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination == "") {
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        //originMarkers = new ArrayList<>();
        //destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            //((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                   // .icon(BitmapDescriptorFactory.fromResource(R.drawable.imagemenu))
//                    .title(route.startAddress)
//                    .position(myLocation)));
//            LatLng LatLgData = new LatLng(Double.parseDouble(tourist_location.Latitude),Double.parseDouble(tourist_location.Longtitude));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.imagemenu))
//                    .title(route.endAddress)
//                    .position(LatLgData)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

            Toasty.info(this, route.duration + " Km", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MyDiaryActivity.class);
        startActivity(intent);
        finish();
    }

}
