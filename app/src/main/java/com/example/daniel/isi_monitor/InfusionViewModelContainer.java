package com.example.daniel.isi_monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class InfusionViewModelContainer extends Observable {
    private final Map<Integer, InfusionViewModel> ViewModels = Collections.synchronizedMap(new HashMap<Integer, InfusionViewModel>());

    IsiApplication app;

    InfusionViewModelContainer(IsiApplication app) {
        this.app = app;
    }

    public boolean AddInfusion(String name, String location, String deviceId, String accessToken) {
        if(ViewModels.containsKey(deviceId)) {
            return false;
        }

        InfusionViewModel ViewModel = new InfusionViewModel(new InfusionModel(name, location, deviceId, accessToken), app);

        ViewModels.put(ViewModel.GetId(), ViewModel);

        setChanged();

        notifyObservers();

        return true;
    }

    public void DeleteInfusion(int id) {
        InfusionViewModel ViewModel = ViewModels.get(id);
        if(null != ViewModel) {
            ViewModels.remove(id);
        }

        setChanged();

        notifyObservers();
    }

    public void UpdateInfusions() {
        synchronized (ViewModels) {
            // Iterator must be in synchronized block
            for(InfusionViewModel ViewModel : ViewModels.values()) {
                ViewModel.Update();
            }
        }

        setChanged();

        notifyObservers();
    }

    public ArrayList<InfusionViewModel> GetViewModels() {
        ArrayList<InfusionViewModel> list = new ArrayList<InfusionViewModel>(ViewModels.values());
        Collections.sort(list);
        return list;
    }

    public InfusionViewModel GetViewModel(Integer id) {
        return ViewModels.get(id);
    }
}
