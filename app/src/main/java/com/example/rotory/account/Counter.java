package com.example.rotory.account;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.rotory.VO.AppConstant;

public class Counter{
    private static CountDownTimer countDownTimer;
    static AppConstant appConstant;
    private final static String TAG = "counter";

    public Counter() {
    }

    public CountDownTimer countDownTimer(TextView counter) {

        countDownTimer= new CountDownTimer(appConstant.MILLISINFUTURE, appConstant.COUNT_DOWN_INTERVAL){

            @Override
            public void onTick(long millisUntilFinished) {
                long phoneAuthCount = millisUntilFinished/1000;
                Log.d(TAG, phoneAuthCount + "");

                if ((phoneAuthCount - ((phoneAuthCount / 60) * 60)) >= 10){
                    counter.setText(((phoneAuthCount)/60) + ":" + (phoneAuthCount - ((phoneAuthCount / 60) * 60))) ;
                }else {
                    counter.setText(((phoneAuthCount)/60) + ":0" + (phoneAuthCount - ((phoneAuthCount / 60) * 60)));
                }

            }

            @Override
            public void onFinish() {
                counter.setText("3:00");
            }
        };


        return countDownTimer;

    }
    public static void stopCounter(TextView textView){
        countDownTimer.cancel();
        textView.setText("3:00");
    }
}
