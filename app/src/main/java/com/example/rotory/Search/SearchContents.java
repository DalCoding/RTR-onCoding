package com.example.rotory.Search;

public class SearchContents {
    int contentsType;
    String title;
    String titleImage;
    int isPublic;
    String userLevel;
    String userName;
    String liked;
    String article;
    String contentsId;
    String writeDate;
    String uid;

    public SearchContents() { }

    public SearchContents(int contentsType, String title, String titleImage, int isPublic, String userLevel, String userName, String liked, String article, String contentsId) {
        this.contentsType = contentsType;
        this.title = title;
        this.titleImage = titleImage;
        this.isPublic = isPublic;
        this.userLevel = userLevel;
        this.userName = userName;
        this.liked = liked;
        this.article = article;
        this.contentsId = contentsId;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
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

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
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

    public String getContentsId() {
        return contentsId;
    }

    public void setContentsId(String contentsId) {
        this.contentsId = contentsId;
    }
}
