package com.example.daniel.isi_monitor;

public class InfusionModel {
    private boolean online;
    private double weight;
    private String name;
    private String location;
    private String deviceId;
    private String accessToken;

    InfusionModel(String name, String location, String deviceId, String accessToken) {
        this.name = name;
        this.location = location;
        this.deviceId = deviceId;
        this.accessToken = accessToken;

        weight = 0.0;
        online = false;
    }

    public double GetWeight() { return weight; }

    public String GetName() { return name; }

    public String GetLocation() { return location; }

    public String GetDeviceId() { return  deviceId; }

    public String GetAccessToken() { return accessToken; }

    public boolean IsOnline() { return online; }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void SetWeight(double weight) {
        this.weight = weight;
    }

}
