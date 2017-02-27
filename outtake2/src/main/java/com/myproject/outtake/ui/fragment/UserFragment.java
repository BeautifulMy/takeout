package com.myproject.outtake.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.myproject.outtake.DBHelper;
import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.UserInfo;
import com.myproject.outtake.ui.activity.LoginActivity;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/17.
 */
public class UserFragment extends BaseFragment {
    @Bind(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @Bind(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @Bind(R.id.login)
    ImageView login;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @Bind(R.id.iv_address)
    ImageView ivAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApp.userId!=-1){
            DBHelper dbHelper = DBHelper.getInstance(getActivity());
            Dao <UserInfo,Integer>dao = dbHelper.getDao(UserInfo.class);
            try {
                UserInfo userInfo = dao.queryForId(MyApp.userId);

                login.setVisibility(View.GONE);
                llUserinfo.setVisibility(View.VISIBLE);
                username.setText(userInfo.getName());
                phone.setText(userInfo.getPhone());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            login.setVisibility(View.VISIBLE);
            llUserinfo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.login)
    public void onClick() {
        startActivity(new Intent(getActivity(),LoginActivity.class));

    }
}
