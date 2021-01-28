package com.example.rotory.userActivity;

public class Scrap {

    public int contentsType;
    public String title;
    public String article;
    public String contentsAddress;
    public  String contentsId;
    public  String savedDate;
    public String titleImage;
    public  String uid;
    public String tag1;

    public Scrap(int contentsType, String title, String article, String contentsAddress,
                 String contentsId, String savedDate, String titleImage, String uid, String tag1) {
        this.contentsType = contentsType;
        this.title = title;
        this.article = article;
        this.contentsAddress = contentsAddress;
        this.contentsId = contentsId;
        this.savedDate = savedDate;
        this.titleImage = titleImage;
        this.uid = uid;
        this.tag1 = tag1;
    }

    public Scrap() {
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
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

    public String getContentsAddress() {
        return contentsAddress;
    }

    public void setContentsAddress(String contentsAddress) {
        this.contentsAddress = contentsAddress;
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
}
