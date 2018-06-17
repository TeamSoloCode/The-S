package com.example.bruce.myapp.Presenter.Login;

import com.example.bruce.myapp.Data.UserProfile;

/**
 * Created by BRUCE on 11/14/2017.
 */

public interface ILogin {
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
