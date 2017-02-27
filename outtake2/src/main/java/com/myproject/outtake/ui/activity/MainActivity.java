package com.myproject.outtake.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.myproject.outtake.R;
import com.myproject.outtake.ui.fragment.HomeFragment;
import com.myproject.outtake.ui.fragment.MoreFragment;
import com.myproject.outtake.ui.fragment.OrderFragment;
import com.myproject.outtake.ui.fragment.UserFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Bind(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;
    @Bind(R.id.main_bottome_switcher_container)
    LinearLayout mainBottomeSwitcherContainer;
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initClick();
        onClick(mainBottomeSwitcherContainer.getChildAt(0));

        //判断应用当前是否有如下定位权限,如果没有则向手机请求权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            //请求定位权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATION);
        }

    }


    private void initClick() {
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            FrameLayout childFrameLayout = (FrameLayout) mainBottomeSwitcherContainer.getChildAt(i);
            childFrameLayout.setOnClickListener(this);
        }

    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new UserFragment());
        fragmentList.add(new MoreFragment());
    }

    @Override
    public void onClick(View view) {
        int i = mainBottomeSwitcherContainer.indexOfChild(view);
        changeButton(i);
        changeFragment(i);
    }

    private void changeFragment(int i) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_fragment_container,fragmentList.get(i)).commit();
    }

    private void changeButton(int i) {
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i1 = 0; i1 <childCount ; i1++) {
            View childAt = mainBottomeSwitcherContainer.getChildAt(i1);
            if (i==i1){
                setEnable(childAt,false);
            }else{
                setEnable(childAt,true);
            }
        }
    }

    private void setEnable(View childAt, boolean b) {
        if (childAt instanceof ViewGroup){
            int childCount = ((ViewGroup) childAt).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt1 = ((ViewGroup) childAt).getChildAt(i);
                childAt1.setEnabled(b);
            }
        }
    }
    private static final int MY_PERMISSIONS_LOCATION = 100;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        //如果请求状态码保持一致
        if (requestCode == MY_PERMISSIONS_LOCATION)
        {
            //获取请求结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission Allow
                Toast.makeText(MainActivity.this, "Permission Allow", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
