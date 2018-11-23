package com.example.daniel.isi_monitor;

import android.util.Log;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfusionModelView {
    // Contains logic
    InfusionModel model;

    InfusionModelView(InfusionModel model) {
        this.model = model;
    }

    void Update() {
        // Process logic
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
    }
}
