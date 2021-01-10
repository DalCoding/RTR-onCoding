package com.example.rotory.VO;

public class Tag {
    int tag_id;
    public String  tag;
    String  tagImage;


    public Tag(String tag) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
