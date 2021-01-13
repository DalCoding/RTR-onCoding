package com.example.rotory.Search;

public class SearchResultRoadItem {
    String userLevelImage;
    String userName;
    String road;
    String title;
    String content;
    String likedIcon;
    String liked;

    public SearchResultRoadItem() {

    }

    public SearchResultRoadItem(String userLevelImage, String userName, String road, String title, String content, String likedIcon, String liked) {
        this.userLevelImage = userLevelImage;
        this.userName = userName;
        this.road = road;
        this.title = title;
        this.content = content;
        this.likedIcon = likedIcon;
        this.liked = liked;
    }


    public String getUserLevelImage() {
        return userLevelImage;
    }

    public void setUserLevelImage(String userLevelImage) {
        this.userLevelImage = userLevelImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikedIcon() {
        return likedIcon;
    }

    public void setLikedIcon(String likedIcon) {
        this.likedIcon = likedIcon;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

}
