package com.example.rotory;

import android.media.Image;

import java.util.Map;

public class Comment {

   String comment;
    String contentsId;
    String commentType;
    String personId;
    String savedDate;
    String uid;

    public Comment() {
    }

    public Comment(String comment, String contentsId,String commentType, String personId, String savedDate, String uid) {
        this.comment = comment;
        this.contentsId = contentsId;
        this.commentType = commentType;
        this.personId = personId;
        this.savedDate = savedDate;
        this.uid = uid;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
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


    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;

    }
}

