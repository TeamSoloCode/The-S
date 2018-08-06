package com.example.bruce.myapp.View.Information_And_Comments;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.Comment_Image_Adapter;
import com.example.bruce.myapp.Adapter.SectionsPageAdapter;
import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.ApiPostObject.PostComment;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Comment_Fragment.Comment_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.Information_Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hsalf.smilerating.SmileRating;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InformationAndCommentsActivity extends AppCompatActivity {

    //passing data
    public String TAG = "TAG";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPage;
    private TextView txtTitle;
    private RatingBar ratingBar;
    private FloatingActionButton fabComment;
    private TabLayout tabLayout;
    private Button btnRate;
    public int costRate = 0;
    Uri filepath;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Dialog dialogComment;
    ArrayList<Uri> mArrayUri;
    ArrayList<TouristLocation> tls;
    ArrayList<String> imagePost;
    Boolean isShowingDialog = false;
    Comment_Image_Adapter adapterImage;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_and_comments);

        initialize();
        getDataFromIntent();

        setupTitle(txtTitle);
        setupViewPager(mViewPage);
        setupTabLayout(tabLayout, mViewPage);

        imagePost = new ArrayList<>();

        setDialogRate(btnRate, InformationAndCommentsActivity.this);

        onClickFabComment();
    }

    private void getDataFromIntent(){
        tls = getIntent().getParcelableArrayListExtra("tourist_location");

        //set up rating bar
        ratingBar.setRating(tls.get(0).getStars());
    }

    /**
     * Event click ò Comment button
     *
     * @param
     */
    private void onClickFabComment() {
        fabComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialogComment = new Dialog(InformationAndCommentsActivity.this);
                dialogComment.setContentView(R.layout.dialog_comment);
                dialogComment.show();

                EditText edtComment = dialogComment.findViewById(R.id.edtComment);
                Button btnComent = dialogComment.findViewById(R.id.btnPost);
                Button btnImagePost = dialogComment.findViewById(R.id.btnimagepost);
                recyclerView = dialogComment.findViewById(R.id.listImagePost);


                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(InformationAndCommentsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                ArrayList<String> image = new ArrayList<>();

                btnImagePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                    }
                });
                btnComent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostComment myComment = new PostComment();
                        if(edtComment.length() != 0){
                            myComment.setComment(edtComment.getText().toString());
                            //set hinh cho comment
                            //myComment.setListImage(imagesPost);
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
                    }
                });

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {

            if (data.getData() != null) {

                if (data.getClipData() != null) {

                    filepath = data.getData();
                    ClipData mClipData = data.getClipData();
                    mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        filepath = item.getUri();
                        mArrayUri.add(filepath);
                        String path = mArrayUri.get(i).toString();
                        imagePost.add(path);
                    }
                    adapterImage = new Comment_Image_Adapter(InformationAndCommentsActivity.this, imagePost);
                    recyclerView.setAdapter(adapterImage);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapterImage.notifyDataSetChanged();
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);

                            filepath = item.getUri();
                            mArrayUri.add(filepath);
                            String path = mArrayUri.get(i).toString();
                            imagePost.add(path);

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
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


                    }
                }

            }

        }

    }

    private void upload() {
        if (filepath != null) {

            for (int i = 0; i < mArrayUri.size(); i++) {
                Uri fipath = mArrayUri.get(i);
                final StorageReference ref = mStorageRef.child("image/" + UUID.randomUUID().toString());
                ref.putFile(fipath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Log.v("successtoast",taskSnapshot.getDownloadUrl().toString());

                        //mData.child("Img_Comment").child(key).push().setValue(taskSnapshot.getDownloadUrl().toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        } else {
            Toast.makeText(this, "filepath is null", Toast.LENGTH_SHORT).show();
        }

    }

    private void initialize() {

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPage = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        txtTitle = findViewById(R.id.title);
        ratingBar = findViewById(R.id.ratingBar_Main);
        fabComment = findViewById(R.id.floatingActionButton_Comment);
        btnRate = findViewById(R.id.btnrated);

    }

    /**
     * Hàm để post đánh giá của user lên server
     *
     * @param userId
     * @param ratingValue
     * @param locationId
     */
    private void postUsersRate(String locationId, String userId, float ratingValue, Dialog dialog) {
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.postRating(locationId, userId, ratingValue).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 2) {
                        Toasty.success(InformationAndCommentsActivity.this,
                                response.body().getResultMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(InformationAndCommentsActivity.this,
                            "Không thể kết nối đến server",
                            Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toasty.error(InformationAndCommentsActivity.this,
                        "Không thể kết nối đến server",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Chọn mức đánh giá
     *
     * @param btnrate
     * @param context
     */
    public void setDialogRate(Button btnrate, Context context) {
        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(InformationAndCommentsActivity.this);
                dialog.setContentView(R.layout.dialog_rating);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
                Button btnRating = dialog.findViewById(R.id.btnrate);
                Button btncancel = dialog.findViewById(R.id.btncancel);

                smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                    @Override
                    public void onSmileySelected(int smiley, boolean reselected) {
                        switch (smiley) {
                            case SmileRating.BAD:
                                Log.i(TAG, "Bad");
                                costRate = smileRating.getRating();
                                Log.i(TAG, String.valueOf(costRate));
                                break;
                            case SmileRating.GOOD:
                                Log.i(TAG, "Good");
                                costRate = smileRating.getRating();
                                Log.i(TAG, String.valueOf(costRate));
                                break;
                            case SmileRating.GREAT:
                                Log.i(TAG, "Great");
                                costRate = smileRating.getRating();
                                Log.i(TAG, String.valueOf(costRate));
                                break;
                            case SmileRating.OKAY:
                                Log.i(TAG, "Okay");
                                costRate = smileRating.getRating();
                                Log.i(TAG, String.valueOf(costRate));
                                break;
                            case SmileRating.TERRIBLE:
                                Log.i(TAG, "Terrible");
                                costRate = smileRating.getRating();
                                Log.i(TAG, String.valueOf(costRate));
                                break;
                        }
                    }
                });

                btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postUsersRate(tls.get(0).getLocationId(),
                                user.getUid(), costRate, dialog);
                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setupTitle(TextView txtTitle) {
        txtTitle.setMaxLines(1);
        //chấm 3 chấm nếu tiêu đề quá dài
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setText(tls.get(0).getName());
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Information_Fragment(), "Infomation");
        adapter.addFragment(new Comment_Fragment(), "Comments");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout(TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setupWithViewPager(mViewPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
