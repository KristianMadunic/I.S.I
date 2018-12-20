package com.example.daniel.isi_monitor;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {
    Button mSave;
    EditText mName, mLocation;

    String deviceId, accessToken;

    IsiApplication app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mSave = (Button) findViewById(R.id.button_save);
        mName = (EditText) findViewById(R.id.text_name);
        mLocation = (EditText) findViewById(R.id.text_location);

        app = (IsiApplication)getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String qrContent = intent.getStringExtra(MainActivity.QR_MESSAGE);

        this.deviceId = "";
        this.accessToken = "";

        try {
            JSONObject mainObject = new JSONObject(qrContent);
            deviceId = mainObject.getString("deviceId");
            accessToken = mainObject.getString("accessToken");
        } catch(Exception e) {

        }

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.GetViewModelContainer()
                        .AddInfusion(
                                mName.getText().toString(),
                                mLocation.getText().toString(),
                                deviceId,
                                accessToken
                        );
                finish();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
