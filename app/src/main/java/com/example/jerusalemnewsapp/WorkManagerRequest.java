package com.example.jerusalemnewsapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkManagerRequest extends Worker {

    MyFirebaseMessagingService myFirebaseMessagingService =
            new MyFirebaseMessagingService();

    public WorkManagerRequest(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        fetchData();
        return Result.success();
    }

    public void fetchData() {

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

                        List<ArticlesModel>  articlesModels = response.articles;
                        Collections.shuffle(articlesModels);
                        ArticlesModel firstIndex = articlesModels.get(0);

                        myFirebaseMessagingService.sendNotification(firstIndex.title, firstIndex.description);

                    }

                    @Override
                    public void onError(ANError anError) {
//                        Toast.makeText(MainActivity.this, "fail to get data", Toast.LENGTH_SHORT).show();
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

}
