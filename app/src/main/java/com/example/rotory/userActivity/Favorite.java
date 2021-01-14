package com.example.rotory.userActivity;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Favorite {
    private String Uid;
    private String userName;
    private String userLevel;
    private String userLevelImg;
    private String userImg;
    private Date mTimeStamp;

    public Favorite(String uid, String userName, String userLevel, String userLevelImg, String userImg) {
        this.Uid = uid;
        this.userName = userName;
        this.userLevel = userLevel;
        this.userLevelImg = userLevelImg;
        this.userImg = userImg;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLevelImg() {
        return userLevelImg;
    }

    public void setUserLevelImg(String userLevelImg) {
        this.userLevelImg = userLevelImg;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    @ServerTimestamp
    public Date getTimeStamp(){
        return mTimeStamp;
    }
    public void setTimestamp(Date timestamp) {
        mTimeStamp = timestamp; }
}
