package com.example.bruce.myapp.Data;

/**
 * Created by anh khoa on 12/23/2017.
 */

public class ChatRoom {
    private String tenRoom;
    private String userId;

    public ChatRoom() {
    }

    public ChatRoom(String tenRoom, String userId) {
        this.tenRoom = tenRoom;
        this.userId = userId;
    }

    public String getTenRoom() {
        return tenRoom;
    }

    public void setTenRoom(String tenRoom) {
        this.tenRoom = tenRoom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
