package com.example.rotory.Search;

import java.util.Map;

public class Tag {
    Map<String, Object> activity;
    Map<String, Object> feel;
    Map<String, Object> mood;
    Map<String, Object> place;
    Map<String, Object> season;
    Map<String, Object> time;

    public Tag() { }

    public Tag(Map<String, Object> activity, Map<String, Object> feel, Map<String, Object> mood, Map<String, Object> place, Map<String, Object> season, Map<String, Object> time) {
        this.activity = activity;
        this.feel = feel;
        this.mood = mood;
        this.place = place;
        this.season = season;
        this.time = time;
    }

    public Map<String, Object> getActivity() {
        return activity;
    }

    public void setActivity(Map<String, Object> activity) {
        this.activity = activity;
    }

    public Map<String, Object> getFeel() {
        return feel;
    }

    public void setFeel(Map<String, Object> feel) {
        this.feel = feel;
    }

    public Map<String, Object> getMood() {
        return mood;
    }

    public void setMood(Map<String, Object> mood) {
        this.mood = mood;
    }

    public Map<String, Object> getPlace() {
        return place;
    }

    public void setPlace(Map<String, Object> place) {
        this.place = place;
    }

    public Map<String, Object> getSeason() {
        return season;
    }

    public void setSeason(Map<String, Object> season) {
        this.season = season;
    }

    public Map<String, Object> getTime() {
        return time;
    }

    public void setTime(Map<String, Object> time) {
        this.time = time;
    }
}
