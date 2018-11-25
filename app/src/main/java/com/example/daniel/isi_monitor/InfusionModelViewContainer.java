package com.example.daniel.isi_monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class InfusionModelViewContainer extends Observable {
    private final Map<Integer, InfusionModelView> modelViews = Collections.synchronizedMap(new HashMap<Integer, InfusionModelView>());

    IsiApplication app;

    InfusionModelViewContainer(IsiApplication app) {
        this.app = app;
    }

    public boolean AddInfusion(String name, String location, String deviceId, String accessToken) {
        if(modelViews.containsKey(deviceId)) {
            return false;
        }

        InfusionModelView modelView = new InfusionModelView(new InfusionModel(name, location, deviceId, accessToken), app);

        modelViews.put(modelView.GetId(), modelView);

        setChanged();

        notifyObservers();

        return true;
    }

    public void DeleteInfusion(int id) {
        InfusionModelView modelView = modelViews.get(id);
        if(null != modelView) {
            modelViews.remove(id);
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
