package com.example.daniel.isi_monitor;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public void SendNotification(String message) {
        // TODO
    }

    public InfusionModelViewContainer GetModelViewContainer() {
        return modelViewContainer;
    }
}
