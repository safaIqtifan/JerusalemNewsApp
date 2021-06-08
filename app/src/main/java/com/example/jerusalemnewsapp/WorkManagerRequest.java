package com.example.jerusalemnewsapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
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
import java.util.concurrent.TimeUnit;

public class WorkManagerRequest extends Worker {

    //    MyFirebaseMessagingService myFirebaseMessagingService =
//            new MyFirebaseMessagingService();
    Context context;
    int count;

    public WorkManagerRequest(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
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

                        List<ArticlesModel> articlesModels = response.articles;
                        Collections.shuffle(articlesModels);
                        ArticlesModel firstIndex = articlesModels.get(0);

                        sendNotification(firstIndex.title, firstIndex.description);
                        Toast.makeText(context, "New Notificxation ", Toast.LENGTH_SHORT).show();

                        OneTimeWorkRequest workRequest =
                                new OneTimeWorkRequest.Builder(WorkManagerRequest.class)
                                        .setInitialDelay(10, TimeUnit.SECONDS)
                                        .build();
                        WorkManager workManager = WorkManager.getInstance(context);
                        workManager.enqueueUniqueWork("send_notification", ExistingWorkPolicy.KEEP,workRequest);
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

    public void sendNotification(String messageTitle, String messageBody) {

        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Que Notifications";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("My Notifications Service");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.MAGENTA);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
//            notificationChannel.setSound(defaultSoundUri);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setSound(defaultSoundUri)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(messageTitle)
                .setContentText(messageBody);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());

    }


}
