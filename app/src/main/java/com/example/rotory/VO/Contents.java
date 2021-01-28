package com.example.rotory.VO;

import com.google.type.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Contents {
    int contentsType;
    String uid;
    String title;
    String tag1;
    String tag2;
    String tag3;
    String tag4;
    String tag5;
    String tag6;
    String tag7;
    String tag8;
    String tag9;
    String tag10;
    String hour;
    String min;
    String isPartner;
    int dtrRating;
    String ratingComment;
    ArrayList<String> dtrName;
    ArrayList<Object> dtrLatLng;
    String prefix;
    ArrayList<String> address;
    String titleImage;
    Map<String, String> imageComment;
    String article;
    int isPublic;
    String writeDate;
    String modifiedDate;
    String scrapped;
    Map<String, Object> smallImage;
    String userLevel;
    String userName;
    String liked;
    String time;


    public Contents() {
    }

    public Contents(String title, String tag1, String time) {
        this.title = title;
        this.tag1 = tag1;
        this.time = time;
    }


    public Contents(String title, String tag1, String hour, String min, String writeDate) {
        this.title = title;
        this.tag1 = tag1;
        this.hour = hour;
        this.min = min;
        this.writeDate = writeDate;
    }

    public Contents(int contentsType, String title, String titleImage, String article, String userLevel, String userName, String liked) {
        this.contentsType = contentsType;
        this.title = title;
        this.titleImage = titleImage;
        this.article = article;
        this.userLevel = userLevel;
        this.userName = userName;
        this.liked = liked;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }

    public String getTag5() {
        return tag5;
    }

    public void setTag5(String tag5) {
        this.tag5 = tag5;
    }

    public String getTag6() {
        return tag6;
    }

    public void setTag6(String tag6) {
        this.tag6 = tag6;
    }

    public String getTag7() {
        return tag7;
    }

    public void setTag7(String tag7) {
        this.tag7 = tag7;
    }

    public String getTag8() {
        return tag8;
    }

    public void setTag8(String tag8) {
        this.tag8 = tag8;
    }

    public String getTag9() {
        return tag9;
    }

    public void setTag9(String tag9) {
        this.tag9 = tag9;
    }

    public String getTag10() {
        return tag10;
    }

    public void setTag10(String tag10) {
        this.tag10 = tag10;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getIsPartner() {
        return isPartner;
    }

    public void setIsPartner(String isPartner) {
        this.isPartner = isPartner;
    }

    public int getDtrRating() {
        return dtrRating;
    }

    public void setDtrRating(int dtrRating) {
        this.dtrRating = dtrRating;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public ArrayList<String> getDtrName() {
        return dtrName;
    }

    public void setDtrName(ArrayList<String> dtrName) {
        this.dtrName = dtrName;
    }

    public ArrayList<Object> getDtrLatLng() {
        return dtrLatLng;
    }

    public void setDtrLatLng(ArrayList<Object> dtrLatLng) {
        this.dtrLatLng = dtrLatLng;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getScrapped() {
        return scrapped;
    }

    public void setScrapped(String scrapped) {
        this.scrapped = scrapped;
    }

    public void setImageComment(Map<String, String> imageComment) {
        this.imageComment = imageComment;
    }

    public Map<String, Object> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(Map<String, Object> smallImage) {
        this.smallImage = smallImage;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


