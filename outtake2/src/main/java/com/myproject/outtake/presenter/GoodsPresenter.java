package com.myproject.outtake.presenter;

import com.google.gson.Gson;
import com.myproject.outtake.model.net.bean.BusinessInfo;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.GoodsTypeInfo;
import com.myproject.outtake.model.net.bean.ResponseInfo;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.ui.adapter.GoodsAdapter;
import com.myproject.outtake.ui.adapter.GoodsTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/18.
 */

public class GoodsPresenter extends BasePresenter {
    GoodsAdapter goodsAdapter;
    Seller seller;
    GoodsTypeAdapter goodsTypeAdapter;

    private List<GoodsTypeInfo> goodsTypelist;
    private ArrayList<GoodsInfo> goodsInfoArrayList;

    public GoodsPresenter(){}
    public GoodsPresenter(GoodsTypeAdapter goodsTypeAdapter, GoodsAdapter goodsAdapter, Seller seller){
        this.goodsTypeAdapter =goodsTypeAdapter;
        this.goodsAdapter = goodsAdapter;
        this.seller = seller;
    }
    @Override
    protected void showerrorMessage(String message) {

    }


    @Override
    protected void parseJson(String json) {
        BusinessInfo businessInfo = new Gson().fromJson(json, BusinessInfo.class);
        goodsTypelist = businessInfo.getList();
        goodsTypeAdapter.setData(goodsTypelist);
        initGoodsInfoData();
        goodsAdapter.setData(goodsInfoArrayList);
    }

    private void initGoodsInfoData() {
         goodsInfoArrayList = new ArrayList<>();
        for (int i = 0; i < goodsTypelist.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = goodsTypelist.get(i);
            List<GoodsInfo> goodsInfoList = goodsTypeInfo.getList();
            for (int j = 0; j < goodsInfoList.size(); j++) {
                GoodsInfo goodsInfo = goodsInfoList.get(j);
                goodsInfo.setTypeId(goodsTypeInfo.getId());
                goodsInfo.setTypeName(goodsTypeInfo.getName());
                goodsInfo.setSellerId((int) seller.getId());
                goodsInfoArrayList.add(goodsInfo);
            }

        }
    }
    public void getGoodsData(Seller seller){
        Call<ResponseInfo> goodsInfo = responseInfoApi.getGoodsInfo(seller.getId());
        goodsInfo.enqueue(new myCallBack());
    }
}
