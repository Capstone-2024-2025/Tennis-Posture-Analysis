package com.example.tennispostureanalysis;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class ScreenCaptureService extends Service {
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "screen_capture";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Screen Capture", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Screen Capture")
                .setContentText("Recording in progress")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        startForeground(NOTIFICATION_ID, notification);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
