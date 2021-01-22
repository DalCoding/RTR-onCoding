package com.example.rotory.Theme;

import java.util.ArrayList;
import java.util.Map;

public class TagList {
    public String tagListTitle;
    public ArrayList<Tags> tags;

    public TagList() {
    }

    public String getTagListTitle() {
        return tagListTitle;
    }

    public void setTagListTitle(String tagListTitle) {
        this.tagListTitle = tagListTitle;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }
}
