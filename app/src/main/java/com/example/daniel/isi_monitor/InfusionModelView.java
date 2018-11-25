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
 * void SendNotification(String message)
 *  -   Damit kannst du eine Push Nachricht generieren.
 */

public class InfusionModelView implements Comparable<InfusionModelView> {
    // Referenced model
    InfusionModel model;

    // Application which is using the view
    IsiApplication app;

    // Title of the List entry
    String title;

    // Short description for List entry
    String description;

    // Background color of list entry as hex string
    String color;

    // For ordering the list
    int priority;

    InfusionModelView(InfusionModel model, IsiApplication app) {
        this.model = model;
        this.app = app;

        // TODO sinnvolle Werte
        this.title = "";
        this.description = "";
        this.color = "#FFFFFF";
    }

    public void SetTitle(String title) {
        this.title = title;
    }

    public void SetDescription(String description) {
        this.description = description;
    }

    public void SetColor(String color) {
        this.color = color;
    }

    public void SetPriority(int priority) {
        this.priority = priority;
    }

    public String GetTitle() { return title; }

    public String GetDescription() { return description; }

    public String GetColor() { return color; }

    public int GetPriority() { return priority; }

    @Override
    public int compareTo(InfusionModelView modelView) {
        return priority - modelView.priority;
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

        String url = model.GetRestRoute("weight");

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
