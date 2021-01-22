package com.example.rotory.Theme;

import com.google.firebase.firestore.DocumentReference;

public class Tags {
    String tag;


    public Tags(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
