package com.example.bruce.myapp.View.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Button btnOk;
    EditText edtEmail,edtPass,edtName;
    ProgressDialog dialogRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //khong cho quay man hinh dien thoai
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnOk= (Button) findViewById(R.id.btnOK);
        edtEmail= (EditText) findViewById(R.id.edtEmail);
        edtPass= (EditText) findViewById(R.id.edtPass);
        edtName= (EditText) findViewById(R.id.edtUsername);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.length() != 0 && edtName.length() != 0 && edtPass.length() != 0){

                    Register();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Please type all information request below!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void Register(){
        dialogRegister=new ProgressDialog(this);
        dialogRegister.show();
        dialogRegister.setMessage("Đang đăng ký .....");
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(edtName.getText().toString())
                                    .setPhotoUri(Uri.parse("https://cdn2.iconfinder.com/data/icons/picons-basic-1/57/basic1-114_user_man-128.png"))
                                    .build();

                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if(dialogRegister.isShowing()){
                                                    dialogRegister.dismiss();
                                                }

                                                Toasty.success(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        } else {
                            if(dialogRegister.isShowing()){
                                dialogRegister.dismiss();
                            }
                            // If sign in fails, display a message to the user.
                            Toasty.success(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
