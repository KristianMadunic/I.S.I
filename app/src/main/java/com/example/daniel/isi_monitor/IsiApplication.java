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
    private final Map<String, InfusionModelView> modelViews = Collections.synchronizedMap(new HashMap<String, InfusionModelView>());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean AddInfusion(String name, String location, String deviceId, String accessToken) {
        if(modelViews.containsKey(name)) {
            return false;
        }

        InfusionModelView modelView = new InfusionModelView(name, location, deviceId, accessToken);

        modelViews.put(name, modelView);
        return true;
    }

    public void DeleteInfusion(String name) {
        modelViews.remove(name);
    }

    public void UpdateInfusions() {
        Log.d("IsiApplication", "UpdateInfusions");
        // Synchronizing on map, not on set
        synchronized (modelViews) {
            // Iterator must be in synchronized block
            for(InfusionModelView modelView : modelViews.values()) {
                modelView.Update();
            }
        }
    }
}
