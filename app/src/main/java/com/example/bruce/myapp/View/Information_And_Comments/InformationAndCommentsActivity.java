package com.example.bruce.myapp.View.Information_And_Comments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.SectionsPageAdapter;
import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiCommonResponse.CommonResponse;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Comment_Fragment.Comment_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.Information_Fragment;
import com.example.bruce.myapp.View.Write_Comment.Write_Comment_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InformationAndCommentsActivity extends AppCompatActivity {

    //passing data
    ArrayList<TouristLocation> tls = new ArrayList<>();
    public String TAG ="TAG";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPage;
    private TextView txtTitle;
    private RatingBar ratingBar;
    private FloatingActionButton fabComment;
    private TabLayout tabLayout;
    private Button btnRate;
    public int costRate=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_and_comments);

        initialize();
        tls = getIntent().getParcelableArrayListExtra("tourist_location");
        setupTitle(txtTitle);
        setupViewPager(mViewPage);
        setupTabLayout(tabLayout,mViewPage);

        //set up rating bar
        ratingBar.setRating(tls.get(0).getStars());
        setDialogRate(btnRate,InformationAndCommentsActivity.this);
        onClickFabComment();
    }

    /**
     * Event click ò Comment button
     */
    private void onClickFabComment() {
        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Write_Comment_Activity.class);
                intent.putParcelableArrayListExtra("tourist_location",tls);
                startActivity(intent);
            }
        });
    }

    private void initialize(){

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPage = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        txtTitle = findViewById(R.id.title);
        ratingBar = findViewById(R.id.ratingBar_Main);
        fabComment = findViewById(R.id.floatingActionButton_Comment);
        btnRate=findViewById(R.id.btnrated);

    }

    /**
     * Hàm để post đánh giá của user lên server
     * @param userId
     * @param ratingValue
     * @param locationId
     */
    private void postUsersRate(String locationId, String userId, float ratingValue, Dialog dialog){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.postRating(locationId, userId, ratingValue).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResultCode() == 1){
                        Toast.makeText(getApplicationContext(), response.body().getResultData().toString(), Toast.LENGTH_SHORT);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), response.body().getResultData().toString(), Toast.LENGTH_SHORT);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Không thể kết nối đến server", Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể kết nối đến server", Toast.LENGTH_SHORT);
            }
        });
    }
    /**
     * Chọn mức đánh giá
     * @param btnrate
     * @param context
     */
    public void setDialogRate(Button btnrate, Context context){
        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(InformationAndCommentsActivity.this);
                dialog.setContentView(R.layout.dialog_rating);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                SmileRating smileRating=dialog.findViewById(R.id.smile_rating);
                Button btnRating=dialog.findViewById(R.id.btnrate);
                Button btncancel=dialog.findViewById(R.id.btncancel);

                smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                    @Override
                    public void onSmileySelected(int smiley, boolean reselected) {
                        switch (smiley) {
                            case SmileRating.BAD:
                                Log.i(TAG, "Bad");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.GOOD:
                                Log.i(TAG, "Good");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.GREAT:
                                Log.i(TAG, "Great");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.OKAY:
                                Log.i(TAG, "Okay");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.TERRIBLE:
                                Log.i(TAG, "Terrible");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                        }
                    }
                });

                btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postUsersRate(tls.get(0).getLocationId(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), costRate, dialog);
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

    private void setupTitle(TextView txtTitle){
        txtTitle.setMaxLines(1);
        //chấm 3 chấm nếu tiêu đề quá dài
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        //txtTitle.setText(tls.get(0).getLocationName());
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Information_Fragment(),"Infomation");
        adapter.addFragment(new Comment_Fragment(),"Comments");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout(TabLayout tabLayout, ViewPager viewPager){
        tabLayout.setupWithViewPager(mViewPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
