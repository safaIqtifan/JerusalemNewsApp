package com.example.jerusalemnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ProgressBar loadingLY;
    RecyclerView JerusalemRV;
    SwipeRefreshLayout swipeRefreshLY;
    FloatingActionButton floatingActionButton;
    List<ArticlesModel> articlesModels;
    NewsAdapter adapter;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidNetworking.initialize(getApplicationContext());

        loadingLY = findViewById(R.id.loadingLY);
        swipeRefreshLY = findViewById(R.id.swipeToRefreshLY);
        JerusalemRV = findViewById(R.id.JerusalemRV);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        JerusalemRV.setLayoutManager(new LinearLayoutManager(this));
        sharedPref = getSharedPreferences("JerusalemShred", Context.MODE_PRIVATE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddPost.class));

            }
        });

        swipeRefreshLY.setColorSchemeColors(ContextCompat.getColor(this, R.color.teal_200));
        swipeRefreshLY.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(false);
            }
        });

        articlesModels = new ArrayList<>();

        adapter = new NewsAdapter(this, articlesModels);
        JerusalemRV.setAdapter(adapter);

        fetchData(true);


//        WorkRequest periodicWork =new PeriodicWorkRequest.Builder(WorkManagerRequest.class,1, TimeUnit.HOURS)
//        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(WorkManagerRequest.class, 10, TimeUnit.SECONDS)
//                .build();
        OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(WorkManagerRequest.class)
//                        .setInitialDelay(10, TimeUnit.SECONDS)
                        .build();
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueueUniqueWork("send_notification", ExistingWorkPolicy.KEEP,workRequest);
//        workManager.enqueueUniquePeriodicWork("send_notification", ExistingPeriodicWorkPolicy.KEEP,periodicWork);


    }

    private void setPrefInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    public void fetchData(boolean showLoading) {

        if (showLoading) {
            loadingLY.setVisibility(View.VISIBLE);
            swipeRefreshLY.setVisibility(View.GONE);
        }

        String currentDate = getDateOnlyNowString();

        AndroidNetworking.get("https://newsapi.org/v2/everything")
                .addQueryParameter("language", "ar")
                .addQueryParameter("q", "القدس")
                .addQueryParameter("apiKey", "6653de81bfac4745a5f5561ddcec9ffa")
//                .addQueryParameter("from", "2021-05-15")
                .addQueryParameter("to", currentDate)
                .addQueryParameter("sortBy", "popularity")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ResultsModel.class, new ParsedRequestListener<ResultsModel>() {

                    @Override
                    public void onResponse(ResultsModel response) {
                        swipeRefreshLY.setRefreshing(false);
                        loadingLY.setVisibility(View.GONE);
                        swipeRefreshLY.setVisibility(View.VISIBLE);

                        articlesModels.clear();
                        articlesModels.addAll(response.articles);

                        adapter.list = articlesModels;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        loadingLY.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "fail to get data", Toast.LENGTH_SHORT).show();
                        System.out.println("Log " + anError.getErrorBody());

                    }
                });
    }

    public String getDateOnlyNowString() {
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            // Date s = parser.parse("");
            return parser.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String formatDate(Object o, String inPattern, String outPattern) {
        SimpleDateFormat parser = new SimpleDateFormat(inPattern, Locale.ENGLISH);
        SimpleDateFormat formater = new SimpleDateFormat(outPattern, Locale.ENGLISH);
        try {
            Date date = parser.parse(o.toString());
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}