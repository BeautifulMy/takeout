package com.myproject.outtake.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.myproject.outtake.DBHelper;
import com.myproject.outtake.MyApp;
import com.myproject.outtake.model.net.bean.ResponseInfo;
import com.myproject.outtake.model.net.bean.UserInfo;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/20.
 */

public class LoginPresenter extends BasePresenter {
    private final Activity activity;
    private Savepoint savepoint;

    public LoginPresenter(Activity activity){
        this.activity = activity;
    }
    @Override
    protected void showerrorMessage(String message) {

    }

    @Override
    protected void parseJson(String json) {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        MyApp.userId = userInfo.get_id();
        if (userInfo!=null){
            DBHelper dbHelper = DBHelper.getInstance(activity);
            Dao <UserInfo,Integer>dao = dbHelper.getDao(UserInfo.class);
            AndroidDatabaseConnection databaseConnection = new AndroidDatabaseConnection(dbHelper.getReadableDatabase(), true);
            try {
                savepoint = databaseConnection.setSavePoint("start");
                databaseConnection.setAutoCommit(false);
                List<UserInfo> userInfoList = dao.queryForAll();
                if (userInfoList!=null&&userInfoList.size()>0){
                    for (int i = 0; i < userInfoList.size(); i++) {
                        UserInfo info = userInfoList.get(i);
                        info.setLogin(0);
                        dao.update(info);
                    }
                }

                UserInfo user = dao.queryForId(userInfo.get_id());
                if (user!=null){
                    userInfo.setLogin(1);
                    dao.update(user) ;
                }else{
                    userInfo.setLogin(1);
                    dao.create(userInfo);
                }
                databaseConnection.commit(savepoint);


            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    databaseConnection.rollback(savepoint);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public void getLoginData(String username,String pwd,  String phone,int type){
        Call<ResponseInfo> loginInfo = responseInfoApi.getLoginInfo(username, pwd, phone, type);
        loginInfo.enqueue(new myCallBack());
    }
}
