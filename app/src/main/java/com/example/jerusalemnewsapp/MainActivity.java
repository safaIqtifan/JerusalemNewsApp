package com.example.jerusalemnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ProgressBar loadingLY;
    RecyclerView JerusalemRV;
    SwipeRefreshLayout swipeRefreshLY;

    List<ArticlesModel> articlesModels;
    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidNetworking.initialize(getApplicationContext());

        loadingLY = findViewById(R.id.loadingLY);
        swipeRefreshLY = findViewById(R.id.swipeToRefreshLY);
        JerusalemRV = findViewById(R.id.JerusalemRV);

        JerusalemRV.setLayoutManager(new LinearLayoutManager(this));

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
        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(WorkManagerRequest.class)
                        .build();

        WorkManager.getInstance(this).enqueue(workRequest);



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