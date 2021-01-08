package com.example.rotory.VO;

import java.util.HashMap;

public class Comment {

    int content_id;

    HashMap<String, String> comment;

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public HashMap<String, String> getComment() {
        return comment;
    }

    public void setComment(HashMap<String, String> comment) {
        this.comment = comment;
    }
}
