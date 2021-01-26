package com.example.rotory;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class ProgressDialogs extends Dialog {
    ProgressBar progressBar;
    public ProgressDialogs(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress);
        progressBar = findViewById(R.id.progressBar);
        try{
            for (int i = 0; i < 5; i ++){
                progressBar.setProgress(i *30);
                Thread.sleep(500);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}