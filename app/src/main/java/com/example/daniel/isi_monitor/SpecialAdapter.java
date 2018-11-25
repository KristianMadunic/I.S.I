package com.example.daniel.isi_monitor;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class SpecialAdapter extends SimpleAdapter {
    private List<String> colors;

    public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to, List<String> colors) {
        super(context, items, resource, from, to);
        this.colors = colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int colorPos = position % colors.size();
        int c = Color.parseColor(colors.get(colorPos));
        view.setBackgroundColor(c);
        return view;
    }
}
