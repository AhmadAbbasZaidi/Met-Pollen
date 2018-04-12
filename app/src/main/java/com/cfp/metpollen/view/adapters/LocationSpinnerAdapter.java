package com.cfp.metpollen.view.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.StationModel;

import java.util.List;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

public class LocationSpinnerAdapter extends BaseAdapter {

    List<StationModel> data = null;
    private Activity context;

    public LocationSpinnerAdapter( Activity context,List<StationModel> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null);
        TextView names = (TextView) convertView.findViewById(R.id.rowText);
        names.setText(data.get(position).getStationName());
        names.setTextColor(Color.WHITE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_layout_dropdown, null);
        TextView names = (TextView) convertView.findViewById(R.id.rowText);
        names.setText(data.get(position).getStationName());
        names.setTextColor(Color.BLACK);
        return convertView;
    }

}