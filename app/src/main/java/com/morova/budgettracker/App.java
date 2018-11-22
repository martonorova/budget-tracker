package com.morova.budgettracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "limit_warning";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel limitWarningChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Limit warning",
                    NotificationManager.IMPORTANCE_HIGH
            );
            limitWarningChannel.setDescription("A channel to warn if you spent 80% of the limit");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(limitWarningChannel);
        }
    }
}
