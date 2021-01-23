package com.example.rotory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressDialogs extends Dialog {
        ProgressBar progressBar;
        public ProgressDialogs(@NonNull Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.fragment_blank);
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
