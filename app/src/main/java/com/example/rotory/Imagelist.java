package com.example.rotory;

import android.net.Uri;


public class Imagelist{
    private String smallimage;

    public Imagelist(String smallimage) {
        this.smallimage = smallimage;
    }


    public String getSmallimage() {
        return smallimage;
    }

    public void setSmallimage(String smallimage) {
        this.smallimage = smallimage;
    }
}