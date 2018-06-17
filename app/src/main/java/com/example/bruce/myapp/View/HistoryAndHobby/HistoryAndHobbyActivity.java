package com.example.bruce.myapp.View.HistoryAndHobby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.HistoryAdapter;
import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Model.MTeam;
import com.example.bruce.myapp.Presenter.HistoryAndHobby.PHistoryAndHobby;
import com.example.bruce.myapp.Presenter.MenuFragment.PMenuFragment;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.BigMap.BigMapsActivity;
import com.example.bruce.myapp.View.Information_And_Comments.InformationAndCommentsActivity;
import com.example.bruce.myapp.View.Login.LoginActivity;
import com.example.bruce.myapp.View.MenuFragment.IViewMenuFragment;
import com.example.bruce.myapp.View.Team.TeamActivity;
import com.example.bruce.myapp.View.TeamSupport.TeamSupportActivity;
import com.example.bruce.myapp.View.User.UserProfileActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class HistoryAndHobbyActivity extends AppCompatActivity implements IViewHistoryAndHobby,IViewMenuFragment,HistoryAdapter.RecyclerViewClicklistener {

    private PHistoryAndHobby presenterHistoryAndHobby = new PHistoryAndHobby(this);
    private PMenuFragment pMenuFragment = new PMenuFragment(this);

    ImageView imgFriendProfilePicture;
    TextView txtGreeting, txtEmail;

    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;

    RecyclerView recyclerViewHistory,recyclerViewRecommended,recyclerViewNearBy;
    StringBuilder hashCodeListHistory = new StringBuilder();
    StringBuilder hashCodeListRecommeded = new StringBuilder();
    StringBuilder hashCodeListLocationNearBy = new StringBuilder();
    ArrayList<Tourist_Location> listHistory;
    ArrayList<Tourist_Location> listRecommended;
    ArrayList<Tourist_Location> listLocationNearBy;
    HistoryAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 99;
    ListView listView;
    //gps
    GPSTracker gps = new GPSTracker(HistoryAndHobbyActivity.this);

    //biến lưu lịch sử
    String history = "";
    ArrayList<Tourist_Location> allLocation;
    UserProfile userProfile;

    String[] menuItem = {};
    private Dialog dialogCreateTeam ;
    private TextView txtTeamName;
    private Button btnOk,btnCancel;
    SpotsDialog loadDaTaDialog;

    private GeoFire geoFire;
    String idCaptain,emailCaptain,idUser;
    private DatabaseReference mDataTeamUser;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    String Language;

    MTeam mTeam = new MTeam();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_and_hobby);

        //lắng nghe ping từ team
        mTeam.teamPingListerner(getApplicationContext());

        //loadLanguage();
        //load data dialog
        loadDaTaDialog=new SpotsDialog(this);
        loadDaTaDialog.show();
//--------------------------------------------------------------------------------------------------------------------------
        //kiểm tra GPS có bật hay chưa trong Presenter
        presenterHistoryAndHobby.receivedEnableGPS(getApplicationContext(), this);
        //lấy dữ thông tin user từ firebase
        presenterHistoryAndHobby.receivedGetUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());

//---------------------------------------------------------------------------------------------------------------------------
        initialize();
        //giao dien profile của ng đăng nhập bằng facebook
        interfaceFacebookUser(user,imgFriendProfilePicture,txtEmail,txtGreeting);
        //hien thi nut memu (toggle)
        setUpToggle(mToggle,mDrawerLayout);
        //set up menu
        setUpListViewMenu(listView);


        FirebaseDatabase.getInstance().getReference("ListInviting").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists())
                {
                    //dataSnapshot.getKey()=idUser
                    //dataSnapshot.getKey()=id Đội Trưởng
                    idUser=dataSnapshot.getKey();
                    idCaptain =dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString();

                    FirebaseDatabase.getInstance().getReference("User").child(idCaptain).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            emailCaptain=dataSnapshot.getValue().toString();
                            diaLogInvite();
                            alertDialog.show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Language = Locale.getDefault().getDisplayLanguage().toString(); // lấy ngôn ngữ mặc định
        if(Language.equals("English")){
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        }else if(Language.equals("Tiếng Việt")){
            Toast.makeText(this, "Tiếng việt", Toast.LENGTH_SHORT).show();
        }
        FirebaseDatabase.getInstance().getReference("Help").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String nameNeedHelp=dataSnapshot.child("Name").getValue().toString();
                String key=dataSnapshot.getKey();
                FirebaseDatabase.getInstance().getReference("Help").child(dataSnapshot.getKey()).child("Team").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                            dataSnapshot.getRef().removeValue();
                            Log.d("sadsad",nameNeedHelp);
                            Intent intent = new Intent(getApplicationContext(), TeamSupportActivity.class);
                            intent.putExtra("NameNeedHelp",nameNeedHelp);
                            intent.putExtra("key",key);
                            intent.putExtra("userId",dataSnapshot.getKey());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("ádasds",dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void diaLogInvite() {
        builder=new AlertDialog.Builder(this);
        builder.setMessage(emailCaptain+ " mời bạn vào nhóm").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mDataTeamUser=FirebaseDatabase.getInstance().getReference("TeamUser");
                        geoFire=new GeoFire(mDataTeamUser.child(idCaptain).child("member"));
                        FirebaseDatabase.getInstance().getReference("CheckTeam").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Captain").setValue(idCaptain);
                        Toast.makeText(HistoryAndHobbyActivity.this, "Chúc mừng bạn đã gia nhập đội của :"+emailCaptain, Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("ListInviting").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(),new GeoLocation(0,0));
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setTitle("Thông báo");
    }


    private void interfaceFacebookUser(FirebaseUser user, ImageView imgFriendProfilePicture, TextView txtEmail, TextView txtGreeting){

        if (user != null) {
            //neu ko dang nhap bang facebook thi` ...
            Picasso.with(this).load(user.getPhotoUrl()).transform(new CircleTransform()).into(imgFriendProfilePicture);
            txtGreeting.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());

        } else {
            Toast.makeText(getApplicationContext(), "chua co user", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpToggle(ActionBarDrawerToggle mToggle,DrawerLayout mDrawerLayout){
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToggle = mToggle;
    }

    private void setUpListViewMenu(final ListView listView){
        presenterHistoryAndHobby.receivedTeamChecker(user.getUid(),menuItem,listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) listView.getItemAtPosition(position);
                if(value == getString(R.string.Logout))
                {
                    pMenuFragment.receivedLogout();
                }
                if(value == getString(R.string.Map)){
                    Intent target = new Intent(HistoryAndHobbyActivity.this, BigMapsActivity.class);
                    target.putParcelableArrayListExtra("allLocation",allLocation);
                    startActivity(target);
                }
                if(value == getString(R.string.MyProfile)){
                    Intent target = new Intent(HistoryAndHobbyActivity.this, UserProfileActivity.class);
                    ArrayList<UserProfile> listUser=new ArrayList<>();
                    listUser.add(userProfile);
                    target.putParcelableArrayListExtra("user",listUser);
                    startActivity(target);
                }
                if(value==getString(R.string.Team))
                {
                    Intent target = new Intent(HistoryAndHobbyActivity.this, TeamActivity.class);
                    startActivity(target);
                }
                if(value==getString(R.string.CreateTeam))
                {
                    dialogCreateTeam = new Dialog(HistoryAndHobbyActivity.this);
                    dialogCreateTeam.setContentView(R.layout.dialog_create_team);
                    txtTeamName=dialogCreateTeam.findViewById(R.id.txtTeamName);
                    btnOk=dialogCreateTeam.findViewById(R.id.btnOk);
                    btnCancel=dialogCreateTeam.findViewById(R.id.btnCancel);
                    btnOk.setOnClickListener(v -> {
                        String TeamName=txtTeamName.getText().toString();

                        FirebaseDatabase.getInstance().getReference("CheckTeam").child(user.getUid()).child("Captain").setValue(user.getUid());
                        FirebaseDatabase.getInstance().getReference("TeamUser").child(user.getUid()).child("TeamName").setValue(TeamName);


                        mDataTeamUser=FirebaseDatabase.getInstance().getReference("TeamUser");
                        geoFire=new GeoFire(mDataTeamUser.child(user.getUid()).child("member"));
                        geoFire.setLocation(user.getUid(),new GeoLocation(0,0));
                        dialogCreateTeam.dismiss();
                    });
                    dialogCreateTeam.show();
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCreateTeam.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void initialize() {


        imgFriendProfilePicture = findViewById(R.id.imgUser);
        txtGreeting = findViewById(R.id.txtDisplayName);
        txtEmail = findViewById(R.id.txtEmail);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        recyclerViewHistory = findViewById(R.id.recyclerView_History);
        recyclerViewRecommended = findViewById(R.id.recyclerView_Recommended);
        recyclerViewNearBy = findViewById(R.id.recyclerView_NearBy);
        listView = findViewById(R.id.Menu);
    }

    private void setUpRecyclerView(RecyclerView recyclerView, HistoryAdapter adapter, ArrayList<Tourist_Location> listHistoryLocation) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HistoryAdapter(listHistoryLocation, this);
        this.adapter = adapter;
        adapter.setClickListenerRecyclerView(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnchangeLanguage) {
            // Hiện thông báo dialog chuyển đổi ngôn ngữ
            Dialog dialog=new Dialog(HistoryAndHobbyActivity.this);
            dialog.setContentView(R.layout.dialog_language);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            TextView txtVietnamese=dialog.findViewById(R.id.txtVietnamese);
            TextView txtEnglish=dialog.findViewById(R.id.txtEnglish);
            ImageButton btnVietnamese=dialog.findViewById(R.id.ImgBtnVietnamese);
            ImageButton btnEnglish=dialog.findViewById(R.id.ImgbtnEnglish);
            btnVietnamese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Locale locale=new Locale("vi");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    startActivity(getIntent());

                }
            });
            btnEnglish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Locale locale=new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    startActivity(getIntent());
                }
            });

            return true;
        }
        else if(id==R.id.btnlogout){

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {

        super.onAttachFragment(fragment);

    }

    @Override
    public void EnableGPS_API22() {
        gps.getLocation();
        if (!gps.canGetLocation()) {
            gps.showSettingAlert();
        }
    }

    @Override
    public void EnableGPS_APILater22() {
        gps.getLocation();
        if (!gps.canGetLocation()) {
            gps.showSettingAlert();
        }

        int accessCoarsePermission
                = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission
                = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

            // Các quyền cần người dùng cho phép.
            String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION};

            // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
        }
    }
    @Override
    public String GetUserHistory(String history) {
        //lấy dữ liệu các địa điểm
        this.history = history;
        presenterHistoryAndHobby.receivedGetLocationData( HistoryAndHobbyActivity.this);
        return null;

    }

    @Override
    public String getUserBehavior(String behavior) {
        presenterHistoryAndHobby.receivedRecommendeToUser(behavior,history,allLocation);
        return null;
    }

    @Override
    public UserProfile UserProfile(UserProfile User) {
        return this.userProfile = User;
    }

    @Override
    public ArrayList<Tourist_Location> GetLocationData(ArrayList<Tourist_Location> tourist_locations) {
        loadDaTaDialog.dismiss();
        //lấy dữ liệu của tất cả các địa điểm để truyền qua activity khác
        allLocation = tourist_locations;

//---------------------------------------------------------------------------------------------------------
        presenterHistoryAndHobby.receivedGetUserHistoryLocation(history, tourist_locations);
        //lấy dữ liệu thông tin hanh vi của user
        presenterHistoryAndHobby.receivedGetUsetBehavior(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //presenterHistoryAndHobby.receivedLocationNearByList(new LatLng(gps.getLatitude(),gps.getLongtitude()),tourist_locations);
        return null;
    }

    @Override
    public ArrayList<Tourist_Location> GetUserHistoryLocation(ArrayList<Tourist_Location> tourist_locations) {
        setUpRecyclerView(recyclerViewHistory,adapter,tourist_locations);
        hashCodeListHistory.append(tourist_locations.hashCode());
        listHistory = tourist_locations;
        return null;
    }

    @Override
    public ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations) {
        setUpRecyclerView(recyclerViewRecommended,adapter,tourist_locations);
        hashCodeListRecommeded.append(tourist_locations.hashCode());
        listRecommended = tourist_locations;
        return null;
    }

    @Override
    public void returnLocationNearByList(ArrayList<Tourist_Location> touristLocations) {
        setUpRecyclerView(recyclerViewNearBy,adapter,touristLocations);
        hashCodeListLocationNearBy.append(touristLocations.hashCode());
        listLocationNearBy = touristLocations;
    }

    @Override
    public void HasTeam(String[] menuItem, ListView listView) {
        menuItem = new String[]{getString(R.string.MyProfile),
                getString(R.string.Map),
                getString(R.string.Logout),
                getString(R.string.Team)};
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                menuItem);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void HasNoTeam(String[] menuItem, ListView listView) {
        menuItem = new String[]{getString(R.string.MyProfile),
                getString(R.string.Map),
                getString(R.string.Logout),
                getString(R.string.CreateTeam)};
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                menuItem);
        listView.setAdapter(listViewAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(getIntent());
    }


    @Override
    public void LogoutSuccess() {
        Intent target = new Intent(this, LoginActivity.class);
        finish();
        startActivity(target);
    }
    private void setUpDialog(Dialog info ,Tourist_Location tl){
        info = new Dialog(HistoryAndHobbyActivity.this);
        //info.requestWindowFeature(Window.FEATURE_NO_TITLE); -- bo title cua dialog
        info.setContentView(R.layout.dialog_bigmap);
        info.setTitle("Choose what you want !");
        info.show();
        Button btnDirection = info.findViewById(R.id.btnDirection);
        Button btnInformation = info.findViewById(R.id.btnInformation);
        btnDirection.setOnClickListener( v -> {

            Intent target = new Intent(HistoryAndHobbyActivity.this, BigMapsActivity.class);
            target.putParcelableArrayListExtra("allLocation",allLocation);
            target.putExtra("destination",tl.getLatitude() + ", " + tl.getLongtitude());
            startActivity(target);
        });
        btnInformation.setOnClickListener(v->{
            ArrayList<Tourist_Location> tls = new ArrayList<>();
            tls.add(tl);
            Intent target = new Intent(HistoryAndHobbyActivity.this, InformationAndCommentsActivity.class);
            target.putParcelableArrayListExtra("tourist_location",tls);
            startActivity(target);
        });
    }
    @Override
    public void onClickItemRecyclerView(View view, int position, String listId) {

        if(listId.equals(hashCodeListHistory.toString())){
            Tourist_Location tl = listHistory.get(position);
            final Dialog info = new Dialog(HistoryAndHobbyActivity.this);
            setUpDialog(info,tl);
        }
        if(listId.equals(hashCodeListRecommeded.toString()))
        {
            Tourist_Location tl = listRecommended.get(position);
            final Dialog info = new Dialog(HistoryAndHobbyActivity.this);
            setUpDialog(info,tl);
        }
        if(listId.equals(hashCodeListLocationNearBy.toString())){
            Tourist_Location tl = listLocationNearBy.get(position);
            final Dialog info = new Dialog(HistoryAndHobbyActivity.this);
            setUpDialog(info,tl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


