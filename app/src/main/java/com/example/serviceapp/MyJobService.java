package com.example.serviceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobService extends Service {
    private static final String TAG = MyJobService.class.getSimpleName();
    public int counter = 0;
    public String reply;
    private Timer timer;
    private TimerTask timerTask;

    public MyJobService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        Log.d("Started", "Service started");
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification(String reply) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel("channelId") == null) {
            NotificationChannel notificationChannel = (new NotificationChannel("channelId", "Channel Name",
                    NotificationManager.IMPORTANCE_HIGH));
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription("Channel Description");
            notificationChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(getApplicationContext(), "channelId");
        builder.setContentTitle("Title")
                .setContentText(reply)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTimeoutAfter(2000);
        return builder.build();
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startTimer() {
        Log.d(TAG, "startTimer: timer started");
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        timer.schedule(timerTask, Calendar.getInstance().getTime(), 10000);
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "run: Timer task initialized");
                counter++;
                startForeground(123, createNotification("Reply " + counter));
            }
        };
    }

    // I couldn't manage to make the notification display the reply from API
    public String getReply() {
        ApiClient.get().apiCall(counter).enqueue(new Callback<ApiResponse>() {
            String reply = "";

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body());
                    } else {
                        reply = "Error";
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
        return reply;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Destroyed");
        stopTimerTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
