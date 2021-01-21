package com.example.rotory.Interface;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public interface LoadMapDtrListener {
    public void loadDtr(MapView mapView, MapPoint point);
    public void loadDtrLine(MapView mapView, ArrayList<MapPoint> Points);
  //  public void showDtrLineNum();
}
