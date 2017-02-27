package com.myproject.outtake.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.myproject.outtake.model.net.bean.HomeInfo;
import com.myproject.outtake.model.net.bean.ResponseInfo;
import com.myproject.outtake.ui.adapter.HomeRecycleViewAdapter;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/17.
 */

public class HomePresenter extends BasePresenter {
    private final HomeRecycleViewAdapter homeRecycleViewAdapter;

    public HomePresenter(HomeRecycleViewAdapter homeRecycleViewAdapter) {
        this.homeRecycleViewAdapter = homeRecycleViewAdapter;
    }

    @Override
    protected void showerrorMessage(String message) {
        Log.e("", "showerrorMessage: " + message);
    }

    @Override
    protected void parseJson(String json) {
        HomeInfo homeInfo = new Gson().fromJson(json, HomeInfo.class);
        homeRecycleViewAdapter.setData(homeInfo);
    }

    //触发网络实现 call的回调  // TODO: 2017/2/17
    public void getHomeData(String lat, String lon) {
        Call<ResponseInfo> homeInfo = responseInfoApi.getHomeInfo(lat, lon);
        homeInfo.enqueue(new myCallBack());
    }
}
