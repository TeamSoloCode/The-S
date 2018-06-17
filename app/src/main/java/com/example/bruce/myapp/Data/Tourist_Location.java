package com.example.bruce.myapp.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by BRUCE on 5/5/2017.
 */

public class Tourist_Location implements Parcelable,Comparable<Tourist_Location> {

    public int location_ID;
    public String LocationName;
    public double Latitude;
    public double Longtitude;

    public String LocationImg;
    public String Address;
    public String BasicInfo;

    public int province_ID = 1;

    public float star = 0f;
    public double Distance = 0.0;
    public int kindOfLocation;



    public Tourist_Location() {

    }

    public Tourist_Location(int location_ID, String locationName, double latitude, double longtitude, String locationImg, String address, String basicInfo, int province_ID, float star, double distance, int kindOfLocation) {
        this.location_ID = location_ID;
        LocationName = locationName;
        Latitude = latitude;
        Longtitude = longtitude;
        LocationImg = locationImg;
        Address = address;
        BasicInfo = basicInfo;
        this.province_ID = province_ID;
        this.star = star;
        Distance = distance;
        this.kindOfLocation = kindOfLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(location_ID);
        dest.writeString(LocationName);
        dest.writeString(LocationImg);
        dest.writeString(BasicInfo);
        dest.writeString(Address);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longtitude);
        dest.writeInt(province_ID);
        dest.writeFloat(star);
        dest.writeDouble(Distance);
        dest.writeInt(kindOfLocation);
    }

    public static  final Parcelable.Creator<Tourist_Location> CREATOR = new Parcelable.Creator<Tourist_Location>() {
        @Override
        public Tourist_Location createFromParcel(Parcel in) {

            return new Tourist_Location(in);
        }

        @Override
        public Tourist_Location[] newArray(int size) {
            return new Tourist_Location[size];
        }
    };

    private Tourist_Location(Parcel in) {
        location_ID = in.readInt();
        LocationName = in.readString();
        LocationImg = in.readString();
        BasicInfo = in.readString();
        Address = in.readString();
        Latitude = in.readDouble();
        Longtitude = in.readDouble();
        province_ID = in.readInt();
        star = in.readFloat();
        Distance = in.readDouble();
        kindOfLocation = in.readInt();
    }

    public int getLocation_ID() {
        return location_ID;
    }

    public void setLocation_ID(int location_ID) {
        this.location_ID = location_ID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }

    public String getLocationImg() {
        return LocationImg;
    }

    public void setLocationImg(String locationImg) {
        LocationImg = locationImg;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBasicInfo() {
        return BasicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        BasicInfo = basicInfo;
    }

    public int getProvince_ID() {
        return province_ID;
    }

    public void setProvince_ID(int province_ID) {
        this.province_ID = province_ID;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public int getKindOfLocation() {
        return kindOfLocation;
    }

    public void setKindOfLocation(int kindOfLocation) {
        this.kindOfLocation = kindOfLocation;
    }

    public static Creator<Tourist_Location> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int compareTo(@NonNull Tourist_Location o) {
        if(this.Distance > o.Distance){
            return 1;
        }else if(this.Distance < o.Distance){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.LocationName + "" + this.Distance;
    }
}

