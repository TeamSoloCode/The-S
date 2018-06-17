package com.example.bruce.myapp.Presenter.MenuFragment;

import com.example.bruce.myapp.Model.MMenuFragment;
import com.example.bruce.myapp.View.MenuFragment.IViewMenuFragment;

/**
 * Created by BRUCE on 11/16/2017.
 */

public class PMenuFragment implements IMenuFragment {

    MMenuFragment modelMenuFragment = new MMenuFragment(this);
    private IViewMenuFragment callbackToView;

    public PMenuFragment(IViewMenuFragment callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedLogout(){

        modelMenuFragment.HandleLogout();
    }

    @Override
    public void LogoutSuccess() {
        callbackToView.LogoutSuccess();
    }
}
