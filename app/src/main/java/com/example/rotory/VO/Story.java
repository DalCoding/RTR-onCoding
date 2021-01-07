package com.example.rotory.VO;

import java.util.ArrayList;

public class Story {
    int	story_id;
    int	user_id;
    int	prefix_id;
    String	storyTitle;
    String	storyAddress;
    String	titleImage;
    ArrayList<String> smallImage;
    ArrayList<String>	imageComment;
    String	storyContent;
    int	isPublic;
    String	writeDate;

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPrefix_id() {
        return prefix_id;
    }

    public void setPrefix_id(int prefix_id) {
        this.prefix_id = prefix_id;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
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

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
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

    String	modifiedDate;
}
