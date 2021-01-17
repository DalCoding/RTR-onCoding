package com.example.rotory.VO;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Person {

    String	Uid;
    String	userId;
    String	password;
    String	userName;
    String	mobile;
    String	email;
    String	userImage;
    String userLevel;
    String userLevelImage;
    String  signUpDate;

    public Person() {
    }

    public Person(String person_id, String userId, String password, String userName, String mobile,
                  String email, String userImage, String userLevel, String userLevelImage, String  signUpDate) {
        this.Uid = person_id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.mobile = mobile;
        this.email = email;
        this.userImage = userImage;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
        this.signUpDate = signUpDate;
    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLevelImage() {
        return userLevelImage;
    }

    public void setUserLevelImage(String userLevelImage) {
        this.userLevelImage = userLevelImage;
    }

    public String  getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String  signUpDate) {
        this.signUpDate = signUpDate;
    }
}
