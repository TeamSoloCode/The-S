package com.example.bruce.myapp.Data;

/**
 * Created by BRUCE on 5/16/2017.
 */

public class Information {
    public int infomation_ID;
    public int location_id;
    public String Image;
    public String Info;
    public Information() {
    }

    public Information(int infomation_ID, int location_id, String image, String info) {
        this.infomation_ID = infomation_ID;
        this.location_id = location_id;
        Image = image;
        Info = info;
    }
}
