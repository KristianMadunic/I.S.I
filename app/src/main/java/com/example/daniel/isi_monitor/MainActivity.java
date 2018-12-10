package com.example.daniel.isi_monitor;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    public static final String QR_MESSAGE = "com.example.daniel.isi_monitor.QR";

    IsiApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (IsiApplication)getApplication();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new com.google.zxing.integration.android.IntentIntegrator(MainActivity.this)
                        .initiateScan();

                // Remove this later
                //app.GetViewModelContainer().AddInfusion("Patient 0", "Raum 401", "3e0022000c47353136383631", "1e43056f563df6c892b932875ca1e3c03efaca75");

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String contents = result.getContents();
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra(QR_MESSAGE, contents);
                startActivity(intent);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        app.GetViewModelContainer().addObserver(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        app.GetViewModelContainer().deleteObserver(this);
        super.onPause();
    }

    public void update(Observable o, Object arg) {
        ArrayList<InfusionViewModel> list = app.GetViewModelContainer().GetViewModels();
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        final List<String> colors = new ArrayList<String>();
        final List<View.OnClickListener> delClickListeners = new ArrayList<View.OnClickListener>();

        for(final InfusionViewModel ViewModel: list) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", ViewModel.GetTitle());
            hm.put("listview_discription", ViewModel.GetDescription());
            hm.put("listview_image", Integer.toString(R.drawable.infusion_icon));
            aList.add(hm);

            colors.add(ViewModel.GetColor());
            delClickListeners.add(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.GetViewModelContainer().DeleteInfusion(ViewModel.GetId());
                }
            });
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        ListAdapter specialAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView=super.getView(position, convertView, parent);
                View delBtn=itemView.findViewById(R.id.btn_del);

                delBtn.setOnClickListener(delClickListeners.get(position));

                int colorPos = position % colors.size();
                int c = Color.parseColor(colors.get(colorPos));

                itemView.setBackgroundColor(c);

                return itemView;
            }
        };

        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(specialAdapter);
    }
}
