package com.example.rotory.VO;

public class Person {
    String	person_id;
    String	userId;
    String	password;
    String	userName;
    String	mobile;
    String	email;
    String	userImage;
    String userLevel;

    public Person() {
    }

    public Person(String person_id, String userId, String password, String userName,
                  String mobile, String email, String userImage, String userLevel) {
        this.person_id = person_id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.mobile = mobile;
        this.email = email;
        this.userImage = userImage;
        this.userLevel = userLevel;
    }


    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
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
}
