package com.example.rotory.VO;

import net.daum.mf.map.api.MapPoint;

public class NearPin  {
    double distance;
    MapPoint point;


    public NearPin() {
    }

    public NearPin(double distance, MapPoint point){
        this.distance = distance;
        this.point = point;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public MapPoint getPoint() {
        return point;
    }

    public void setPoint(MapPoint point) {
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