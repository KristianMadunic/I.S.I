package com.example.daniel.isi_monitor;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class IsiApplication extends Application {
    InfusionViewModelContainer ViewModelContainer;

    Handler handler = new Handler();

    final int updateInterval = 1000;

    Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        ViewModelContainer = new InfusionViewModelContainer(this);

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something
                ViewModelContainer.UpdateInfusions();

                handler.postDelayed(runnable, updateInterval);
            }
        }, 1);
    }

    public void SendNotification(String message, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.infusion_icon)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(id, mBuilder.build());
    }

    public InfusionViewModelContainer GetViewModelContainer() {
        return ViewModelContainer;
    }
}
