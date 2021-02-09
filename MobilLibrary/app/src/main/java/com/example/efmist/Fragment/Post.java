package com.example.efmist.Fragment;

public class Post {

    String pImg,pTitle,pDescription,uid,pId,pTime;
    public Post(){

    }


    public Post(String pImg, String pTitle, String pDescription, String uid, String pId, String pTime) {
        this.pImg = pImg;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.uid = uid;
        this.pId = pId;
        this.pTime = pTime;
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescrition) {
        this.pDescription = pDescrition;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}
