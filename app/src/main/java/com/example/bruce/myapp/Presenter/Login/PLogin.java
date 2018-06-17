package com.example.bruce.myapp.Presenter.Login;

import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Model.MLogin;
import com.example.bruce.myapp.View.Login.IViewLogin;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by BRUCE on 11/14/2017.
 */

public class PLogin implements ILogin {

    private MLogin modelLogin = new MLogin(this);
    private IViewLogin callbackToView;
    public PLogin(IViewLogin callbackToView) {

        this.callbackToView = callbackToView;
    }

    public void receivedLoginChecker(){

        modelLogin.LoginChecker();
    }
    public void receivedHandelLoginEvent(String email , String pass){

        modelLogin.HandleLogin(email,pass);
    }
    public void receivedHandleFacebookAccessToken(AccessToken accessToken){

        modelLogin.handleFacebookAccessToken(accessToken);
    }

    public void receivedFirebaseAuthWithGoogle(GoogleSignInAccount account){
        modelLogin.firebaseAuthWithGoogle(account);
    }

    @Override
    public void LoginSuccess() {
        callbackToView.LoginSuccess();
    }

    @Override
    public void LoginFailed() {
        callbackToView.LoginFailed();
    }

    @Override
    public void EmailIsEmpty() {
        callbackToView.EmailIsEmpty();
    }

    @Override
    public void PasswordIsEmpty() {
        callbackToView.PasswordIsEmpty();
    }

    @Override
    public UserProfile User_Profile(UserProfile user) {

        return callbackToView.User_Profile(user);
    }

    @Override
    public void LoginFacebookSuccess() {
        callbackToView.LoginFacebookSuccess();
    }

    @Override
    public void LoginFacebookFailed() {
        callbackToView.LoginFacebookFailed();
    }

    @Override
    public void LoginGoogleSuccess() {
        callbackToView.LoginGoogleSuccess();
    }

    @Override
    public void LoginChecker() {
        callbackToView.LoginChecker();
    }

}
