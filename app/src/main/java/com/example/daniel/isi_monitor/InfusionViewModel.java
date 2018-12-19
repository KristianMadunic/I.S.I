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
 * void SendNotification(String message, int id)
 *  -   Damit kannst du eine Push Nachricht generieren.
 *
 * Alle weiteren Funktionalitäten laufen über die lokalen Variablen.
 */

public class InfusionViewModel implements Comparable<InfusionViewModel> {
    final boolean debug = false;

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

    // To keep track of id's
    static int counter = 0;

    // To identify the model view
    final int id;

    InfusionViewModel(InfusionModel model, IsiApplication app) {
        this.model = model;
        this.app = app;

        this.id = counter++;

        // Initialize
        this.title = model.GetName();
        this.description = "Init...";
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

    public int GetId() { return  id; }

    @Override
    public int compareTo(InfusionViewModel modelView) {
        return priority - modelView.priority;
    }

    public void GetResponse(String prop, Callback cb) {
        OkHttpClient client = new OkHttpClient();

        String url = model.GetRestRoute(prop);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(cb);
    }

    /**
     * Die Update() Methode wird zyklisch aufgerufen.
     * Ich habe schonmal den Code für das Aktuallisieren des Gewichts geschrieben weil man dafür
     * eine spezielle Bib verwenden muss.
     * Du kannst den Code einfach unten weiterschreiben.
     */
    public void Update() {
        if(!debug) {
            /////////////////////////////////////////////////////////////////////////////
            // Auslesen des Gewichts via Http Request:
            //
            GetResponse("avgWeight", new Callback() {
                public void onFailure(Call call, IOException e) {
                    // Do something when request failed
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    // Read data in the worker thread
                    final String data = response.body().string();

                    DeviceValueFactory factory = new DeviceValueFactory();

                    double weight = factory.DoubleFromJsonString(data);

                    // Update weight attribute of model
                    model.SetWeight(weight);
                }
            });

            /////////////////////////////////////////////////////////////////////////////
            // Auslesen der "Leer Zeit" via Http Request:
            //
            GetResponse("counter", new Callback() {
                public void onFailure(Call call, IOException e) {
                    // Do something when request failed
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    // Read data in the worker thread
                    final String data = response.body().string();

                    DeviceValueFactory factory = new DeviceValueFactory();

                    int counter = factory.IntegerFromJsonString(data);

                    // Update weight attribute of model
                    model.SetCounter(counter);
                }
            });
        } else {
            // Debug mode
            int newCounter = model.GetCounter() + 600;
            if(newCounter > 200*60) newCounter = 0;
            model.SetWeight(1);
            model.SetCounter(newCounter);
        }

        /////////////////////////////////////////////////////////////////////////////
        // Dein Code beginnt hier:
        //

        // Determine priority
        this.SetPriority(Integer.MAX_VALUE - model.GetCounter());

        // Create description message
        String description = model.GetLocation() + " / Status: ";
        if(model.GetCounter() < 10*60) {
            description += "Nicht leer";
        } else {
            int minutes = model.GetCounter() / 60;
            description += "Seit " + minutes + " Minuten leer";
        }
        this.SetDescription(description);

        // Calculate color.
        // Starts with green, transforms to red over yellow.
        int red = 0, green = 0, blue = 0;
        int maxTime = 120*60; // Full red after 2h
        int time = model.GetCounter();
        if(time > maxTime) {
            time = maxTime;
        }
        float f = (float)time / maxTime; // convert to [0 ... 1]
        float a = (1.f - f)*2.f;
        float x = (float)Math.floor(a);
        float y = (float)Math.floor(255.f*(a-x));
        int group = (int)x; // Group: 0->red 1->yellow 2->green
        switch(group)
        {
            case 0:
                red=255;
                green=(int)y;
                blue=0;
                break;
            case 1:
                red=255-(int)y;
                green=255;
                blue=0;
                break;
            case 2:
                red=0;
                green=255;
                blue=(int)y;
                break;
        }
        this.SetColor("#" + String.format("%02x", red) + String.format("%02x", green) + String.format("%02x", blue));

        // Throw notification if empty for too long
        if(model.GetCounter() > 30*60) {
            app.SendNotification(model.GetName() + ": Infusion ist leer", this.GetId());
        }

    }
}
