package com.example.rotory.VO;

import java.util.ArrayList;

public class Road {
    int	road_id;
    String	roadTitle;
    int	user_id;
    ArrayList<String> tag;
    String	hour;
    String	min;
    String	isPartner;
    int	dtrRating;
    String	ratingComment;
    int	isPublic;
    ArrayList<Integer>	dtrRoadLine;
    ArrayList<String>	dtrName;
    String	writeDate;
    String	modifiedDate;

    public int getRoad_id() {
        return road_id;
    }

    public void setRoad_id(int road_id) {
        this.road_id = road_id;
    }

    public String getRoadTitle() {
        return roadTitle;
    }

    public void setRoadTitle(String roadTitle) {
        this.roadTitle = roadTitle;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
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
}
