package com.myproject.outtake.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.ui.fragment.BaseFragment;
import com.myproject.outtake.ui.fragment.GoodsFragment;
import com.myproject.outtake.ui.fragment.SellerFragment;
import com.myproject.outtake.ui.fragment.SuggestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public class myFragmentPagerAdapter extends FragmentPagerAdapter{
        private static List<Fragment> fragmentList = new ArrayList<>();
    private final String[] stringArray;
    private final Seller seller;

    public myFragmentPagerAdapter(FragmentManager fm, String[] stringArray, Seller seller) {
        super(fm);
        this.stringArray = stringArray;
        this.seller = seller;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment baseFragment=null;
        switch (position) {
            case 0:
                baseFragment = new GoodsFragment();
                break;
            case 1:
                baseFragment = new SuggestFragment();
                break;
            case 2:
                baseFragment = new SellerFragment();
                break;
        }
        if (!fragmentList.contains(baseFragment)){
            fragmentList.add(baseFragment);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("seller",seller);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    public int getCount() {
        return stringArray.length;
    }
    public static GoodsFragment getGoodsFragment(){
        if (fragmentList!=null && fragmentList.size()>0){
            GoodsFragment goodsFragment = (GoodsFragment) fragmentList.get(0);
            return goodsFragment;
        }
        return null;
    }
}
