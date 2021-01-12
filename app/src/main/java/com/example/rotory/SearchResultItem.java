package com.example.rotory;

public class SearchResultItem {
    //String userLevelImage;
    String userName;
    //Long contentsType;
    String storyTitle;
    String storyContent;
    //String titleImage;
    String liked;
    //String likedImage;

    public SearchResultItem() {}

    public SearchResultItem(/*String userLevelImage, */String userName/*, Long contentsType*/, String storyTitle, String storyContent/*, String titleImage*/, String liked/*, String likedImage*/) {
        //this.userLevelImage = userLevelImage;
        this.userName = userName;
        //this.contentsType = contentsType;
        this.storyTitle = storyTitle;
        this.storyContent = storyContent;
        //this.titleImage = titleImage;
        this.liked = liked;
        //this.likedImage = likedImage;
    }

    /*public String getUserLevelImage() {
        return userLevelImage;
    }

    public void setUserLevelImage(String userLevelImage) {
        this.userLevelImage = userLevelImage;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*public long getContentsType() {
        return contentsType;
    }

    public void setContentsType(Long contentsType) {
        this.contentsType = contentsType;
    }*/

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    /*public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }*/

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    /*public String getLikedImage() {
        return likedImage;
    }

    public void setLikedImage(String likedImage) {
        this.likedImage = likedImage;
    }*/
}
