package com.example.bruce.myapp.View.Write_Comment;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.bruce.myapp.Data.Comment;
import com.example.bruce.myapp.Adapter.Comment_Image_Adapter;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Write_Comment_Activity extends AppCompatActivity {

    EditText edtComment;
    ImageButton btnPost,btnPostImage;
    RecyclerView recyclerView;

    Comment_Image_Adapter adapterImage;
    Comment myComment;
    DatabaseReference mData;
    DatabaseReference Comment;
    FirebaseAuth firebaseAuth;
    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<String> imagesPost;
    ArrayList<Uri> mArrayUri;

    Uri filepath;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        storage= FirebaseStorage.getInstance();
        mStorageRef=storage.getReference();

        initialize();
        imagesPost = new ArrayList<>();

        recyclerView.setVisibility(View.GONE);

        mData = FirebaseDatabase.getInstance().getReference();

        firebaseAuth= FirebaseAuth.getInstance();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapterImage=new Comment_Image_Adapter(getApplicationContext(),imagesPost);

        recyclerView.setAdapter(adapterImage);
        addImage(btnPostImage);
        Post(btnPost,edtComment);


    }

    private void Post(ImageButton btnPost, final EditText edtComment){
        ArrayList<Tourist_Location> tls;
        tls = getIntent().getParcelableArrayListExtra("tourist_location");

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtComment.length() == 0){

                }

                else{
                    myComment = new Comment();
                    myComment.userID = firebaseAuth.getCurrentUser().getUid();
                    myComment.locationID = tls.get(0).location_ID;
                    myComment.comment = edtComment.getText().toString();

                    //lấy ngày hệ thống đổi ra kiểu String
                    Date today = new Date(System.currentTimeMillis());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
                    String date = timeFormat.format(today.getTime());
                    myComment.date = date;

                    myComment.userName = firebaseAuth.getCurrentUser().getDisplayName();

                     key = mData.child("Comments").push().getKey();
                    myComment.commentID=key;
                    Comment   = mData.child("Comments").child(key);
                    Comment.setValue(myComment);

                    Log.d("sad",key);
                    upload();
                    //onBackPressed();
                    finish();
                }
            }
        });
    }

    private void upload() {
        if(filepath!=null) {

               for(int i=0;i<mArrayUri.size();i++){
                   Uri fipath=mArrayUri.get(i);
                   final StorageReference ref = mStorageRef.child("image/" + UUID.randomUUID().toString());
                   ref.putFile(fipath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Log.v("successtoast",taskSnapshot.getDownloadUrl().toString());

                           mData.child("Img_Comment").child(key).push().setValue(taskSnapshot.getDownloadUrl().toString());
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

    private void addImage(ImageButton btnPostImage) {
        btnPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){

            if(resultCode==RESULT_OK){

                if(data.getData()!=null){

                    filepath=data.getData();

                }else{
                    if(data.getClipData()!=null){
                        ClipData mClipData=data.getClipData();
                         mArrayUri = new ArrayList<Uri>();
                        for(int i = 0 ;i < mClipData.getItemCount(); i++){

                            ClipData.Item item = mClipData.getItemAt(i);

                            filepath = item.getUri();
                            mArrayUri.add(filepath);
                            String path= mArrayUri.get(i).toString();
//                             imageHinh=new ImageHinh(mArrayUri.get(i).toString());
                            imagesPost.add(path);


                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),filepath);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                byte[] data1 = baos.toByteArray();




//                                StorageReference mountainsRef =mStorageRef .child("image_cmt_"+imagesPost.get(i).toString());
//                                UploadTask uploadTask = mountainsRef.putBytes(data1);
//
//                                uploadTask.addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//                                        // Handle unsuccessful uploads
//                                        Toast.makeText(Write_Comment_Activity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                        Toast.makeText(Write_Comment_Activity.this, "Upload Done!", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                        recyclerView.setAdapter(adapterImage);
                    }

                }

            }

        }

        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initialize(){
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnPost = (ImageButton) findViewById(R.id.btnPost);
        recyclerView = (RecyclerView) findViewById(R.id.listImagePost);
        btnPostImage = (ImageButton) findViewById(R.id.btnPostImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
