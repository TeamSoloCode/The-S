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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.Comment_Image_Adapter;
import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.ApiPostObject.PostComment;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Write_Comment_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtComment;
    private ImageButton btnPost,btnPostImage;
    private RecyclerView recyclerView;

    private Comment_Image_Adapter adapterImage;
    private PostComment myComment;
    private DatabaseReference mData;
    private DatabaseReference Comment;
    private FirebaseUser user;
    private int PICK_IMAGE_MULTIPLE = 1;
    private ArrayList<String> imagesPost;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<TouristLocation> tls;

    private Uri filepath;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        storage= FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        initialize();
        getDataFromIntent();

        imagesPost = new ArrayList<>();

        recyclerView.setVisibility(View.GONE);



        setUpRecyclerViewPostImage();

        addImage(btnPostImage);
    }

    private void initialize(){
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnPost = (ImageButton) findViewById(R.id.btnPost);
        recyclerView = (RecyclerView) findViewById(R.id.listImagePost);

        btnPostImage = (ImageButton) findViewById(R.id.btnPostImage);
        btnPost.setOnClickListener(this);
    }

    private void getDataFromIntent(){
        tls = getIntent().getParcelableArrayListExtra("tourist_location");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:
                myComment = new PostComment();
                if(edtComment.length() != 0){
                    myComment.setComment(edtComment.getText().toString());
                    myComment.setListImage(imagesPost);
                }

                //chuyển object lại thành json
                Gson gson = new Gson();
                String jsonComment = gson.toJson(myComment);

                //goi api
                Retrofit retrofit = ApiClient.getApiClient();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                apiInterface.postComment(tls.get(0).getLocationId(),user.getUid(), jsonComment).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResultCode() == 2){
                                Toasty.success(getApplicationContext(), response.body().getResultMessage().toString() , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toasty.error(getApplicationContext(), response.body().getResultMessage().toString() , Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toasty.error(getApplicationContext(), "Can not connect to server" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "Can not connect to server" , Toast.LENGTH_SHORT).show();
                    }
                });

                //upload();
                finish();
                break;
        }
    }

    private void setUpRecyclerViewPostImage(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapterImage = new Comment_Image_Adapter(getApplicationContext(),imagesPost);
        recyclerView.setAdapter(adapterImage);
    }

    private void upload() {
        if(filepath!=null) {

               for(int i = 0; i < mArrayUri.size(); i++){
                   Uri fipath = mArrayUri.get(i);
                   final StorageReference ref = mStorageRef.child("image/" + UUID.randomUUID().toString());
                   ref.putFile(fipath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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
//                          imageHinh=new ImageHinh(mArrayUri.get(i).toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
