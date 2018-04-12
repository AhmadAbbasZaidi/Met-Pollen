package com.cfp.metpollen.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.presenter.ApiResponseListener;
import com.cfp.metpollen.view.abstracts.OnScrollPositionChangedListener;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.adapters.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends BaseFragment implements ApiResponseListener {


    private static TodayFragment instance = null;
    private static int blurIndex = 0;
    private ApiResponseListener apiResponseListener;
    private ListView lv;
    private ListAdapter adapter;
    private boolean mHasBeenVisible = true;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment getInstance() {
        if (instance == null) {
            instance = new TodayFragment();
        }
        return instance;
    }

    public int getBlurIndex() {
        return blurIndex;
    }

    public void setBlurIndex(int blurIndex) {
        this.blurIndex = blurIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        final FragmentTodayRecyclerAdapter adapter = new FragmentTodayRecyclerAdapter(getActivity());
        recyclerview.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(recyclerview, dx, dy);
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerview.getLayoutManager());
                int lastItemVisible = linearLayoutManager.findLastVisibleItemPosition();
                ((MainActivity) getActivity()).setBackground(lastItemVisible);
            }
        });*/


        apiResponseListener = this;
        lv = (ListView) view.findViewById(R.id.listview);
        adapter = new ListAdapter(getActivity(), R.layout.item_recyclerview_today_first);
        lv.setAdapter(adapter);

        final View view2 = view.findViewById(R.id.view);
        lv.setOnScrollListener(new OnScrollPositionChangedListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//                ((MainActivity) getActivity()).setBackground(firstVisibleItem + visibleItemCount - 1);


                //Do your onScroll stuff
            }

            @Override
            public void onScrollPositionChanged(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int scrollYPosition) {
                //Enjoy having access to the amount the ListView has scrolled

                if (mHasBeenVisible) {
                    setBlurIndex(scrollYPosition);
                    ((MainActivity) getActivity()).setBackground(scrollYPosition);
                }
//                Log.i("Scroll Y ", " = " + scrollYPosition);
                /*if (view.getLastVisiblePosition() == 1) {
                    View view2 = view.getChildAt(1).findViewById(R.id.view);

                    if (scrollYPosition > 0) {
                        view2.setVisibility(View.GONE);
                    } else {
                        if (scrollYPosition + view2.getHeight() < 0) {
                            view2.setVisibility(View.GONE);
                        } else {
                            view2.setVisibility(View.VISIBLE);
                        }
                    }
                }*/
            }
        });

        /*lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ((MainActivity) getActivity()).setBackground(firstVisibleItem+visibleItemCount-1);

                View c = lv.getChildAt(0);
                int scrolly = -c.getTop() + firstVisibleItem * c.getHeight();
                if(scrolly<100)
                {
                    view2.setVisibility(View.VISIBLE);
                }
                else
                {
                    view2.setVisibility(View.GONE);
                }
                Log.i("last Position"," = "+scrolly);
//                view.getLastVisiblePosition();
                adapter.setLastVisiblePosition(view.getLastVisiblePosition());
            }
        });*/

    }

    public void updateForecast() {
    }

    @Override
    public void setApiResponse(JSONObject jsonObject, String calledApi) {

    }

    @Override
    public void setApiResponse(JSONArray jsonArray, String calledApi) {

    }

    @Override
    public void setApiResponse(String jsonAsString, String calledApi) {

    }

    @Override
    public void setApiError() {

    }

    @Override
    public void onTabSelected(int position) {
//        Toast.makeText(getActivity(),"TabPosition = "+position+"\nblurIndex = "+blurIndex,Toast.LENGTH_LONG).show();
        Log.i("Tab = ",position+"");
        mHasBeenVisible = true;
        if (mHasBeenVisible) {
            ((MainActivity) getActivity()).setBackground(blurIndex);
            ((MainActivity) getActivity()).getBackgroundViewOverlayThreeDays().setVisibility(View.GONE);
            ((MainActivity) getActivity()).getBackgroundViewOverlayHourly().setVisibility(View.GONE);
            ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setVisibility(View.VISIBLE);
//            ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setBackgroundColor(getResources().getColor(R.color.blueoverlay));
        }
    }

    @Override
    public void onTabUnSelected(int position) {
        mHasBeenVisible = false;
    }

    public void updateData(CurrentModel currentModelList) {
        if (adapter != null) {
            adapter = new ListAdapter(getActivity(), R.layout.item_recyclerview_today_first);
        }
    }
}