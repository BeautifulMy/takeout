package com.myproject.outtake.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.GoodsTypeInfo;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.ui.adapter.ShopCardAdapter;
import com.myproject.outtake.ui.adapter.myFragmentPagerAdapter;
import com.myproject.outtake.ui.fragment.GoodsFragment;
import com.myproject.outtake.utils.CountPriceFormater;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusinessActivity extends FragmentActivity {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ib_menu)
    ImageButton ibMenu;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;
    @Bind(R.id.imgCart)
    ImageView imgCart;
    @Bind(R.id.tvSelectNum)
    TextView tvSelectNum;
    @Bind(R.id.tvCountPrice)
    TextView tvCountPrice;
    @Bind(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @Bind(R.id.tvSendPrice)
    TextView tvSendPrice;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.bottom)
    LinearLayout bottom;
    @Bind(R.id.fl_Container)
    FrameLayout flContainer;
    private String[] stringArray = new String[]{"商品", "评价", "商家"};
    private Seller seller;
    public  static ShopCardAdapter shopCardAdapter;
    private ArrayList<GoodsInfo> shopCardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        seller = (Seller) intent.getSerializableExtra("seller");
        /*for (int i = 0; i < stringArray.length; i++) {
            //选项卡个数和字符串的个数一致
            tabs.addTab(tabs.newTab().setText(stringArray[i]));
        }*/
        myFragmentPagerAdapter myFragmentPagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager(), stringArray, seller);
        vp.setAdapter(myFragmentPagerAdapter);
        tabs.setupWithViewPager(vp);
        tvTitle.setText(seller.getName());
        tvDeliveryFee.setText("配送费:¥" + seller.getDeliveryFee());
        tvSendPrice.setText("起送价:¥" + seller.getSendPrice());


    }

    public void addFlyView(View v, int width, int height) {
        if (v != null) {
            flContainer.addView(v, width, height);
        }
    }

    public int[] getLocationResource() {
        int[] LocationResource = new int[2];
        imgCart.getLocationInWindow(LocationResource);
        return LocationResource;
    }

    public void updateShopCart() {
        int totalCount = 0;
        int totalPrice = 0;
        ArrayList<GoodsInfo> goodsInfos = myFragmentPagerAdapter.getGoodsFragment().goodsAdapter.getData();
        for (int i = 0; i < goodsInfos.size(); i++) {
            GoodsInfo goodsInfo = goodsInfos.get(i);
            if (goodsInfo.getCount() > 0) {
                totalCount += goodsInfo.getCount();
                totalPrice += goodsInfo.getCount() * goodsInfo.getNewPrice();
                //shopCardList.add(goodsInfo);
            }
            refreshShopCartUI(totalCount, totalPrice);


        }


    }

    private void refreshShopCartUI(int totalCount, int totalPrice) {
        if (totalCount > 0) {
            tvSelectNum.setVisibility(View.VISIBLE);
            tvSelectNum.setText(totalCount + "");
            tvCountPrice.setText(CountPriceFormater.format(totalPrice));
        } else {
            tvSelectNum.setVisibility(View.INVISIBLE);
            tvCountPrice.setText(CountPriceFormater.format(0.0f));
        }
        String sendPrice = seller.getSendPrice();
        float floatSendPrice = Float.parseFloat(sendPrice);
        if (totalPrice > floatSendPrice) {
            tvSubmit.setVisibility(View.VISIBLE);
            tvSendPrice.setVisibility(View.GONE);
        } else {
            tvSubmit.setVisibility(View.GONE);
            tvSendPrice.setVisibility(View.VISIBLE);
        }


    }

    private View sheet;

    @OnClick({R.id.tvSubmit, R.id.bottom})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvSubmit:
                Intent intent = new Intent(BusinessActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("shopCardList", getshopCardList());
                intent.putExtra("seller",seller);
                startActivity(intent);
                break;
            case R.id.bottom:
                //updateShopCart();
                if (sheet == null) {
                    sheet = onCreateSheetView();
                }
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                } else {
                   shopCardList= getshopCardList();
                    shopCardAdapter.setShopCartList(shopCardList);
                    shopCardAdapter.notifyDataSetChanged();
                    bottomSheetLayout.showWithSheetView(sheet);
                    myFragmentPagerAdapter.getGoodsFragment().goodsAdapter.notifyDataSetChanged();
                    myFragmentPagerAdapter.getGoodsFragment().goodsTypeAdapter.notifyDataSetChanged();
                }


                break;
        }

    }


    private View onCreateSheetView() {
        View view = View.inflate(this, R.layout.cart_list, null);
        shopCardList = getshopCardList();


        RecyclerView rvcard = (RecyclerView) view.findViewById(R.id.rvCart);
        TextView tvClear = (TextView) view.findViewById(R.id.tvClear);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(BusinessActivity.this);
                builder.setTitle("是否清空购物车");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearGoodsAdapter();
                        clearGoodsTypeAdapter();
                        clearShopCartAdapter();
                        updateShopCart();

                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        shopCardAdapter = new ShopCardAdapter(this);
        shopCardAdapter.setShopCartList(shopCardList);
        rvcard.setLayoutManager(new LinearLayoutManager(this));
        rvcard.setAdapter(shopCardAdapter);


        return view;

    }

    @NonNull
    private ArrayList<GoodsInfo> getshopCardList() {
        final ArrayList<GoodsInfo> shopCardList = new ArrayList<>();
        ArrayList<GoodsInfo> goodsInfos = myFragmentPagerAdapter.getGoodsFragment().goodsAdapter.getData();
        for (int i = 0; i < goodsInfos.size(); i++) {
            GoodsInfo goodsInfo = goodsInfos.get(i);
            if (goodsInfo.getCount() > 0) {

                shopCardList.add(goodsInfo);

            }
        }
        return shopCardList;
    }

    public void dismissSheetView() {
        bottomSheetLayout.dismissSheet();
    }
    private void clearShopCartAdapter() {
        ArrayList<GoodsInfo> shopCartList = getshopCardList();
        for (int i = 0; i < shopCartList.size(); i++) {
            GoodsInfo goodsInfo = shopCartList.get(i);
            if (goodsInfo.getCount()>0){
                goodsInfo.setCount(0);
            }
        }
        shopCardAdapter.notifyDataSetChanged();
    }

    private void clearGoodsTypeAdapter() {
        GoodsFragment goodsFragment = myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment!=null){
            List<GoodsTypeInfo> goodsTypeInfoList = goodsFragment.goodsTypeAdapter.getData();
            for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
                if (goodsTypeInfo.getCount()>0){
                    //将购买商品的数量设置为0
                    goodsTypeInfo.setCount(0);
                }
            }
            goodsFragment.goodsTypeAdapter.notifyDataSetChanged();
        }
    }

    private void clearGoodsAdapter() {
        GoodsFragment goodsFragment =myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment!=null){
            ArrayList<GoodsInfo> goodsInfoList = goodsFragment.goodsAdapter.getData();
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getCount()>0){
                    //将购买商品的数量设置为0
                    goodsInfo.setCount(0);
                }
            }
            goodsFragment.goodsAdapter.notifyDataSetChanged();
        }
    }
}
