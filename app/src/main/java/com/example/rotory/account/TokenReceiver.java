package com.example.rotory.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public abstract class TokenReceiver {

/* extends BroadcastReceiver {
    private static final String TAG = "TokenReceiver";
    private static final String ACTION_TOKEN = "com.example.ACTION_TOKEN";
    //확인하기
    private static final String EXTRA_KEY_TOEKN = "key_token";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:" + intent);

        if (ACTION_TOKEN.equals(intent.getAction())){
            String token = intent.getExtras().getString(EXTRA_KEY_TOEKN);
            onNewToken(token);
        }
    }

    public static IntentFilter getFilter(){
        IntentFilter filter = new IntentFilter(ACTION_TOKEN);
        return filter;
    }

    public abstract void onNewToken(String token);*/

}
