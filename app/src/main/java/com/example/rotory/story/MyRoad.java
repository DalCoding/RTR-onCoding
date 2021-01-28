package com.example.rotory.story;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;

public class MyRoad {

    private int contentsType;
    private ArrayList<String> dtrName;
    private String title;
    private ArrayList<String> dtrAddress;

    public MyRoad() {}

    public MyRoad(int contentsType, ArrayList<String> dtrName, String title, ArrayList<String> dtrAddress) {
        this.contentsType = contentsType;
        this.dtrName = dtrName;
        this.title = title;
        this.dtrAddress = dtrAddress;
    }

    public ArrayList<String> getDtrName() {
        return dtrName;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public void setDtrName(ArrayList<String> dtrName) {
        this.dtrName = dtrName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getDtrAddress() {
        return dtrAddress;
    }

    public void setAddress(ArrayList<String> dtrAddress) {
        this.dtrAddress = dtrAddress;
    }
}
