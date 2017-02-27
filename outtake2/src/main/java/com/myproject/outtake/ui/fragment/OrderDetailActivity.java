package com.myproject.outtake.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.myproject.outtake.Orderobserver;
import com.myproject.outtake.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailActivity extends FragmentActivity implements Observer{

    @Bind(R.id.iv_order_detail_back)
    ImageView ivOrderDetailBack;
    @Bind(R.id.tv_seller_name)
    TextView tvSellerName;
    @Bind(R.id.tv_order_detail_time)
    TextView tvOrderDetailTime;
    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.ll_order_detail_type_container)
    LinearLayout llOrderDetailTypeContainer;
    @Bind(R.id.ll_order_detail_type_point_container)
    LinearLayout llOrderDetailTypePointContainer;
    private AMap aMap;
    private LatLng latlngBuyer
            ;
    private LatLng latlngSeller;
    private List<LatLng> riderPosList = new ArrayList<>();
    private LatLng riderPos
            ;
    private Marker markerRider
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        String orderId = getIntent().getStringExtra("orderId");
        String type = getIntent().getStringExtra("type");

        map.onCreate(savedInstanceState);// 此方法必须重写
        aMap = map.getMap();

        //注册观察者
        Orderobserver.getInstance().addObserver(this);
        int index = getIndex(type);
        changeUI(index);
        ivOrderDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    private void changeUI(int index) {
        for (int i = 0; i < llOrderDetailTypeContainer.getChildCount(); i++) {
            TextView textView = (TextView) llOrderDetailTypeContainer.getChildAt(i);
            ImageView imageView = (ImageView) llOrderDetailTypePointContainer.getChildAt(i);

            textView.setTextColor(Color.LTGRAY);
            imageView.setImageResource(R.drawable.order_time_node_normal);
        }
        if (index != -1) {
            TextView tv = (TextView) llOrderDetailTypeContainer.getChildAt(index);
            tv.setTextColor(Color.BLUE);

            ImageView imageView = (ImageView) llOrderDetailTypePointContainer.getChildAt(index);
            imageView.setImageResource(R.drawable.order_time_node_disabled);
        }
    }

    private int index = -1;

    private int getIndex(String type) {
        switch (type) {
            case Orderobserver.ORDERTYPE_SUBMIT://订单已经提交
                index = 0;
                break;
            case Orderobserver.ORDERTYPE_RECEIVEORDER://商家已接单
                index = 1;
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION://配送中
                index = 2;
                break;
            case Orderobserver.ORDERTYPE_SERVED://已送达
                index = 3;
                break;
        }
        return index;
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap<String, String> hashMap = (HashMap<String, String>) o;
        String orderId = hashMap.get("orderId");
        String type = hashMap.get("type");
        int index = getIndex(type);
        changeUI(index);
        switch(type){
            case Orderobserver.ORDERTYPE_DISTRIBUTION://配送中
                //显示买卖双方,高德地图
                initMap();
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE://骑手接单
                //初始化骑手,让骑手显示在地图的中心点
                initRider(o);
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://骑手取餐
                changeRider(o);
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL://骑手送餐
                changeRider(o);
                break;
        }
    }

    private void changeRider(Object o) {
        HashMap<String, String> hashMap = (HashMap<String, String>) o;

        String type = hashMap.get("type");
        //骑手在取餐过程中拿到的经纬度
        String lat = hashMap.get("lat");
        String lng = hashMap.get("lng");

        //获取经纬度信息,定位骑手的位置
        LatLng currentPos=new LatLng(Double.valueOf(lat),Double.valueOf(lng));
        //将经纬度添加在经纬度集合中
        riderPosList.add(currentPos);//骑行过程中的经纬度
        //设置骑手的所在位置
        markerRider.setPosition(currentPos);
        //地图定位焦点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));

        String info="";
        DecimalFormat format=new DecimalFormat(".00");

        switch (type) {
            case Orderobserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://取餐常量
                // 取餐,距离卖家的距离
                float ds = AMapUtils.calculateLineDistance(currentPos, latlngSeller);
                info="距离商家"+format.format(ds)+"米";
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                // 送餐,距离买家的距离
                float db = AMapUtils.calculateLineDistance(currentPos, latlngBuyer);
                info="距离买家"+format.format(db)+"米";
                break;
        }
        markerRider.setSnippet(info);
        markerRider.showInfoWindow();
        //参数一:当前所处位置
        //参数二:历史的点
        drawLine(currentPos,riderPosList.get(riderPosList.size()-2));
    }
    private void drawLine(LatLng currentPos, LatLng pos) {
        aMap.addPolyline(new PolylineOptions().add(pos,currentPos).width(2).color(Color.GREEN));
    }

    private void initRider(Object o) {
        riderPosList.clear();
        HashMap<String, String> hashMap = (HashMap<String, String>) o;
        //骑手所在经纬度
        String lat = hashMap.get("lat");
        String lng = hashMap.get("lng");

        if(TextUtils.isEmpty(lat)|| TextUtils.isEmpty(lng)){
            return;
        }
        riderPos = new LatLng(Double.valueOf(lat),Double.valueOf(lng));

        //初始化图片
        ImageView markerRiderIcon = new ImageView(this);
        markerRiderIcon.setImageResource(R.mipmap.order_rider_icon);
        //指定骑手所在的经纬度，以及在地图的中心点显示
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderPos));
        //地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //骑手图片的显示

        //指定锚点，以及描述文本
        markerRider = aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(riderPos));
        markerRider.setSnippet("骑手已接单");
        //显示骑手
        markerRider.showInfoWindow();
        markerRider.setIcon(BitmapDescriptorFactory.fromView(markerRiderIcon));

        //记录开始点的位置
        riderPosList.add(riderPos);
    }

    private void initMap() {
        map.setVisibility(View.VISIBLE);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        latlngBuyer = new LatLng(40.100519, 116.365828);
        //将相机的焦点移动到latlngBuyer经纬上
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlngBuyer));
        //指定买家图片
        Marker markerLatlngBuye=aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(latlngBuyer));
        ImageView markerBuyerIcon = new ImageView(this);
        markerBuyerIcon.setImageResource(R.mipmap.order_buyer_icon);
        markerLatlngBuye.setIcon(BitmapDescriptorFactory.fromView(markerBuyerIcon));
        // 添加卖家marker
        latlngSeller = new LatLng(40.060244, 116.343513);

        //指定卖家图片
        Marker markerLatlngSeller=aMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(latlngSeller));
        ImageView markerSellerIcon = new ImageView(this);
        markerSellerIcon.setImageResource(R.mipmap.order_seller_icon);
        markerLatlngSeller.setIcon(BitmapDescriptorFactory.fromView(markerSellerIcon));
    }
}
