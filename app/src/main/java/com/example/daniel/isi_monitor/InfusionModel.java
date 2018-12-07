package com.example.daniel.isi_monitor;

public class InfusionModel {
    private boolean online;
    private double weight;
    private int counter;
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
        counter = 0;
    }

    public int GetCounter() { return counter; }

    public double GetWeight() { return weight; }

    public String GetName() { return name; }

    public String GetLocation() { return location; }

    public String GetRestRoute(String prop) {
        return "https://api.particle.io/v1/devices/" + deviceId
                + "/" + prop
                + "?access_token=" + accessToken;
    }

    public boolean IsOnline() { return online; }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void SetWeight(double weight) {
        this.weight = weight;
    }

    public void SetCounter(int counter) { this.counter = counter; }

}
