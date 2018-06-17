package com.example.bruce.myapp.View.Login;

import com.example.bruce.myapp.Data.UserProfile;

/**
 * Created by BRUCE on 11/14/2017.
 */

public interface IViewLogin {
    void LoginSuccess();
    void LoginFailed();
    void EmailIsEmpty();
    void PasswordIsEmpty();
    UserProfile User_Profile(UserProfile user);
    void LoginFacebookSuccess();
    void LoginFacebookFailed();
    void LoginGoogleSuccess();
    void LoginChecker();
}
