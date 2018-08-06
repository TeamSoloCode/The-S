package com.example.bruce.myapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruce.myapp.Adapter.AddMissingLocationAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import es.dmoral.toasty.Toasty;


public class AddMissingLocation extends AppCompatActivity implements AddMissingLocationAdapter.RecyclerViewClicklistener {
    Button btnAddImage;
    private int REQUEST_CAMERA = 777, SELECT_FILE = 888;
    private String userChoosenTask;
    ImageView imga;
    RecyclerView rvAddImg;
    AddMissingLocationAdapter adapterImage;
    ArrayList<String> imagesPost;
    ArrayList<Uri> imageUri;
    Uri filePath;
    String address,log,lat;
    EditText edtAddress,edtPlaceName,edtKind,edtPhone,edtDescribe;
    ArrayList<String> arrImg;
    boolean checkname=false,checkAdress=false,checkCategory =false;
    StorageReference mStorageRef;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_missing_location);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a place");
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        storage= FirebaseStorage.getInstance();
        mStorageRef=storage.getReference();
        arrImg=new ArrayList<>();
        checkError();
        rvAddImg = findViewById(R.id.rvAddMissingLocation);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rvAddImg.setLayoutManager(layoutManager);
        imagesPost=new ArrayList<>();
        imageUri=new ArrayList<>();
        adapterImage=new AddMissingLocationAdapter(getApplicationContext(),imagesPost);
        adapterImage.setClickListener(this);
    }

    private void checkError() {
        edtPlaceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPlaceName.getText().toString().length() <= 0) {
                    edtPlaceName.setError(getString(R.string.MessageNameNull));
                    checkname=false;
                } else {
                    edtPlaceName.setError(null);
                    checkname=true;
                }
                invalidateOptionsMenu();
            }
        });
        edtKind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtKind.getText().toString().length() <= 0) {
                    edtKind.setError(getString(R.string.MessageKindgoryNull));
                    checkCategory=false;
                } else {
                    edtKind.setError(null);
                    checkCategory=true;
                }
                invalidateOptionsMenu();
            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtAddress.getText().toString().length() <= 0) {
                    edtAddress.setError(getString(R.string.MessageAddressNull));
                    checkAdress=false;
                }
                 else if(address==null){
                    edtAddress.setError(getString(R.string.MessageCoordinatesNull));
                    checkAdress=false;
                }
                else {
                    edtAddress.setError(null);
                    checkAdress=true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    private void initialize() {
        btnAddImage = findViewById(R.id.btnAddImage);
        imga = findViewById(R.id.imga);
        edtAddress = findViewById(R.id.edtAddress);
        edtPlaceName = findViewById(R.id.edtPlaceName);
        edtDescribe = findViewById(R.id.edtDescribe);
        edtKind = findViewById(R.id.edtKind);
        edtPhone = findViewById(R.id.edtPhone);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected  void onActivityResult(int req, int res, Intent data){
        if  ((req!=REQUEST_CAMERA && req!=SELECT_FILE) )
        {
            android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.places);
            fragment.onActivityResult(1, res, data);

        }
        else if (res == Activity.RESULT_OK) {
            if (req == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                rvAddImg.setAdapter(adapterImage);
            }
            else if (req == REQUEST_CAMERA)
            {
                onCaptureImageResult(data);
                rvAddImg.setAdapter(adapterImage);
            }
        }

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMissingLocation.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddMissingLocation.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if(data.getClipData()==null)
        {
            imagesPost.add(data.getData().toString());
        }
        else{
            ClipData mClipData=data.getClipData();
            for(int i = 0 ;i < mClipData.getItemCount(); i++){
                ClipData.Item item = mClipData.getItemAt(i);
                filePath = item.getUri();
                if(!imagesPost.contains(filePath.toString())) {
                    imagesPost.add(filePath.toString());
                    imageUri.add(filePath);
                }
            }
        }
        adapterImage.notifyDataSetChanged();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), thumbnail, "Title", null);
        imagesPost.add(Uri.parse(path).toString());
        //code add hinh len firebase o day
        rvAddImg.setAdapter(adapterImage);
    }

    @Override
    public void itemClickRemoveImg(View view, int position) {
        imagesPost.remove(position);
        imageUri.remove(position);
        rvAddImg.setAdapter(adapterImage);
    }

    void getPlace(Place place){
        address=place.getAddress().toString();
        String latlog=place.getLatLng().toString().split("[(]")[1];
        edtAddress.setText(address);
        lat=latlog.split(",")[0];
        log=latlog.split(",")[1].substring(0,latlog.split(",")[1].length()-1);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_place,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(checkname==true && checkAdress==true && checkCategory==true)
        {
            menu.getItem(0).setEnabled(true);
        }
        else
        {
            menu.getItem(0).setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save_place){
            //xu ly tra du lieu len web gom address,log,lat, name ,edtPlaceName.txt,edtKind.txt,edtPhone.txt,edtDescribe.txt,   images
            Toasty.success(this, getString(R.string.MessageAddLocationSuccess), Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //chay lai menu
    @Override
    public void invalidateOptionsMenu() {

        super.invalidateOptionsMenu();
    }
}
