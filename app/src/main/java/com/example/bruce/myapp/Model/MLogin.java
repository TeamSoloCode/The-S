package com.example.bruce.myapp.Model;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Presenter.Login.ILogin;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by BRUCE on 11/14/2017.
 */

public class MLogin extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ILogin callback;
    UserProfile constructerUserProfile;


    public MLogin(ILogin callback) {

        this.callback = callback;
    }
    Profile profile = Profile.getCurrentProfile();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public void LoginChecker(){

        if(profile != null)
        {
            callback.LoginChecker();
        }
        if(user != null) {

            callback.LoginChecker();
        }
    }
    public void HandleLogin(String email, String password){

        if(email.length()==0 || password.length()==0)
        {
            if(email.length()==0){
                callback.EmailIsEmpty();
            }
            else{
                callback.PasswordIsEmpty();
            }
        }
        else {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() ) {
                                //day thong tin ngdung len Firebase
                                constructerUserProfile = new UserProfile(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                                //truyền userprofile qua View
                                callback.User_Profile(constructerUserProfile);
                                callback.LoginSuccess();
                            }
                            else {
                                callback.LoginFailed();
                            }
                        }
                    });
        }
    }
    public void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            UserProfile constructerUserProfile = new UserProfile(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(),"2,3,4,5,6");
                            callback.User_Profile(constructerUserProfile);

                            callback.LoginFacebookSuccess();


                        } else {

                            callback.LoginFacebookFailed();

                        }
                        // ...
                    }
                });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            UserProfile constructerUserProfile = new UserProfile(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                            callback.User_Profile(constructerUserProfile);
                            callback.LoginGoogleSuccess();
                        } else {
                            Log.i("google","failed");
                        }
                        // ...
                    }
                });
    }

}

