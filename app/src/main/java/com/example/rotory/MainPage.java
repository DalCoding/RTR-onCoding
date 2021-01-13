package com.example.rotory;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainPage extends Fragment {

    public static MainPage newInstance() {
        return new MainPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_page, container, false);
       initUI(rootView);
       return rootView;

    }

    private void initUI(ViewGroup rootView) {




      /*  MapView mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        mapViewContainer.addView(mapView);

       */
        /*MapView mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        mapViewContainer.addView(mapView);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.541258, 126.838193), 2, true);*/
/*
        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
        mainMapExtendBtn.bringToFront();
        mainMapExtendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), BigMapPage.class);
                startActivity(intent);
            }
        });

        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
        mainMapExtendBtn.bringToFront();
        mainMapExtendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(BigMapPage.newInstance());
            }
        });
*/

        // 디버그 키 해시 구하기 (카카오 맵 API 연동 시 필요), 맵 코드 주석 처리 후 실행! (애뮬에서 돌리면 실행 오류 날 수 있음)
        /*try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.example.rotory", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("Key Hash : ", str);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}




