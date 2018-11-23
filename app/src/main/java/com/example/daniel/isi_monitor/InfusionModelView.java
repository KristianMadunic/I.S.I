package com.example.daniel.isi_monitor;

import android.util.Log;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Hi Kristian. Ich habe folgende Schnittstellen für dich die du über die Membervariable
 * app ansprechen kannst:
 *
 * void AddInfusionData(String deviceId, int priority, double weight, String name, String location, String title, String description)
 *  -   Damit kannst du in der Auflistung einen neuen Eintrag erstellen. priority bestimmt die Position (hoch oben, niedrig unten)
 *
 * void RemoveInfusionData(String deviceId)
 *  -   Damit kannst du einen Eintrag aus der Auflistung entfernen.
 *
 * void UpdateInfusionData(String deviceId, int priority, double weight)
 *  -   Damit kannst du einen Eintrag in der Liste modifizieren.
 *
 * void SendNotification(String message)
 *  -   Damit kannst du eine Push Nachricht generieren.
 */

public class InfusionModelView {
    // Contains logic
    InfusionModel model;

    IsiApplication app;

    InfusionModelView(InfusionModel model, IsiApplication app) {
        this.model = model;
        this.app = app;
    }

    /**
     * Die Remove() Methode wird aufgerufen wenn der User auf den Mülleimer klickt.
     */
    public void Remove() {
        // Listeneintrag wird aus Frontend raus geworfen wenn Daten aus dem Backend entfernt werden
        app.RemoveInfusionData(model.GetDeviceId());
    }

    /**
     * Die Update() Methode wird zyklisch aufgerufen.
     * Ich habe schonmal den Code für das Aktuallisieren des Gewichts geschrieben weil man dafür
     * eine spezielle Bib verwenden muss.
     * Du kannst den Code einfach unten weiterschreiben.
     */
    public void Update() {
        /////////////////////////////////////////////////////////////////////////////
        // Auslesen des Gewichts via Http Request:
        //
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.particle.io/v1/devices/"
                + model.GetDeviceId() + "/weight?access_token="
                + model.GetAccessToken();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Do something when request failed
                e.printStackTrace();
                Log.d("InfusionModelView", "Request Failed.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d("InfusionModelView","Request not Succesful");

                    // Mark model as not online
                    model.setOnline(false);

                    return;
                } else {
                    // Mark model as online
                    model.setOnline(true);
                }

                // Read data in the worker thread
                final String data = response.body().string();

                DeviceValueFactory factory = new DeviceValueFactory();

                double weight = factory.FromJsonString(data);

                Log.d("InfusionModelView","Set weight: " + weight);

                // Update weight attribute of model
                model.SetWeight(weight);
            }
        });

        /////////////////////////////////////////////////////////////////////////////
        // Dein Code beginnt hier:
        //



    }
}
