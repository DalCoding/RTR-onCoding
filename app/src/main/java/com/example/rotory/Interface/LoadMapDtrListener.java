package com.example.rotory.Interface;

import com.google.android.gms.maps.model.LatLng;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public interface LoadMapDtrListener {
    public void loadDtr(LatLng point);
    public void loadDtrLine(MapView mapView, ArrayList<MapPoint> Points);
  //  public void showDtrLineNum();
}
