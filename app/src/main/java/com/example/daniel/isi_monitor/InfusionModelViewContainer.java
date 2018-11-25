package com.example.daniel.isi_monitor;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class InfusionModelViewContainer extends Observable {
    private final Map<String, InfusionModelView> modelViews = Collections.synchronizedMap(new HashMap<String, InfusionModelView>());

    IsiApplication app;

    InfusionModelViewContainer(IsiApplication app) {
        this.app = app;
    }

    public boolean AddInfusion(String name, String location, String deviceId, String accessToken) {
        if(modelViews.containsKey(deviceId)) {
            return false;
        }

        InfusionModelView modelView = new InfusionModelView(new InfusionModel(name, location, deviceId, accessToken), app);

        modelViews.put(deviceId, modelView);

        setChanged();

        notifyObservers();

        return true;
    }

    public void DeleteInfusion(String deviceId) {
        InfusionModelView modelView = modelViews.get(deviceId);
        if(null != modelView) {
            modelViews.remove(deviceId);
        }

        setChanged();

        notifyObservers();
    }

    public void UpdateInfusions() {
        synchronized (modelViews) {
            // Iterator must be in synchronized block
            for(InfusionModelView modelView : modelViews.values()) {
                modelView.Update();
            }
        }

        setChanged();

        notifyObservers();
    }

    public ArrayList<InfusionModelView> GetModelViews() {
        ArrayList<InfusionModelView> list = new ArrayList<InfusionModelView>(modelViews.values());
        Collections.sort(list);
        return list;
    }
}
