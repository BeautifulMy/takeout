package com.myproject.outtake.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myproject.outtake.model.net.bean.Order;
import com.myproject.outtake.model.net.bean.ResponseInfo;
import com.myproject.outtake.ui.adapter.OrderAdapter;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/25.
 */

public class OrderPresenter extends BasePresenter {

    private final OrderAdapter orderAdapter;
    private List<Order> orderList;
    public OrderPresenter(OrderAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }
    @Override
    protected void showerrorMessage(String message) {

    }

    @Override
    protected void parseJson(String json) {
        Gson gson = new Gson();
        orderList = gson.fromJson(json,new TypeToken<List<Order>>(){}.getType());
        orderAdapter.setData(orderList);
    }
    public void getOrderData(int userId){
        Call<ResponseInfo> orderInfo = responseInfoApi.getOrderInfo(userId);
        orderInfo.enqueue(new myCallBack());
    }
}
