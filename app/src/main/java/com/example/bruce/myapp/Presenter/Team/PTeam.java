package com.example.bruce.myapp.Presenter.Team;

import android.content.Context;

import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.Model.MTeam;
import com.example.bruce.myapp.View.Team.IViewTeam;

import java.util.List;

/**
 * Created by Admin on 01/12/2017.
 */

public class PTeam implements ITeam{
    public IViewTeam callbackToView;

    public MTeam mTeam=new MTeam(this);

    public PTeam(IViewTeam callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedCreateTeam(String userId, String teamsName){
        mTeam.createTeam(userId, teamsName);
    }

    @Override
    public void createTeam(int resultCode, String resultMessage) {
        callbackToView.createTeam(resultCode, resultMessage);
    }

    public void receivedHasTeam(String userId, Context context){
        mTeam.hasTeam(userId, context);
    }

    @Override
    public void hasTeam(int resultCode, String resultMessage) {
        callbackToView.hasTeam(resultCode, resultMessage);
    }

    public void receivedInviteMember(String userId, String userInvitedEmail, Context context){
        mTeam.handleInviteMember(userId, userInvitedEmail,context);
    }

    @Override
    public void inviteMember(int resultCode, String resultMessage) {
        callbackToView.inviteMember(resultCode, resultMessage);
    }

    public void receivedGetInviterinformation(String userId){
        mTeam.getInvitersInformation(userId);
    }

    @Override
    public void getInvitersInfo(int resultCode, List<InvitersInfo> invitersInfo, String resultMessage) {
        callbackToView.getInvitersInfo(resultCode, invitersInfo, resultMessage);
    }

    public void receivedAcceptInvitation(String userId, String teamId){
        mTeam.handleAcceptTheInvitation(userId, teamId);
    }

    @Override
    public void acceptInvitation(int resultCode, String resultMessage) {

    }
}
