package com.example.rotory.VO;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> master
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

<<<<<<< HEAD
public class Person {

    String	Uid;
=======
public class Person {
    String	person_id;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
<<<<<<< HEAD
=======
public class Person {

    String	Uid;
>>>>>>> master
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
    String	userId;
    String	password;
    String	userName;
    String	mobile;
    String	email;
    String	userImage;
    String userLevel;
    String userLevelImage;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    String  signUpDate;
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
=======
    String  signUpDate;
>>>>>>> master
=======
    String  signUpDate;
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

    public Person() {
    }

    public Person(String person_id, String userId, String password, String userName, String mobile,
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                  String email, String userImage, String userLevel, String userLevelImage, String  signUpDate) {
        this.Uid = person_id;
=======
                  String email, String userImage, String userLevel, String userLevelImage) {
        this.person_id = person_id;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
<<<<<<< HEAD
=======
                  String email, String userImage, String userLevel, String userLevelImage, String  signUpDate) {
        this.Uid = person_id;
>>>>>>> master
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.mobile = mobile;
        this.email = email;
        this.userImage = userImage;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        this.signUpDate = signUpDate;
    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
=======
=======
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        this.signUpDate = signUpDate;
>>>>>>> master
    }


    public String getUid() {
        return Uid;
    }

<<<<<<< HEAD
    public void setPerson_id(String person_id) {
        this.person_id = person_id;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
=======
    public void setUid(String uid) {
        Uid = uid;
<<<<<<< HEAD
>>>>>>> master
=======
=======
    }


    public String getPerson_id() {

        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> master
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

    public String  getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String  signUpDate) {
        this.signUpDate = signUpDate;
    }
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
=======
>>>>>>> master
=======
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
}
