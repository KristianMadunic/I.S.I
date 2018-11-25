package com.example.daniel.isi_monitor;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class IsiApplication extends Application {
    InfusionModelViewContainer modelViewContainer;

    Handler handler = new Handler();

    final int updateInterval = 10000;

    Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        modelViewContainer = new InfusionModelViewContainer(this);

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something
                modelViewContainer.UpdateInfusions();

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

    public InfusionModelViewContainer GetModelViewContainer() {
        return modelViewContainer;
    }
}
