package com.example.bruce.myapp.View.DiaryCheckPoint;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.Comment_Image_Adapter;
import com.example.bruce.myapp.AddMissingLocation;
import com.example.bruce.myapp.Data.CheckPoint;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Presenter.DiaryCheckPoint.PDiaryCheckPoint;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.Utility;
import com.example.bruce.myapp.View.Diary.DiaryActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import es.dmoral.toasty.Toasty;

public class DiaryCheckPointActivity extends AppCompatActivity implements View.OnClickListener,IViewDiaryCheckPoint {

    private Button btnSaveChanges, btnAddImage;
    private EditText edtDiscription;
    private RecyclerView recyclerViewCheckPointImages;
    private Comment_Image_Adapter checkPointAdapter;
    private ArrayList<String> imageStorageRef;

    private FirebaseUser user;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    private PDiaryCheckPoint pDiaryCheckPoint;
    private String diaryId;
    private CheckPoint checkPoint;
    private String mode;
    private GPSTracker gpsTracker;


    private int PICK_IMAGE_REQUEST = 1;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 555, SELECT_FILE = 666;
    Uri filePath;
    ArrayList<Uri> imageUri;
    ArrayList<String> imagesPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_check_point);

        imageStorageRef = new ArrayList<>();
        imageUri=new ArrayList<>();
        imagesPost=new ArrayList<>();

        initialize();
        getDataFromIntent();
        if(mode.equals("add")){
            btnSaveChanges.setText("Add check point");
        }
        else{
            renderData(checkPoint);
        }

        pDiaryCheckPoint = new PDiaryCheckPoint(this);
        btnSaveChanges.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        gpsTracker = new GPSTracker(this);
    }

    private void initialize(){
        btnAddImage = findViewById(R.id.btnAddCheckPointsPic);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        edtDiscription = findViewById(R.id.edtCheckPointDiscription);
        recyclerViewCheckPointImages = findViewById(R.id.recyclerView_CheckPointImage);
    }

    private void getDataFromIntent(){
        diaryId = getIntent().getStringExtra("diaryId");
        mode = getIntent().getStringExtra("mode");
        ArrayList<CheckPoint> passData = getIntent().getParcelableArrayListExtra("checkpoint");
        if(passData != null){
            checkPoint = passData.get(0);
            edtDiscription.setText(checkPoint.getDescription());
        }else{
            checkPoint = new CheckPoint();
            checkPoint.setImages(new ArrayList<>());
            renderData(checkPoint);
        }
    }

    public void renderData(CheckPoint checkPoint){
        LinearLayoutManager layoutManager = new LinearLayoutManager(DiaryCheckPointActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewCheckPointImages.setLayoutManager(layoutManager);
        imagesPost=checkPoint.getImages();
        checkPointAdapter = new Comment_Image_Adapter(getApplicationContext(), imagesPost);
        recyclerViewCheckPointImages.setAdapter(checkPointAdapter);
        //checkPointAdapter.setClickListenerRecyclerView(this);
        checkPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveChanges:
                if(mode.equals("add")){
                    if(edtDiscription.length() > 0){
                        checkPoint.setDescription(edtDiscription.getText().toString());
                    }
                    if(gpsTracker.canGetLocation()){
                        checkPoint.setLat(gpsTracker.getLatitude());
                        checkPoint.setLog(gpsTracker.getLongtitude());
                    }
                    else {
                        gpsTracker.showSettingAlert();
                        return;
                    }
                    checkPoint.setDeletePlag(false);
                    checkPoint.setImages(checkPoint.getImages());
                    checkPoint.setCreateDate(System.currentTimeMillis());

                    if(edtDiscription.length() == 0){
                        Toasty.info(this,"Discription can not be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    pDiaryCheckPoint.receivedAddNewCheckPoint(
                            user.getUid(),
                            diaryId,
                            checkPoint);
                }
                else if(mode.equals("update")){
                    if(edtDiscription.length() == 0){
                        Toasty.info(DiaryCheckPointActivity.this,"Discription can not be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        upload();
                    }

                }
                break;
            case R.id.btnAddCheckPointsPic:
                selectImage();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                // Show only images, no videos or anything else
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setType("image/*");
//                // Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(DiaryCheckPointActivity.this);
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                checkPointAdapter.notifyDataSetChanged();
            }
            else if (requestCode == REQUEST_CAMERA)
            {
                onCaptureImageResult(data);
                checkPointAdapter.notifyDataSetChanged();
            }
        }
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            storageRef = storage.getReference();
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                String imgRef = "Image/CheckPoint/"+user.getEmail()+"/"+diaryId+"/"+System.currentTimeMillis();
//                StorageReference userImageRef = storageRef.child(imgRef);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//                byte[] data1 = baos.toByteArray();
//
//                UploadTask uploadTask = userImageRef.putBytes(data1);
//
//                if(checkPoint != null && checkPoint.getImages() != null){
//                    if(checkPoint.getImages().size() <= 19){
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                                if(taskSnapshot.getDownloadUrl() != null){
//                                    checkPoint.getImages().add(taskSnapshot.getDownloadUrl().toString());
//                                    checkPointAdapter.notifyDataSetChanged();
//                                    imageStorageRef.add(imgRef);
//                                }
//                            }
//                        });
//                    }
//                    else {
//                        Toasty.warning(this, "Can't add more than 20 images in a check point",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                            if(taskSnapshot.getDownloadUrl() != null){
//                                checkPoint.getImages().add(taskSnapshot.getDownloadUrl().toString());
//                                checkPointAdapter.notifyDataSetChanged();
//                                imageStorageRef.add(imgRef);
//                            }
//                        }
//                    });
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void addDiaryCheckPoint(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        imageStorageRef.clear();
        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDiaryCheckPoint(int resultCode, String resultMessage) {
        if(resultCode != 2){
            Toasty.error(this, resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        imageStorageRef.clear();
        Toasty.success(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("diaryId", diaryId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(imageStorageRef.size() > 0){
            for(String imageUrl : imageStorageRef){
                storageRef.child(imageUrl).delete();
            }
        }
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
            imageUri.add(data.getData());
        }
        else{
            ClipData mClipData=data.getClipData();
            for(int i = 0 ;i < mClipData.getItemCount(); i++){
                ClipData.Item item = mClipData.getItemAt(i);
                filePath = item.getUri();
                if(!imagesPost.contains(filePath.toString())) {
                    imagesPost.add(filePath.toString());
                    //Uri tra? len firebase
                    imageUri.add(filePath);
                }
            }
        }
        checkPointAdapter.notifyDataSetChanged();
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), thumbnail, "Title", null);
        imagesPost.add(Uri.parse(path).toString());
        imageUri.add(Uri.parse(path));
        //code add hinh len firebase o day
        checkPointAdapter.notifyDataSetChanged();
    }
    private void upload() {
        if(imageUri!=null) {
            storageRef = storage.getReference();
            for(int i=0;i<imageUri.size();i++){
                Uri fipath=imageUri.get(i);
                String imgRef = "Image/CheckPoint/"+user.getEmail()+"/"+diaryId+"/"+System.currentTimeMillis();
                StorageReference userImageRef = storageRef.child(imgRef);
                userImageRef.putFile(fipath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        checkPoint.setDescription(edtDiscription.getText().toString());
                        checkPoint.getImages().add(taskSnapshot.getDownloadUrl().toString());
                        pDiaryCheckPoint.receivedUpdateNewCheckPoint(
                                user.getUid(),
                                diaryId,
                                checkPoint);
                        //mData.child("Img_Comment").child(key).push().setValue(taskSnapshot.getDownloadUrl().toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }else {
            Toast.makeText(this, "filepath is null", Toast.LENGTH_SHORT).show();
        }

    }

}
