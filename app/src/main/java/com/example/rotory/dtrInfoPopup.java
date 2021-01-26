package com.example.rotory;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class dtrInfoPopup extends AppCompatActivity {
    private static final String TAG = "dtrInfoPage";

    TextView placeName;
    TextView address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_info_page);

        placeName = findViewById(R.id.dinfoSignText);
        address = findViewById(R.id.dinfoAdText);


        ArrayList<String> dtrName = new ArrayList<>();
        ArrayList<String> dtrLatLng = new ArrayList<>();


        // db에서 가져온 후 위도 경도로 변환 필요

        Geocoder geocoder = new Geocoder(this);

        /*try {
            List<Address> addresses = geocoder.getFromLocation(dtrLatLng.latitude(), dtrLatLng.longitude(), 1);
            Log.d(TAG, "onComplete:" + addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }*/
    }
}
