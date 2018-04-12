package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.customViews.CircleImageView;
import com.cfp.metpollen.view.interfaces.ColorSelectorListener;

import java.util.List;


/**
 * Created by AhmedAbbas on 11/17/2017.
 */

public class WidgetColorSelectorRecyclerAdapter extends RecyclerView.Adapter<WidgetColorSelectorRecyclerAdapter.ColorViewHolder> {
    private final int resource;
    private final ColorSelectorListener colorSelectorListener;
    Context context;
    private List<Integer> colors;

    public WidgetColorSelectorRecyclerAdapter(Context context, int resource, List<Integer> colors, ColorSelectorListener colorSelectorListener) {
        this.context = context;
        this.resource = resource;
        this.colors = colors;
        this.colorSelectorListener = colorSelectorListener;
    }


    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
        Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(colors.get(position), PorterDuff.Mode.SRC);
        holder.circleImageView.setImageBitmap(bmp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorSelectorListener.onColorSelected(colors.get(position));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;

        public ColorViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageViewColor);
        }
    }

}
