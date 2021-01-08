package com.example.rotory.VO;

import java.util.ArrayList;

public class UserActivity {
    String content_id;
    String scrapedTime;
    int contentType;
    ArrayList<String> liked;
    ArrayList<String> inforWritten;
    ArrayList<String> stared;
    ArrayList<String> scraped;

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getScrapedTime() {
        return scrapedTime;
    }

    public void setScrapedTime(String scrapedTime) {
        this.scrapedTime = scrapedTime;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    public ArrayList<String> getInforWritten() {
        return inforWritten;
    }

    public void setInforWritten(ArrayList<String> inforWritten) {
        this.inforWritten = inforWritten;
    }

    public ArrayList<String> getStared() {
        return stared;
    }

    public void setStared(ArrayList<String> stared) {
        this.stared = stared;
    }

    public ArrayList<String> getScraped() {
        return scraped;
    }

    public void setScraped(ArrayList<String> scraped) {
        this.scraped = scraped;
    }
}
