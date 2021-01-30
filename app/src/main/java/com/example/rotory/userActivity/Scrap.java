package com.example.rotory.userActivity;

import java.util.ArrayList;
import java.util.Map;

public class Scrap {

    public int contentsType;
    public String title;
    public String article;
    public ArrayList<String> address;
    //public ArrayList<String> dtrName;
    public String dtrName;
    public  String contentsId;
    public  String savedDate;
    public String titleImage;
    public  String uid;
    public String tag1;


    public Scrap() {
    }


    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }


    public String getDtrName() {
        return dtrName;
    }

    public void setDtrName(String dtrName) {
        this.dtrName = dtrName;
    }

    public String getContentsId() {
        return contentsId;
    }

    public void setContentsId(String contentsId) {
        this.contentsId = contentsId;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }
}
