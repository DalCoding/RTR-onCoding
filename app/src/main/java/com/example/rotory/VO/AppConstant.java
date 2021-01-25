package com.example.rotory.VO;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.rotory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

public class AppConstant {
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String TAG = "AppConstant";
    public static final int loginCode = 3000;
    public static final int themeCode = 2000;
    public static final int mainCode = 1000;
    public static final int searchCode = 1100;
    public static final int profileEditCode = 4120;
    public static final int findWithPhoneCode = 3221;

    public static final int MILLISINFUTURE = 120 * 1000; //총 시간
    public static final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    public static final String REGEX_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    public static final String REGEX_NUMBER = "^(?=.*[0-9])[0-9]{9,12}$";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

    public AppConstant() {
    }

    public int getUserLevelImage(String userLevel) {
        switch (userLevel){
            case "어린다람쥐":
                return R.drawable.level2;
            case "학생다람쥐":
                return R.drawable.level3;
            case "어른다람쥐" :
                return R.drawable.level4;
            case "박사다람쥐" :
                return R.drawable.level5;
            case "다람쥐의 신":
                return R.drawable.level6;
            default:
                return R.drawable.level1;
        }
    }

    public void getThemeImage(String tagText, ImageView imageView, Context context) {
        Log.d(TAG, tagText + imageView);
        db.collection("tag").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                Map<String, Object> allTagList = snapshot.getData();
                                Log.d(TAG,"태그 정보" + allTagList);
                                String getKeyString = getKey(allTagList,tagText);
                                Log.d(TAG,"키에 맞는 밸류 값 확인?" + getKeyString);
                                if (allTagList.get(getKeyString) != null){
                                    Log.d(TAG ,allTagList.get(getKeyString).toString());
                                    String path = "tags/"+ getKey(allTagList, tagText)+".png";
                                    storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d(TAG,"storage에서 이미지 가져오기 성공" +uri);
                                            Glide.with(context)
                                                    .load(uri)
                                                    .into(imageView);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"storage에서 이미지 가져오기 실패" +e.toString() );
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }


    public static String getKey(Map<String, Object> map, Object value){
        for (String key : map.keySet()){
            if (value.equals(map.get(key))){
                return key;
            }
        }

        return null;
    }

}
