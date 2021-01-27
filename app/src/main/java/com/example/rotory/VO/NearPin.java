package com.example.rotory.VO;

import com.google.android.gms.maps.model.LatLng;


public class NearPin {
    double distance;
    LatLng point;
    String documentId;


    public NearPin() {
    }

    public NearPin(double distance, LatLng point, String documentId){
        this.distance = distance;
        this.point = point;
        this.documentId = documentId;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


}