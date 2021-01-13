package com.example.rotory.Search;

public class SearchResultStoryItem {
    String userLevelImage;
    String userName;
    String story;
    String title;
    String content;
    String titleImage;
    String likedIcon;
    String liked;

    public SearchResultStoryItem() {

    }

    public SearchResultStoryItem(String userLevelImage, String userName, String story, String title, String content, String titleImage, String likedIcon, String liked) {
        this.userLevelImage = userLevelImage;
        this.userName = userName;
        this.story = story;
        this.title = title;
        this.content = content;
        this.titleImage = titleImage;
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

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
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

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
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
