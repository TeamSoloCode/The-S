package com.example.bruce.myapp.Presenter.User;

/**
 * Created by Admin on 28/11/2017.
 */

public interface IUser {
    void changePasswordSuccess();
    void confirmPasswordwrong();
    void newPasswordIsEmpty();
    void oldPasswordWrong();
    void oldPasswordIsEmpty();
}
