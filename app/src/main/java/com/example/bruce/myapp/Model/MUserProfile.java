package com.example.bruce.myapp.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Presenter.User.IUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Admin on 27/11/2017.
 */

public class MUserProfile {
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user=firebaseAuth.getCurrentUser();

    IUser callback;

    public MUserProfile(IUser callback) {
        this.callback = callback;
    }

    public void handleSavepassWord(String oldPassWord, String newPassWord, String confirmPassWord)
    {
        if(oldPassWord.length() != 0){
            firebaseAuth.signInWithEmailAndPassword(user.getEmail(), oldPassWord)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (newPassWord.length() != 0 && confirmPassWord.length() != 0) {
                                if (newPassWord.equals(confirmPassWord)) {
                                    user.updatePassword(newPassWord).addOnCompleteListener(task1 -> {
                                        callback.changePasswordSuccess();
                                    });
                                } else {
                                        callback.confirmPasswordwrong();
                                }
                            }
                            else {
                                callback.newPasswordIsEmpty();
                            }
                        }
                        else {
                            callback.oldPasswordWrong();
                        }
                    });
        }
        else {
            callback.oldPasswordIsEmpty();
        }
    }

}
