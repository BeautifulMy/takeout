package com.myproject.outtake.utils;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.myproject.outtake.DBHelper;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */

public class ReceiptAddressDao {

    private final Dao<ReceiptAddressBean, Integer> dao;

    public ReceiptAddressDao(Context context){
        DBHelper dbHelper = new DBHelper(context);
        dao = dbHelper.getDao(ReceiptAddressBean.class);
    }
    public  void insert(ReceiptAddressBean receiptAddressBean){
        try {
            dao.create(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(ReceiptAddressBean receiptAddressBean){
        try {
            dao.delete(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(ReceiptAddressBean receiptAddressBean){
        try {
            dao.update(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<ReceiptAddressBean> queryUserAllAddress(int userId){
        Where<ReceiptAddressBean, Integer> where = dao.queryBuilder().where();
        List<ReceiptAddressBean> receiptAddressBeanList = null;
        try {
            receiptAddressBeanList = where.eq("uid", userId).query();
            return receiptAddressBeanList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public ReceiptAddressBean queryUserSelectAddress(int userId, int isSelect){
        Where<ReceiptAddressBean, Integer> where = dao.queryBuilder().where();
        try {
            List<ReceiptAddressBean> queryList = where.eq("uid", userId).and().eq("isSelect", 1).query();
            //ReceiptAddressBean receiptAddressBean = queryList.get(0);
            if (queryList!=null && queryList.size()>0){
                ReceiptAddressBean receiptAddressBean = queryList.get(0);
                return receiptAddressBean;
            }
        } catch (SQLException e) {
            int a  = 0;
        }
        return null;
    }
}
