package com.example.bruce.myapp.Data;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class CheckPoint {
    private String id;
    private double lat;
    private double log;
    private ArrayList<String> images;
    private String description;
    private long createDate = System.currentTimeMillis();
    private boolean deletePlag;

    public CheckPoint() {
        this.images = new ArrayList<>();
        this.images.add("https://www.google.com/logos/doodles/2018/childrens-day-2018-panama-venezuela-5562131071107072-2x.png");
        this.images.add("https://www.google.com/logos/doodles/2014/emmeline-pankhursts-156th-birthday-5135289961938944-hp.jpg");
    }

    public String getId() {
        return id;
    }

    public boolean isDeletePlag() {
        return deletePlag;
    }

    public void setDeletePlag(boolean deletePlag) {
        this.deletePlag = deletePlag;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
