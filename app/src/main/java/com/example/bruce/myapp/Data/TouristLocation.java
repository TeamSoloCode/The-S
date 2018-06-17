package com.example.bruce.myapp.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by BRUCE on 6/5/2018.
 */

public class TouristLocation implements Parcelable,Comparable<TouristLocation> {
    private String id;
    private String addedDate;
    private String basicInfo;
    private String address;
    private double log;
    private double lat;
    private int kindId;
    private String image;
    private String name;
    private long rateTimes;
    private float stars;
    private String updatedDate;
    private double distance;

    public TouristLocation() {}

    protected TouristLocation(Parcel in) {
        id = in.readString();
        addedDate = in.readString();
        basicInfo = in.readString();
        address = in.readString();
        log = in.readDouble();
        lat = in.readDouble();
        kindId = in.readInt();
        image = in.readString();
        name = in.readString();
        rateTimes = in.readLong();
        stars = in.readFloat();
        updatedDate = in.readString();
        distance = in.readDouble();
    }

    public static final Creator<TouristLocation> CREATOR = new Creator<TouristLocation>() {
        @Override
        public TouristLocation createFromParcel(Parcel in) {
            return new TouristLocation(in);
        }

        @Override
        public TouristLocation[] newArray(int size) {
            return new TouristLocation[size];
        }
    };

    @Override
    public int compareTo(@NonNull TouristLocation o) {
        if(this.distance > o.distance){
            return 1;
        }else if(this.distance < o.distance){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.name + "" + this.distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(addedDate);
        dest.writeString(basicInfo);
        dest.writeString(address);
        dest.writeDouble(log);
        dest.writeDouble(lat);
        dest.writeInt(kindId);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeLong(rateTimes);
        dest.writeFloat(stars);
        dest.writeString(updatedDate);
        dest.writeDouble(distance);
    }

    public void setLocationId(String locationId) {
        this.id = locationId;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLocationId() {
        return id;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public String getAddress() {
        return address;
    }

    public double getLog() {
        return log;
    }

    public double getLat() {
        return lat;
    }

    public int getKindId() {
        return kindId;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public long getRateTimes() {
        return rateTimes;
    }

    public float getStars() {
        return stars;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public double getDistance() {
        return distance;
    }
}
