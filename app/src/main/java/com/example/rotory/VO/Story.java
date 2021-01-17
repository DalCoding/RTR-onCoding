package com.example.rotory.VO;

import java.util.ArrayList;
import java.util.Date;

public class Story {
    int storyId;
    int prefixId;
    int isPublic;
    String textStory;
    String uid;
    String userName;
    String userLevel;
    String userLevelImage;
    String storyTitle;
    String storyContents;
    String storyAddress;
    String titleImage;
    String favoriteIcon;
    String liked;
    String stared;
    String scrapped;
    String storyComment;
    Date addedDate;
    Date writeDate;
    Date modifiedDate;
    ArrayList<String> smallImage;
    ArrayList<String> imageComment;


    public Story() {
        // firestore 연결에 필요한 생성자
    }

    public Story(int storyId, int prefixId, int isPublic, String textStory, String uid, String userName, String userLevel, String userLevelImage, String storyTitle,
                 String storyContents, String storyAddress, String titleImage, String favoriteIcon, String liked, String stared,
                 String scrapped, String storyComment, Date addedDate, Date writeDate, Date modifiedDate, ArrayList<String> smallImage,
                 ArrayList<String> imageComment) {
        this.storyId = storyId;
        this.prefixId = prefixId;
        this.isPublic = isPublic;
        this.textStory = textStory;
        this.uid = uid;
        this.userName = userName;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
        this.storyTitle = storyTitle;
        this.storyContents = storyContents;
        this.storyAddress = storyAddress;
        this.titleImage = titleImage;
        this.favoriteIcon = favoriteIcon;
        this.liked = liked;
        this.stared = stared;
        this.scrapped = scrapped;
        this.storyComment = storyComment;
        this.addedDate = addedDate;
        this.writeDate = writeDate;
        this.modifiedDate = modifiedDate;
        this.smallImage = smallImage;
        this.imageComment = imageComment;
    }

    public Story(String textStory, String userLevel, String userName, String userLevelImage, String storyTitle, String storyContents, String titleImage, String favoriteIcon, String liked) {
        this.textStory = textStory;
        this.userName = userName;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
        this.storyTitle = storyTitle;
        this.storyContents = storyContents;
        this.titleImage = titleImage;
        this.favoriteIcon = favoriteIcon;
        this.liked = liked;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(int prefixId) {
        this.prefixId = prefixId;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getTextStory() {
        return textStory;
    }

    public void setTextStory(String textStory) {
        this.textStory = textStory;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLevelImage() {
        return userLevelImage;
    }

    public void setUserLevelImage(String userLevelImage) {
        this.userLevelImage = userLevelImage;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryContents() {
        return storyContents;
    }

    public void setStoryContents(String storyContents) {
        this.storyContents = storyContents;
    }

    public String getStoryAddress() {
        return storyAddress;
    }

    public void setStoryAddress(String storyAddress) {
        this.storyAddress = storyAddress;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getFavoriteIcon() {
        return favoriteIcon;
    }

    public void setFavoriteIcon(String favoriteIcon) {
        this.favoriteIcon = favoriteIcon;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getStared() {
        return stared;
    }

    public void setStared(String stared) {
        this.stared = stared;
    }

    public String getScrapped() {
        return scrapped;
    }

    public void setScrapped(String scrapped) {
        this.scrapped = scrapped;
    }

    public String getStoryComment() {
        return storyComment;
    }

    public void setStoryComment(String storyComment) {
        this.storyComment = storyComment;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ArrayList<String> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(ArrayList<String> smallImage) {
        this.smallImage = smallImage;
    }

    public ArrayList<String> getImageComment() {
        return imageComment;
    }

    public void setImageComment(ArrayList<String> imageComment) {
        this.imageComment = imageComment;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
