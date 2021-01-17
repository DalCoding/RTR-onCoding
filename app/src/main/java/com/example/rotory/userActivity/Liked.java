package com.example.rotory.userActivity;

public class Liked {
    String titleImage;
    String title;
    String tag1;
    String savedDate;
    String contentsId;
    int contentsType;

    public Liked() {
    }

    public Liked(String titleImage, String title, String tag1, String savedDate, String contentsId, int contentsType) {
        this.titleImage = titleImage;
        this.title = title;
        this.tag1 = tag1;
        this.savedDate = savedDate;
        this.contentsId = contentsId;
        this.contentsType = contentsType;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
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

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }
}

