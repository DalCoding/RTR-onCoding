package com.example.rotory;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

public class Comment {
    Image levelImg;
    String userName;
    String time;
    String commCon;
    String report;

    public Image getLevelImg() {
        return levelImg;
    }

    public void setLevelImg(Image levelImg) {
        this.levelImg = levelImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommCon() {
        return commCon;
    }

    public void setCommCon(String commCon) {
        this.commCon = commCon;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Comment(Image levelImg, String userName, String time, String commCon, String report) {
        this.levelImg = levelImg;
        this.userName = userName;
        this.time = time;
        this.commCon = commCon;
        this.report = report;
    }
}
