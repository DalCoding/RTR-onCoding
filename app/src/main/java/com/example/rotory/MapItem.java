package com.example.rotory;

import java.io.Serializable;
import java.util.ArrayList;

public class MapItem implements Serializable {
    ArrayList<String> dtrName;
    ArrayList<String> dtrLatLng;
    ArrayList<String> address;

    public MapItem(ArrayList<String> dtrName, ArrayList<String> dtrLatLng, ArrayList<String> address) {
        this.dtrName = dtrName;
        this.dtrLatLng = dtrLatLng;
        this.address = address;
    }

    public MapItem() {}

    public ArrayList<String> getDtrName() {
        return dtrName;
    }

    public void setDtrName(ArrayList<String> dtrName) {
        this.dtrName = dtrName;
    }

    public ArrayList<String> getDtrLatLng() {
        return dtrLatLng;
    }

    public void setDtrLatLng(ArrayList<String> dtrLatLng) {
        this.dtrLatLng = dtrLatLng;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }
}
