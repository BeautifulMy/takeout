package com.myproject.outtake.presenter;

import com.myproject.outtake.Constants;
import com.myproject.outtake.model.net.ResponseInfoApi;
import com.myproject.outtake.model.net.bean.ResponseInfo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/2/17.
 */

public abstract class BasePresenter {

    public  ResponseInfoApi responseInfoApi;
    private HashMap<String, String> errorMap;

    public BasePresenter() {
        errorMap = new HashMap<>();
        errorMap.put("1", "此页数据没有更新");
        errorMap.put("2", "服务器忙");
        errorMap.put("3", "请求参数异常");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        responseInfoApi = retrofit.create(ResponseInfoApi.class);


    }

    class myCallBack implements Callback<ResponseInfo> {
        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            ResponseInfo body = response.body();
            if (body.getCode().equals("0")) {
                String json = body.getData();
                parseJson(json);
            } else {
                String error = errorMap.get(body.getCode());
                onFailure(call, new RuntimeException(error));
            }

        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            if (t instanceof RuntimeException) {
                String message = t.getMessage();
                showerrorMessage(message);
            }else{
                showerrorMessage("服务器忙,请稍后再试");
            }
        }
    }

    protected abstract void showerrorMessage(String message);

    protected abstract void parseJson(String json);


}
