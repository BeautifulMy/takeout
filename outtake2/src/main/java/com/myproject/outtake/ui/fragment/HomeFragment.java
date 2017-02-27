package com.myproject.outtake.ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myproject.outtake.R;
import com.myproject.outtake.presenter.HomePresenter;
import com.myproject.outtake.ui.adapter.HomeRecycleViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/17.
 */
public class HomeFragment extends BaseFragment {
        private int countY = 0;
    @Bind(R.id.rv_home)
    RecyclerView rvHome;
    @Bind(R.id.home_tv_address)
    TextView homeTvAddress;
    @Bind(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @Bind(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        HomeRecycleViewAdapter homeRecycleViewAdapter = new HomeRecycleViewAdapter(getActivity());
        HomePresenter homePresenter = new HomePresenter(homeRecycleViewAdapter);
        rvHome.setAdapter(homeRecycleViewAdapter);
        homePresenter.getHomeData("","");
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                countY+=dy;
                int bgColor = 0X553190E8;
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                if(countY==0){
                    bgColor=0X553190E8;
                }else if(countY>300){
                    bgColor =0XFF3190E8;
                }else {
                   bgColor= (int)argbEvaluator.evaluate(countY/300f,0X553190E8,0XFF3190E8);
                }
                llTitleContainer.setBackgroundColor(bgColor);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
