package com.example.bruce.myapp.Presenter.User;

import com.example.bruce.myapp.Model.MUserProfile;
import com.example.bruce.myapp.View.User.IViewUserProfile;

/**
 * Created by Admin on 27/11/2017.
 */

public class PUser implements IUser {
    IViewUserProfile callbackToView;
    MUserProfile mUserProfile=new MUserProfile(this);

    public PUser(IViewUserProfile callbackToView) {
     this.callbackToView = callbackToView;
    }

    public void receivedHandleSavepassWord(String oldPassWord, String newPassWord, String confirmPassWord){
          mUserProfile.handleSavepassWord(oldPassWord,newPassWord,confirmPassWord);
      }

    @Override
    public void changePasswordSuccess() {
      callbackToView.changePasswordSuccess();
    }

    @Override
    public void confirmPasswordwrong() {
      callbackToView.confirmPasswordwrong();
    }

    @Override
    public void newPasswordIsEmpty() {
      callbackToView.newPasswordIsEmpty();
    }

    @Override
    public void oldPasswordWrong() {
      callbackToView.oldPasswordWrong();
    }

    @Override
    public void oldPasswordIsEmpty() {
      callbackToView.oldPasswordIsEmpty();
    }
}
