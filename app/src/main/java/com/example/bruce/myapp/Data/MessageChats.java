package com.example.bruce.myapp.Data;

/**
 * Created by anh khoa on 12/22/2017.
 */

public class MessageChats {
    public String content;
    public String user;
    public String time;
    public boolean isme;
    public String userid;
    public String ImageUser;

    public MessageChats() {
    }

    public MessageChats(String content, String user, String time, boolean isme,String userid, String imageUser) {
        this.content = content;
        this.user = user;
        this.time = time;
        this.isme = isme;
        this.userid=userid;
        this.ImageUser = imageUser;
    }

    public MessageChats(String content, String user, String time, boolean isme) {
        this.content = content;
        this.user = user;
        this.time = time;
        this.isme = isme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIsme() {
        return isme;
    }

    public void setIsme(boolean isme) {
        this.isme = isme;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
