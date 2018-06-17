package com.example.bruce.myapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BRUCE on 6/15/2017.
 */

public class UserProfile implements Parcelable {
    public String Email;
    public String Name;
    public String Image ="";
    public String Hobbie;
    public String Phone="";
    public String Birthday="";
    public boolean Gender;
    public String Behavior;
    public UserProfile() {
    }

    public UserProfile(String email, String name, String image, String hobbie) {
        Email = email;
        Name = name;
        Image = image;
        Hobbie = hobbie;
    }

    public UserProfile(String email, String name, String image) {
        Email = email;
        Name = name;
        Image = image;
    }


    protected UserProfile(Parcel in) {
        Email = in.readString();
        Name = in.readString();
        Image = in.readString();
        Hobbie = in.readString();
        Phone = in.readString();
        Birthday = in.readString();
        Gender = in.readByte() != 0;
        Behavior = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(Hobbie);
        dest.writeString(Phone);
        dest.writeString(Birthday);
        dest.writeByte((byte) (Gender ? 1 : 0));
        dest.writeString(Behavior);
    }
}
