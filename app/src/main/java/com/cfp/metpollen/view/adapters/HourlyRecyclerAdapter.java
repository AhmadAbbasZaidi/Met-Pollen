package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.StationModel;
import com.cfp.metpollen.view.interfaces.SearchDropdownListener;
import com.cfp.metpollen.view.utilities.AppWideVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by AhmedAbbas on 11/17/2017.
 */

public class HourlyRecyclerAdapter extends RecyclerView.Adapter<HourlyRecyclerAdapter.ViewHolder> {
    private static List<Integer> stationModelsList;
    Context context;

    public HourlyRecyclerAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.stationModelsList = list;
    }

    public static List<Integer> getStationModelsList() {
        return stationModelsList;
    }

    @Override
    public HourlyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_hourly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stationModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
