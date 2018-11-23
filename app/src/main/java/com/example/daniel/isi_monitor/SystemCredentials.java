package com.example.daniel.isi_monitor;

public class SystemCredentials {
    private String deviceId;
    private String accessToken;

    SystemCredentials(String deviceId, String accessToken) {
        this.deviceId = deviceId;
        this.accessToken = accessToken;
    }

    public String getDeviceId() { return deviceId; }

    public String getAccessToken() { return accessToken; }
}
