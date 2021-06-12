package com.example.jerusalemnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.jerusalemnewsapp.rclass.Constant;
import com.example.jerusalemnewsapp.rclass.Methods;
import com.example.jerusalemnewsapp.rclass.WorkManagerRequest;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

public class Settings extends AppCompatActivity {

    SharedPreferences sharedPreferences, app_preferences;
    SharedPreferences.Editor editor;
    View selectColorBtn;
    Button saveBtn;
    Switch notificationS;
    RadioButton smallRB, normalRB, largeRB;
    Methods methods;

    int appTheme;
    //    int themeColor;
    int appColor;
    float appFontScale;
//    Constant constant;
    WorkManagerRequest workManagerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
//        themeColor = appColor;
//        Constant.color = appColor;

        if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_settings);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("Settings");
        toolbar.setBackgroundColor(Constant.color);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        methods = new Methods();
        selectColorBtn = findViewById(R.id.button_color);
        saveBtn = findViewById(R.id.saveBtn);
        smallRB = findViewById(R.id.smallRB);
        normalRB = findViewById(R.id.normalRB);
        largeRB = findViewById(R.id.largeRB);
        notificationS = findViewById(R.id.notification_switch);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        colorize();

        selectColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(Settings.this);
                dialog.setTitle("Select");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {


                        appColor = color;
                        colorize();
//                        int styleId;
                        appTheme = methods.setColorTheme(appColor);

//                        Intent intent = new Intent(Settings.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });

                dialog.show();
            }
        });

        smallRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFontScale = Constant.SMALL_FONT;
            }
        });

        normalRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFontScale = Constant.NORMAL_FONT;
            }
        });

        largeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFontScale = Constant.LARGE_FONT;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAppTheme();
                setFontScale();

                Intent intent = new Intent(Settings.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        appFontScale = sharedPreferences.getFloat("font_size", Constant.NORMAL_FONT);
        if (appFontScale == Constant.SMALL_FONT)
            smallRB.setChecked(true);
        else if (appFontScale == Constant.LARGE_FONT)
            largeRB.setChecked(true);
        else
            normalRB.setChecked(true);


        notificationS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManagerRequest = new WorkManagerRequest(Settings.this);
            }
        });


    }






    private void setAppTheme() {

        editor.putInt("color", appColor);
        editor.putInt("theme", appTheme);
        editor.apply();
        editor.commit();

    }

    private void setFontScale() {

        editor.putFloat("font_size", appFontScale);
        editor.apply();
        editor.commit();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        System.out.println("Log appColor " + appColor);
        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(appColor);

        selectColorBtn.setBackground(d);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}