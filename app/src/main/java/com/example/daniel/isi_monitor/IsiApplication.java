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
        if(modelViews.containsKey(deviceId)) {
            return false;
        }

        InfusionModelView modelView = new InfusionModelView(new InfusionModel(name, location, deviceId, accessToken), this);

        modelViews.put(deviceId, modelView);
        return true;
    }

    public void DeleteInfusion(String deviceId) {
        InfusionModelView modelView = modelViews.get(deviceId);
        if(null != modelView) {
            modelView.Remove();
            modelViews.remove(deviceId);
        }
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

    public void AddInfusionData(String deviceId, int priority, double weight, String name, String location, String title, String description) {
        // TODO
    }

    public void RemoveInfusionData(String deviceId) {
        // TODO
    }

    public void UpdateInfusionData(String deviceId, int priority, double weight) {
        // TODO
    }

    public void SendNotification(String message) {
        // TODO
    }
}
