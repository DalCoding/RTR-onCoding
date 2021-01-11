package com.example.rotory.VO;

import java.util.ArrayList;
import java.util.HashMap;

public class Contents {
    int contents_id;
    int contentsType;
    int user_id;
    int road_id;
    int story_id;
    String roadTitle;
    ArrayList<String> tag;
    String hour;
    String min;
    String isPartner;
    int dtrRating;
    String ratingComment;
    ArrayList<Integer> dtrRoadLine;
    ArrayList<String> dtrName;
    String storyTitle;
    int prefix_id;
    String storyAddress;
    String titleImage;
    ArrayList<String> smallImage;
    HashMap<String, ArrayList<Integer>> dtrAddress;
    ArrayList<String> imageComment;
    String storyContent;
    int isPublic;
    String writeDate;
    String modifiedDate;
    String roadAddress;


    public int getContents_id() {
        return contents_id;
    }

    public void setContents_id(int contents_id) {
        this.contents_id = contents_id;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoad_id() {
        return road_id;
    }

    public void setRoad_id(int road_id) {
        this.road_id = road_id;
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public String getRoadTitle() {
        return roadTitle;
    }

    public void setRoadTitle(String roadTitle) {
        this.roadTitle = roadTitle;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getIsPartner() {
        return isPartner;
    }

    public void setIsPartner(String isPartner) {
        this.isPartner = isPartner;
    }

    public int getDtrRating() {
        return dtrRating;
    }

    public void setDtrRating(int dtrRating) {
        this.dtrRating = dtrRating;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public ArrayList<Integer> getDtrRoadLine() {
        return dtrRoadLine;
    }

    public void setDtrRoadLine(ArrayList<Integer> dtrRoadLine) {
        this.dtrRoadLine = dtrRoadLine;
    }

    public ArrayList<String> getDtrName() {
        return dtrName;
    }

    public void setDtrName(ArrayList<String> dtrName) {
        this.dtrName = dtrName;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public int getPrefix_id() {
        return prefix_id;
    }

    public void setPrefix_id(int prefix_id) {
        this.prefix_id = prefix_id;
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

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public HashMap<String, ArrayList<Integer>> getDtrAddress() {
        return dtrAddress;
    }

    public void setDtrAddress(HashMap<String, ArrayList<Integer>> dtrAddress) {
        this.dtrAddress = dtrAddress;
    }
}


