package com.example.bruce.myapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by BRUCE on 7/15/2018.
 */

public class CheckPoint implements Parcelable {
    private String id;
    private double lat;
    private double log;
    private ArrayList<String> images;
    private String description;
    private long createDate;
    private boolean deleteFlag;
    private int kind;

    public CheckPoint() {}

    protected CheckPoint(Parcel in) {
        id = in.readString();
        lat = in.readDouble();
        log = in.readDouble();
        images = in.createStringArrayList();
        description = in.readString();
        createDate = in.readLong();
        kind = in.readInt();
    }

    public static final Creator<CheckPoint> CREATOR = new Creator<CheckPoint>() {
        @Override
        public CheckPoint createFromParcel(Parcel in) {
            return new CheckPoint(in);
        }

        @Override
        public CheckPoint[] newArray(int size) {
            return new CheckPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(lat);
        dest.writeDouble(log);
        dest.writeStringList(images);
        dest.writeString(description);
        dest.writeLong(createDate);
        dest.writeInt(kind);
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public LatLng toPosition(){
        return new LatLng(this.lat, this.log);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeletePlag() {
        return deleteFlag;
    }

    public void setDeletePlag(boolean deletePlag) {
        this.deleteFlag = deletePlag;
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
