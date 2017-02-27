package com.myproject.outtake.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.myproject.outtake.Orderobserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/2/25.
 */

public class myreceive extends BroadcastReceiver {
    private HashMap<String, String> jsonMap = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);

                if (jsonObject.has("orderId")) {
                    String orderId = jsonObject.getString("orderId");
                    jsonMap.put("orderId", orderId);
                    Log.e(TAG, "onReceive: " + orderId);
                }
                if (jsonObject.has("type")) {
                    String type = jsonObject.getString("type");
                    jsonMap.put("type", type);
                }
                if (jsonObject.has("lat")) {
                    String lat = jsonObject.getString("lat");
                    jsonMap.put("lat", lat);
                }
                if (jsonObject.has("lng")) {
                    String lng = jsonObject.getString("lng");
                    jsonMap.put("lng", lng);
                }
                Orderobserver.getInstance().changUI(jsonMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
