package com.example.rotory;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainPage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_page, container, false);
       initUI(rootView);
       return rootView;

    }

    private void initUI(ViewGroup rootView) {

          /*MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.bigMapLayout);
        mapViewContainer.addView(mapView);*/



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




