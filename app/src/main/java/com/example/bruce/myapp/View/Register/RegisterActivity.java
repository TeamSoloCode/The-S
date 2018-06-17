package com.example.bruce.myapp.View.Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

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
                                    .setPhotoUri(Uri.parse("https://scontent.fsgn5-1.fna.fbcdn.net/v/t31.0-1/c379.0.1290.1290/10506738_10150004552801856_220367501106153455_o.jpg?oh=31f7ca1c6afb7b2ff08b0a73fd383def&oe=5ACE2A7C"))
                                    .build();

                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if(dialogRegister.isShowing()){
                                                    dialogRegister.dismiss();
                                                }
                                                FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid()).child("Email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid()).child("Image").setValue("https://scontent.fsgn5-1.fna.fbcdn.net/v/t31.0-1/c379.0.1290.1290/10506738_10150004552801856_220367501106153455_o.jpg?oh=31f7ca1c6afb7b2ff08b0a73fd383def&oe=5ACE2A7C");
                                                FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid()).child("Hobbie").setValue("1,2");
                                                FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid()).child("Behavior").setValue("1,2");
                                                FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid()).child("Gender").setValue(true);

                                                AlertDialog.Builder tb=new AlertDialog.Builder(RegisterActivity.this)
                                                        .setMessage("Register Success!,\nPlease check your mail! ")
                                                        .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                tb.create().show();
                                            }
                                        }
                                    });

                        } else {
                            if(dialogRegister.isShowing()){
                                dialogRegister.dismiss();
                            }
                            // If sign in fails, display a message to the user.
                            AlertDialog.Builder tb1=new AlertDialog.Builder(RegisterActivity.this)
                                    .setMessage("Register failed !!")
                                    .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            tb1.create().show();
                        }

                        // ...
                    }
                });
    }
}
