package com.example.efmist.Activity;

public class User {


    private String email;
    private String uid;
    private String image;
    private String name;
    public User() {
    }

    public User(String email, String uid, String image, String name) {
        this.email = email;
        this.uid = uid;
        this.image = image;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
