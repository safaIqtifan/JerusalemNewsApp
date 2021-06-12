package com.example.jerusalemnewsapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jerusalemnewsapp.rclass.Constant;

public class BaseActivity extends AppCompatActivity {

    public void adjustFontScale(Configuration configuration) {

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        configuration.fontScale = app_preferences.getFloat("font_size", Constant.NORMAL_FONT);
//        configuration.fontScale = 1.5f;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());

    }

}
