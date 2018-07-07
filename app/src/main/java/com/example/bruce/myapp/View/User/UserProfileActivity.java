package com.example.bruce.myapp.View.User;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.User.PUser;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UserProfileActivity extends AppCompatActivity implements IViewUserProfile {

    private FirebaseUser user;
    private DatabaseReference mDataUser;
    private FirebaseAuth firebaseAuth;

    private int REQUEST_CODE_IMAGE = 1;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView txtUserEmail,txtUserPass,txtUserProfile_Name_toolbar,txtBirthday,txtPhone;
    private RadioButton rdiMale,rdiFemale;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private ImageButton btnChangePassWord;
    private EditText txtUserName,edtold,edtnew,edtconfirm;
    private Button btnSave,btnSavePass,btnCanclePass;
    private Dialog dia ;
    private boolean gender;

    private PUser pUser=new PUser(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_constraint);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialize();
        setSupportActionBar(toolbar);
        OnClickFloatingAcionButton();
        CheckGenre(radioGroup);
        //ShowUserInfomation(profilesUser);
        SaveUserInfomation(mDataUser);
        OnClickBtnChangePassWord();

    }

    private void OnClickBtnChangePassWord() {
        btnChangePassWord.setOnClickListener(v -> {
            dia = new Dialog(this);
            dia.setContentView(R.layout.dialog_changepass);
            edtold = dia.findViewById(R.id.edtoldpass);
            edtnew = dia.findViewById(R.id.edtnewpass);
            edtconfirm = dia.findViewById(R.id.edtconfirmPass);
            btnSavePass = dia.findViewById(R.id.btnsavepass);
            btnCanclePass = dia.findViewById(R.id.btncancelpass);
            dia.show();

            OnlickBtnCanclePass();
            OnclickBtnSavePass();
        });
    }
    private void OnclickBtnSavePass() {

        btnSavePass.setOnClickListener(v -> {
            pUser.receivedHandleSavepassWord(edtold.getText().toString(),edtnew.getText().toString(),edtconfirm.getText().toString());
        });
    }

    private void OnlickBtnCanclePass() {
        btnCanclePass.setOnClickListener(v1 -> {
            dia.dismiss();
        });
    }

    private void CheckGenre(RadioGroup radioGroup) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(rdiMale.isChecked()){
                gender=true;
            }
            else {
                gender=false;
            }
        });
    }

    private void SaveUserInfomation(DatabaseReference mDataUser) {
        btnSave.setOnClickListener(v -> {
            if(user!=null) {
//                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(txtUserName.getText().toString().trim())
//                        .build();
//                user.updateProfile(profileUpdates);
//                txtUserName.setText(txtUserName.getText().toString().trim());
//                txtUserProfile_Name_toolbar.setText(txtUserName.getText().toString().trim());
//                mDataUser.child("Email").setValue(txtUserEmail.getText().toString().trim());
//                mDataUser.child("Name").setValue(txtUserName.getText().toString().trim());
//                mDataUser.child("Image").setValue(user.getPhotoUrl().toString());
//                mDataUser.child("Phone").setValue(txtPhone.getText().toString().trim());
//                mDataUser.child("Birthday").setValue(txtBirthday.getText().toString().trim());
//                mDataUser.child("Gender").setValue(gender);

                Toasty.success(this, "Save Successful !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowUserInfomation(ArrayList<UserProfile> profilesUser) {
        txtUserEmail.setText(user.getEmail());
        txtUserName.setText(user.getDisplayName());
        txtUserProfile_Name_toolbar.setText(user.getDisplayName());
        Picasso.with(this).load(user.getPhotoUrl()).transform(new CircleTransform()).into(imageView);
            txtBirthday.setText(profilesUser.get(0).Birthday);
            txtPhone.setText(profilesUser.get(0).Phone);
                if(profilesUser.get(0).Gender ==true)
                {
                    rdiMale.setChecked(true);
                } else {
                    rdiFemale.setChecked(true);
                }
    }

    private void OnClickFloatingAcionButton() {
        fab.setOnClickListener(view -> {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamera, REQUEST_CODE_IMAGE);
        });
    }

    private void initialize() {

        txtUserEmail =  findViewById(R.id.txtUserProfile_Mail);
        txtUserName =  findViewById(R.id.txtUserProfile_Name);
        txtUserPass =  findViewById(R.id.txtUserProfile_Pass);
        txtPhone=  findViewById(R.id.txtPhone);
        txtBirthday=  findViewById(R.id.txtBirthday);
        rdiMale=  findViewById(R.id.rdiMale);
        rdiFemale=  findViewById(R.id.rdiFemale);
        radioGroup=  findViewById(R.id.radioGroup);

        imageView =  findViewById(R.id.imageView) ;
        toolbarLayout =  findViewById(R.id.toolbar_layout);
        txtUserProfile_Name_toolbar =  findViewById(R.id.txtUserProfile_Name_toolbar);
        btnSave=  findViewById(R.id.btnSave);

        btnChangePassWord=  findViewById(R.id.btnchangepass);


        toolbar =  findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth= FirebaseAuth.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            //Drawable d = new BitmapDrawable(getResources(), bitmap);
            //toolbarLayout.setBackground(d);
            CircleTransform circleTransform = new CircleTransform();
            Bitmap circleBitmap = circleTransform.transform(bitmap);
            imageView.setImageBitmap(circleBitmap);

            StorageReference userImageRef = storageRef.child("Image/User/"+user.getEmail());

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap1 = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = userImageRef.putBytes(data1);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(downloadUrl.toString()))
                            .build();
                    user.updateProfile(profileChangeRequest);
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void changePasswordSuccess() {
        Toast.makeText(this, "Password has changed", Toast.LENGTH_SHORT).show();
        dia.dismiss();
    }

    @Override
    public void confirmPasswordwrong() {
        Toast.makeText(this, "Confirm Password is wrong ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newPasswordIsEmpty() {
        Toast.makeText(this, "Please type your new password \n" +
                "and your confirm password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void oldPasswordWrong() {
        Toast.makeText(this, "Old password is wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void oldPasswordIsEmpty() {
        Toast.makeText(this, "Please type your old password", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), HistoryAndHobbyActivity.class));
        super.onBackPressed();
    }
}
