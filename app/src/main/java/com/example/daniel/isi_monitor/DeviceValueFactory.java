package com.example.daniel.isi_monitor;

import org.json.JSONObject;

public class DeviceValueFactory {

    public double FromJsonString(String jsonString) {
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            return mainObject.getDouble("result");
        } catch(Exception e) {

        }
        return 0.0;
    }

}
