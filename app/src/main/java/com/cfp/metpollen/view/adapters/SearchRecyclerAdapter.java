package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    private final SearchDropdownListener searchDropdownListener;
    private static List<StationModel> stationModelsList;
    private LinearLayout rootLayout;
    Context context;

    public SearchRecyclerAdapter(Context context, SearchDropdownListener searchDropdownListener, LinearLayout rootLayout) {
        this.context = context;
        this.searchDropdownListener = searchDropdownListener;
        stationModelsList = new ArrayList<>();
        this.rootLayout = rootLayout;
    }

    public static List<StationModel> getStationModelsList() {
        return stationModelsList;
    }

    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_layout_dropdown, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchRecyclerAdapter.ViewHolder holder, int position) {
        final StationModel model = stationModelsList.get(position);
        holder.name.setText(model.getStationName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDropdownListener.setSelectedLocation(model);
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return stationModelsList.get(position).getStationId();
    }

    @Override
    public int getItemCount() {
        return stationModelsList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stationModelsList.clear();
        if (charText.length() == 0) {
            stationModelsList.addAll(AppWideVariables.getStationList());
            rootLayout.setVisibility(View.INVISIBLE);
        } else {
            if(AppWideVariables.getStationList()!=null&&AppWideVariables.getStationList().size()>0) {
                rootLayout.setVisibility(View.VISIBLE);
                for (StationModel sm : AppWideVariables.getStationList()) {
                    if (sm.getStationName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        stationModelsList.add(sm);
                    }
                }
            }
            else
            {
                rootLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
