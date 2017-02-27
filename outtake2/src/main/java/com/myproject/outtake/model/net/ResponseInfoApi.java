package com.myproject.outtake.model.net;

import com.myproject.outtake.Constants;
import com.myproject.outtake.model.net.bean.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface ResponseInfoApi {
    @GET(Constants.HOME)
    Call<ResponseInfo>getHomeInfo(@Query("latitude")String lat,@Query("longitude")String lon);
    @GET(Constants.BUSINESS)
    Call<ResponseInfo>getGoodsInfo(@Query("sellerId")long sellerId);
    @GET(Constants.LOGIN)
    Call<ResponseInfo> getLoginInfo(@Query("username") String username,@Query("password")String password,
                                    @Query("phone")String phone, @Query("type")int type);

    //http://10.0.2.2:8080/TakeoutServiceVersion2/order?userId=
    @GET(Constants.ORDER)
    Call<ResponseInfo> getOrderInfo(@Query("userId") long userId);
}
