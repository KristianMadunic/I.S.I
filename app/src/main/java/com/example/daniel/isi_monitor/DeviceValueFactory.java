package com.example.daniel.isi_monitor;

import org.json.JSONObject;

public class DeviceValueFactory {

    public double DoubleFromJsonString(String jsonString) {
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            return mainObject.getDouble("result");
        } catch(Exception e) {

        }
        return 0.0;
    }

    public int IntegerFromJsonString(String jsonString) {
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            return mainObject.getInt("result");
        } catch(Exception e) {

        }
        return 0;
    }

}
