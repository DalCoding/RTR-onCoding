package com.example.rotory.VO;

import com.google.android.gms.maps.model.LatLng;


public class NearPin  {
    double distance;
    LatLng point;


    public NearPin() {
    }

    public NearPin(double distance, LatLng point){
        this.distance = distance;
        this.point = point;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

  /*  @Override
    public int compareTo(NearPin nearPin) {
        if (this.distance == nearPin.distance ) {
            return 0;
        } else if (this.distance < nearPin.distance){
            return -1;
        } else {
            return 1;
        }
        //return Double.toString(this.distance).compareTo(Double.toString(nearPin.distance));
        //return "하이".compareTo("하이용");
    } */

}