package com.example.jerusalemnewsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.jerusalemnewsapp.Adapter.NewsAdapter;
import com.example.jerusalemnewsapp.Model.ArticlesModel;
import com.example.jerusalemnewsapp.Model.ResultsModel;
import com.example.jerusalemnewsapp.rclass.Constant;
import com.example.jerusalemnewsapp.rclass.WorkManagerRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoritesActivity extends BaseActivity {

    SwipeRefreshLayout swipeToRefreshLY;
    RecyclerView JerusalemRV;
    List<ArticlesModel> articlesModels;
    NewsAdapter adapter;


    SharedPreferences app_preferences;
    int appTheme;
    int appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);

        if (appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(appTheme);
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.favorite));
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(appColor);


        swipeToRefreshLY = findViewById(R.id.swipeToRefreshLY);
        JerusalemRV = findViewById(R.id.JerusalemRV);

        JerusalemRV.setLayoutManager(new LinearLayoutManager(this));
        app_preferences = getSharedPreferences("JerusalemShred", Context.MODE_PRIVATE);

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AddPost.class));
//
//            }
//        });
        swipeToRefreshLY.setEnabled(false);

        articlesModels = RootApplication.dbRealm.where(ArticlesModel.class).findAll();
//        articlesModels = new ArrayList<>();

        adapter = new NewsAdapter(this, articlesModels, true);
        JerusalemRV.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_video) {
            startActivity(new Intent(FavoritesActivity.this, VideoActivity.class));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new VideoFragment()).commit();
            return true;
        }

        if (id == R.id.action_about_jerusalem) {
            startActivity(new Intent(FavoritesActivity.this, AboutJerusalemActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(FavoritesActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}