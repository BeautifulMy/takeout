package com.myproject.outtake;

import android.app.Application;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.myproject.outtake.model.net.bean.UserInfo;

import java.sql.SQLException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/2/19.
 */

public class MyApp extends Application {

    public static int statusBarHeight;
    public static int userId=-1;

    @Override
    public void onCreate() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        SMSSDK.initSDK(this, "1b773e202ebab", "02c25a426f9eec179e5384460eac4194");
        DBHelper dbHelper = DBHelper.getInstance(this);
        Dao <UserInfo,Integer>dao = dbHelper.getDao(UserInfo.class);
        Where<UserInfo, Integer> where = dao.queryBuilder().where();
        try {
            List<UserInfo> LoginUserList = where.eq("isLogin", 1).query();
            if (LoginUserList!=null&&LoginUserList.size()>0){
                UserInfo userInfo = LoginUserList.get(0);
                userId = userInfo.get_id();
            }else{
                userId=-1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
