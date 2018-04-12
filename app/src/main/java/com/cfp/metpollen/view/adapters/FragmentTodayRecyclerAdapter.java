package com.cfp.metpollen.view.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.customAnimations.ArcAngleAnimation;
import com.cfp.metpollen.view.customViews.ArcView;
import com.cfp.metpollen.view.customViews.CircleProgressBar;


/**
 * Created by AhmedAbbas on 11/17/2017.
 */

public class FragmentTodayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    public FragmentTodayRecyclerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_first, parent, false);
                return new HolderItemOne(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_second, parent, false);
                return new HolderItemTwo(view);
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_third, parent, false);
                return new HolderItemThree(view);
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_fourth, parent, false);
                return new HolderItemFour(view);
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_fifth, parent, false);
                return new HolderItemFive(view);
            case 5:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_sixth, parent, false);
                return new HolderItemSix(view);
            case 6:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_today_seventh, parent, false);
                return new HolderItemSeven(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            HolderItemOne holderItemOne = (HolderItemOne) holder;
            holderItemOne.poition= position;
            holderItemOne.circleProgressBar.setStrokeWidth(50);
            holderItemOne.circleProgressBar.setMin(0);
            holderItemOne.circleProgressBar.setMax(100);
            holderItemOne.circleProgressBar.setProgressWithAnimation(80);
            holderItemOne.circleProgressBar.setColor(Color.GREEN);
            holderItemOne.progressText.setText(80 + "%");
        } else if (position == 1) {
            HolderItemTwo holderItemTwo = (HolderItemTwo) holder;
            holderItemTwo.poition= position;

        } else if (position == 2) {
            HolderItemThree holderItemThree = (HolderItemThree) holder;
            holderItemThree.poition= position;

        } else if (position == 3) {
            HolderItemFour holderItemFour = (HolderItemFour) holder;
            holderItemFour.poition= position;

        } else if (position == 4) {
            HolderItemFive holderItemFive = (HolderItemFive) holder;

            Animation anim = AnimationUtils.loadAnimation(context, R.anim.circular_motion);
            anim.setFillAfter(true);
            holderItemFive.fan1.setAnimation(anim);
            holderItemFive.fan1.startAnimation(anim);


            Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.circular_motion);
            anim2.setFillAfter(true);
            holderItemFive.fan2.setAnimation(anim2);
            holderItemFive.fan2.startAnimation(anim2);

        } else if (position == 5) {
            HolderItemSix holderItemSix = (HolderItemSix) holder;
            ArcAngleAnimation animation = new ArcAngleAnimation(holderItemSix.arcView, 150);
            animation.setDuration(3000);
            holderItemSix.arcView.startAnimation(animation);
        }else if (position == 6) {
            HolderItemSeven holderItemSeven = (HolderItemSeven) holder;
            holderItemSeven.poition= position;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
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
        return 7;
    }


    class HolderItemOne extends RecyclerView.ViewHolder {
        CircleProgressBar circleProgressBar;
        TextView progressText;
        int poition;

        public HolderItemOne(View itemView) {
            super(itemView);
            circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circleProgressBar);
            progressText = (TextView) itemView.findViewById(R.id.progressText);
        }
    }

    class HolderItemTwo extends RecyclerView.ViewHolder {

        int poition;

        public HolderItemTwo(View itemView) {
            super(itemView);

        }
    }

    class HolderItemThree extends RecyclerView.ViewHolder {
        int poition;
        public HolderItemThree(View itemView) {
            super(itemView);
        }
    }

    class HolderItemFour extends RecyclerView.ViewHolder {
        int poition;
        public HolderItemFour(View itemView) {
            super(itemView);
        }
    }

    class HolderItemFive extends RecyclerView.ViewHolder {
        ImageView fan1, fan2;
        int poition;

        public HolderItemFive(View itemView) {
            super(itemView);
            fan1 = (ImageView) itemView.findViewById(R.id.fan1);
            fan2 = (ImageView) itemView.findViewById(R.id.fan2);
        }
    }

    class HolderItemSix extends RecyclerView.ViewHolder {
        ArcView arcView;
        int poition;

        public HolderItemSix(View itemView) {
            super(itemView);
            arcView = (ArcView) itemView.findViewById(R.id.arcView);
        }
    }

    class HolderItemSeven extends RecyclerView.ViewHolder {
        int poition;
        public HolderItemSeven(View itemView) {
            super(itemView);
        }
    }
}
