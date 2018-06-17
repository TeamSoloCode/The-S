package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.Presenter.MenuFragment.IMenuFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by BRUCE on 11/16/2017.
 */

public class MMenuFragment {
    IMenuFragment callback;

    public MMenuFragment(IMenuFragment callback) {
        this.callback = callback;
    }

    public void HandleLogout(){

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        callback.LogoutSuccess();
    }
}
