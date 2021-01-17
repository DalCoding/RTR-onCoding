package com.example.rotory.VO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Tag {
    int tag_id;
    ArrayList<String> tag;
    String  tagImage;

    public Tag() { }

    public Tag(int tag_id, ArrayList<String> tag, String tagImage) {
        this.tag_id = tag_id;
        this.tag = tag;
        this.tagImage = tagImage;
    }

    public Tag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

}
