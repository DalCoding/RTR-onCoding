package com.example.rotory.story;

import com.google.gson.internal.$Gson$Preconditions;

public class MyRoad {

    int contentsType;
    String dtrName;
    String title;
    String address;

    public MyRoad() {}

    public MyRoad(int contentsType, String dtrName, String title, String address) {
        this.contentsType = contentsType;
        this.dtrName = dtrName;
        this.title = title;
        this.address = address;
    }

    public String getDtrName() {
        return dtrName;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public void setDtrName(String dtrName) {
        this.dtrName = dtrName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
