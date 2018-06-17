package com.example.bruce.myapp.Presenter.Team;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Model.MTeam;
import com.example.bruce.myapp.View.Team.IViewTeam;

import java.util.ArrayList;

/**
 * Created by Admin on 01/12/2017.
 */

public class PTeam implements ITeam{
    public IViewTeam callbackToView;

    public MTeam mTeam=new MTeam(this);

    public PTeam(IViewTeam callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedAddListUser(TeamAdapter adapter,ArrayList<UserProfile> listUser){
        mTeam.handleAddListUser(adapter,listUser);
    }


}
