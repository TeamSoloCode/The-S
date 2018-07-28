package com.example.bruce.myapp.Presenter.Team;

import android.content.Context;
import android.location.Location;

import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.Data.TeamMember;
import com.example.bruce.myapp.Model.MTeam;
import com.example.bruce.myapp.View.Team.IViewTeam;

import java.util.ArrayList;
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
        callbackToView.acceptInvitation(resultCode, resultMessage);
    }

    /**
     * Kiểm tra user có phải la leader hay không
     * @param userId
     * @param teamId
     * @param context
     */
    public void receivedIsLeader(String userId, String teamId, Context context){
        mTeam.handleIsLeader(userId, teamId, context);
    }

    @Override
    public void isLeader(int resultCode, String resultMessage) {
        callbackToView.isLeader(resultCode, resultMessage);
    }

    public void receivedGetAllTeamMember(String userId, String teamId){
        mTeam.handleGetAllTeamsMember(userId, teamId);
    }

    @Override
    public void getAllTeamMember(int resultCode, ArrayList<TeamMember> listTeamMember, String resultMessage) {
        callbackToView.getAllTeamMember(resultCode, listTeamMember, resultMessage);
    }

    public void receivedPostUserLocation(String userId, String teamId, Location userLocation){
        mTeam.handlePostUserLocation(userId, teamId, userLocation);
    }

    public void detachingFirebaseListener(){
        mTeam.handleDetachingListener();
    }

    public void receivedLeaveMyTeam(String userId, String teamId, Context context){
        mTeam.handleLeaveMyTeam(userId, teamId, context);
    }

    @Override
    public void leaveMyTeam(int resultCode, String resultMessage) {
        callbackToView.leaveMyTeam(resultCode, resultMessage);
    }
}
