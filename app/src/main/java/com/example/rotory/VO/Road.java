package com.example.rotory.VO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Road {
    int roadId;
    int dtrRating;
    int isPublic;
    String ratingComment;
    String textRoad;
    String uid;
    String userName;
    String userLevel;
    String userLevelImage;
    String roadTitle;
    String hour;
    String min;
    String isPartner;
    String favoriteIcon;
    String liked;
    String stared;
    String scrapped;
    String roadComment;
    Date writeDate;
    Date modifiedDate;
    Date addedDate;
    // Map<Object, Object> dtrRoadLine;
    ArrayList<String> tag;

    public Road() {
        // firestore 연결에 필요한 생성자
    }

    public Road(int roadId, int dtrRating, int isPublic, String ratingComment, String textRoad, String uid, String userName,
                String userLevelImage, String userLevel, String roadTitle, String hour, String min, String isPartner, String favoriteIcon, String liked,
                String stared, String scrapped, String roadComment, Date writeDate, Date modifiedDate, Date addedDate,
                Map<Object, Object> dtrRoadLine, ArrayList<String> tag) {
        this.roadId = roadId;
        this.dtrRating = dtrRating;
        this.isPublic = isPublic;
        this.ratingComment = ratingComment;
        this.textRoad = textRoad;
        this.uid = uid;
        this.userName = userName;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
        this.roadTitle = roadTitle;
        this.hour = hour;
        this.min = min;
        this.isPartner = isPartner;
        this.favoriteIcon = favoriteIcon;
        this.liked = liked;
        this.stared = stared;
        this.scrapped = scrapped;
        this.roadComment = roadComment;
        this.writeDate = writeDate;
        this.modifiedDate = modifiedDate;
        this.addedDate = addedDate;
        //this.dtrRoadLine = dtrRoadLine;
        this.tag = tag;
    }

    public Road(String textRoad, String userName, String userLevel, String userLevelImage, String roadTitle, String favoriteIcon, String liked, Map<Object, Object> dtrRoadLine) {
        this.textRoad = textRoad;
        this.userName = userName;
        this.userLevel = userLevel;
        this.userLevelImage = userLevelImage;
        this.roadTitle = roadTitle;
        this.favoriteIcon = favoriteIcon;
        this.liked = liked;
        //this.dtrRoadLine = dtrRoadLine;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getDtrRating() {
        return dtrRating;
    }

    public void setDtrRating(int dtrRating) {
        this.dtrRating = dtrRating;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public String getTextRoad() {
        return textRoad;
    }

    public void setTextRoad(String roadText) {
        this.textRoad = roadText;
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

    public String getRoadTitle() {
        return roadTitle;
    }

    public void setRoadTitle(String roadTitle) {
        this.roadTitle = roadTitle;
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

    public String getRoadComment() {
        return roadComment;
    }

    public void setRoadComment(String roadComment) {
        this.roadComment = roadComment;
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

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    /*public Map<Object, Object> getDtrRoadLine() {
        return dtrRoadLine;
    }

    public void setDtrRoadLine(Map<Object, Object> dtrRoadLine) {
        this.dtrRoadLine = dtrRoadLine;
    }*/

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }
}
