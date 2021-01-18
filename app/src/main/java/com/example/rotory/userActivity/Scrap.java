package com.example.rotory.userActivity;

public class Scrap {

    String title;
    String article;
    String contentsAddress;
    int contentsType;
    String contentsId;
    String savedDate;
    String titleImage;
    String uid;

    public Scrap() {
    }

    public Scrap(String title, String article, String contentsAddress, int contentsType,
                 String contentsId, String savedDate, String titleImage, String uid) {
        this.title = title;
        this.article = article;
        this.contentsAddress = contentsAddress;
        this.contentsType = contentsType;
        this.contentsId = contentsId;
        this.savedDate = savedDate;
        this.titleImage = titleImage;
        this.uid = uid;
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
