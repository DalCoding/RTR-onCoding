package com.example.rotory.kakao;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String TAG = "ApiClient";

    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit == null){
            Log.d(TAG,"Api 클라이언트 받아옴");
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d(TAG,"Api 클라이언트 받아옴" + retrofit.toString());
        }
        return  retrofit;
    }


}
