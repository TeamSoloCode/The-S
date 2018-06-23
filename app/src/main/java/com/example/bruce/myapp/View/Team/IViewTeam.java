package com.example.bruce.myapp.View.Team;

import com.example.bruce.myapp.Data.InvitersInfo;

import java.util.List;

/**
 * Created by Admin on 01/12/2017.
 */

public interface IViewTeam {
    void createTeam(int resultCode, String resultMessage);
    void hasTeam(int resultCode, String resultMessage);
    void inviteMember(int resultCode, String resultMessage);
    void getInvitersInfo(int resultCode, List<InvitersInfo> invitersInfo, String resultMessage);
    void acceptInvitation(int resultCode, String resultMessage);
}
