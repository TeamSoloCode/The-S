package com.example.bruce.myapp.Data;

/**
 * Created by BRUCE on 6/24/2018.
 */

public class TeamMember {
    private String id;
    private String name;
    private String phone = "1234";
    private String image;
    private String email;

    public TeamMember() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
