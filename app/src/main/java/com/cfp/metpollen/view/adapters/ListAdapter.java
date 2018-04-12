package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.customAnimations.ArcAngleAnimation;
import com.cfp.metpollen.view.customViews.ArcView;
import com.cfp.metpollen.view.customViews.CircleProgressBar;

/**
 * Created by AhmedAbbas on 11/23/2017.
 */

public class ListAdapter extends ArrayAdapter<String> {

    Context context;
    private int ViewTypeCount = 7;

    public ListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public int getCount() {
        return ViewTypeCount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(getItemViewType(position), parent, false);

        switch (position) {
            case 0: {
                CircleProgressBar circleProgressBar = (CircleProgressBar) convertView.findViewById(R.id.circleProgressBar);
                TextView progressText = (TextView) convertView.findViewById(R.id.progressText);
                View view1 = convertView.findViewById(R.id.view1);
                ViewGroup.LayoutParams params = view1.getLayoutParams();
                params.height = (int) ((parent.getHeight() / context.getResources().getDisplayMetrics().density) / 2.5 - 120 * context.getResources().getDisplayMetrics().density);
                view1.setLayoutParams(params);
                view1.setMinimumHeight(parent.getHeight() / 4);
                circleProgressBar.setStrokeWidth(50);
                circleProgressBar.setMin(0);
                circleProgressBar.setMax(100);
                circleProgressBar.setProgressWithAnimation(80);
                circleProgressBar.setColor(Color.GREEN);
                progressText.setText(80 + "%");
            }
            break;
            case 1:
/*
                View v = convertView.findViewById(R.id.view);
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = params.height - scrollAmmount;
                v.setLayoutParams(params);
*/
                break;
            case 2:
                break;
            case 3:
                break;
            case 4: {
                ImageView fan1 = (ImageView) convertView.findViewById(R.id.fan1);
                ImageView fan2 = (ImageView) convertView.findViewById(R.id.fan2);
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.circular_motion);
                anim.setFillAfter(true);
                fan1.setAnimation(anim);
                fan1.startAnimation(anim);

                Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.circular_motion);
                anim2.setFillAfter(true);
                fan2.setAnimation(anim2);
                fan2.startAnimation(anim2);
            }
            break;
            case 5: {
                ArcView arcView = (ArcView) convertView.findViewById(R.id.arcView);
                ArcAngleAnimation animation = new ArcAngleAnimation(arcView, 130);
                animation.setDuration(1500);
                arcView.startAnimation(animation);
            }
            break;
            case 6: {
                View view1 = convertView.findViewById(R.id.view1);
                view1.setMinimumHeight(parent.getHeight() / 3 - 50);
            }
            break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return R.layout.item_recyclerview_today_first;
            case 1:
                return R.layout.item_recyclerview_today_second;
            case 2:
                return R.layout.item_recyclerview_today_third;
            case 3:
                return R.layout.item_recyclerview_today_fourth;
            case 4:
                return R.layout.item_recyclerview_today_fifth;
            case 5:
                return R.layout.item_recyclerview_today_sixth;
            case 6:
                return R.layout.item_recyclerview_today_seventh;
        }
        return 0;
    }

}
