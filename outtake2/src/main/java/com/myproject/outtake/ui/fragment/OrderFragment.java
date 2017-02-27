package com.myproject.outtake.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.presenter.OrderPresenter;
import com.myproject.outtake.ui.adapter.OrderAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/17.
 */
public class OrderFragment extends BaseFragment {
    @Bind(R.id.rv_order_list)
    RecyclerView rvOrderList;
    @Bind(R.id.srl_order)
    SwipeRefreshLayout srlOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OrderAdapter orderAdapter = new OrderAdapter(getActivity());
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rvOrderList.setAdapter(orderAdapter);

        OrderPresenter orderPresenter = new OrderPresenter(orderAdapter);
        orderPresenter.getOrderData(MyApp.userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
