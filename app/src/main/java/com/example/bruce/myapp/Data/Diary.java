package com.example.bruce.myapp.Data;

/**
 * Created by BRUCE on 7/4/2018.
 */

public class Diary {
    private String id;
    private String name;
    private String description;
    private String image;
    private long createDate;
    private String endDate;
    private String updateDate;
    private float distance;
    private int checkPoint;
    private int deleteFlag;

    public Diary() {
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setCheckPoint(int checkPoint) {
        this.checkPoint = checkPoint;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public long getCreateDate() {
        return createDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public float getDistance() {
        return distance;
    }

    public int getCheckPoint() {
        return checkPoint;
    }

    public String getId() {
        return id;
    }
}
