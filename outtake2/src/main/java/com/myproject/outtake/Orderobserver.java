package com.myproject.outtake;

import java.util.Observable;

/**
 * Created by Administrator on 2017/2/25.
 */

public class Orderobserver extends Observable{
    /* 订单状态
       * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单*/
    public static final String ORDERTYPE_UNPAYMENT = "10";
    public static final String ORDERTYPE_SUBMIT = "20";
    public static final String ORDERTYPE_RECEIVEORDER = "30";
    public static final String ORDERTYPE_DISTRIBUTION = "40";
    // 骑手状态：接单、取餐、送餐
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48";

    public static final String ORDERTYPE_SERVED = "50";
    public static final String ORDERTYPE_CANCELLEDORDER = "60";
    private Orderobserver(){}
    private static Orderobserver orderobserver = null;
    public static Orderobserver getInstance() {
        if (orderobserver==null){
             orderobserver = new Orderobserver();

        }
        return orderobserver;
    }

    public void changUI(Object o) {
        setChanged();
        notifyObservers(o);
    }
}
