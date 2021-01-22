package com.example.rotory.google;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity/* implements View.OnClickListener */{
    Context context;
    private Button tab1, tab2, tab3;
    private Fragment fragment = null;

    /*PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(context, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getBaseContext();
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(context)
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                            //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            //android.Manifest.permission.WRITE_EXTERNAL_STORAGE // 기기, 사진, 미디어, 파일 엑세스 권한
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView() {
        tab1 = findViewById(R.id.btn_tab1);
        tab2 = findViewById(R.id.btn_tab2);
        tab3 = findViewById(R.id.btn_tab3);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);

        if(findViewById(R.id.fragment_container) != null){
            Fragment01 fragment01 = new Fragment01();
            fragment01.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment01).commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tab1:
                fragment = new Fragment01();
                selectFragment(fragment);
                break;
            case R.id.btn_tab2:
                fragment = new Fragment02();
                selectFragment(fragment);
                break;
            case R.id.btn_tab3:
                fragment = new Fragment03();
                selectFragment(fragment);
                break;
        }
    }

    private void selectFragment(Fragment fragment) {
        // 액티비티 내의 프래그먼트를 관리하려면 FragmentManager를 사용해야 함.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        // FragmentTransaction을 변경하고 나면, 반드시 commit()을 호출해야 변경 내용이 적용됨
        fragmentTransaction.commit();
    }*/
}