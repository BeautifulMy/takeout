package com.myproject.outtake;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;
import com.myproject.outtake.model.net.bean.UserInfo;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/20.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private HashMap<String,Dao> hashMap = new HashMap<>();
    private static DBHelper dbHelper = null;

    public DBHelper(Context context) {
        super(context, "takeout.db", null, 1);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);

        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserInfo.class);
            TableUtils.createTable(connectionSource, ReceiptAddressBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public Dao getDao(Class clazz){
        Dao dao = null;
        dao = hashMap.get(clazz.getSimpleName());
        if (dao==null){
            try {
                dao = super.getDao(clazz);
                hashMap.put(clazz.getSimpleName(),dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return dao;
    }

    @Override
    public void close() {
        for (String key : hashMap.keySet()) {
            Dao dao = hashMap.get(key);
            dao=null;
        }
    }
}
