package com.example.rotory;

import android.media.Image;

import java.util.Map;

public class Comment {
    String comment;
    String contentsId;
    int contentsType;
    String personId;
    String uid;
    String savedDate;
    Map<String, Object> report;

    public Comment(String comment, String contentsId, int contentsType, String personId, String uid, String savedDate, Map<String, Object> report) {
        this.comment = comment;
        this.contentsId = contentsId;
        this.contentsType = contentsType;
        this.personId = personId;
        this.uid = uid;
        this.savedDate = savedDate;
        this.report = report;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContentsId() {
        return contentsId;
    }

    public void setContentsId(String contentsId) {
        this.contentsId = contentsId;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public Map<String, Object> getReport() {
        return report;
    }

    public void setReport(Map<String, Object> report) {
        this.report = report;
    }
}

