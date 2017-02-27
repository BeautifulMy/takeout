package com.myproject.outtake.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.utils.CountPriceFormater;
import com.myproject.outtake.utils.ReceiptAddressDao;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOrderActivity extends FragmentActivity {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.iv_location)
    ImageView ivLocation;
    @Bind(R.id.tv_hint_select_receipt_address)
    TextView tvHintSelectReceiptAddress;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_label)
    TextView tvLabel;
    @Bind(R.id.tv_address)
    TextView tvAddress;

    @Bind(R.id.rl_location)
    RelativeLayout rlLocation;

    @Bind(R.id.iv_arrow)
    ImageView ivArrow;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_seller_name)
    TextView tvSellerName;
    @Bind(R.id.ll_select_goods)
    LinearLayout llSelectGoods;
    @Bind(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @Bind(R.id.tv_CountPrice)
    TextView tvCountPrice;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    private List<GoodsInfo> shopCardList;
    private float totalCountPrice = 0.0f;
    private Seller seller;

    String[] addressLabels = new String[]{"家", "公司", "学校"};
    //家  橙色
    //公司 蓝色
    //学校   绿色
    int[] bgLabels = new int[]{
            Color.parseColor("#fc7251"),//家  橙色
            Color.parseColor("#468ade"),//公司 蓝色
            Color.parseColor("#02c14b"),//学校   绿色
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        shopCardList = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCardList");
        seller = (Seller) getIntent().getSerializableExtra("seller");
        OncreateSelectGoods();
        tvDeliveryFee.setText("¥" + seller.getDeliveryFee());
        String deliveryFee = seller.getDeliveryFee();
        float floatDeliveryFee = Float.parseFloat(deliveryFee);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取商品总金额
        float totalMoney = totalCountPrice + floatDeliveryFee;
        tvCountPrice.setText("待支付:" + CountPriceFormater.format(totalMoney));

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                Message message = h.obtainMessage();
                super.run();
                ReceiptAddressDao receiptAddressDao = new ReceiptAddressDao(ConfirmOrderActivity.this);
                ReceiptAddressBean receiptAddressBean = receiptAddressDao.queryUserSelectAddress(MyApp.userId, 1);
                if (receiptAddressBean != null) {
                    message.obj = receiptAddressBean;
                    h.sendMessage(message);
                }
            }
        }.start();

    }

    Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            ReceiptAddressBean receiptAddressBean = (ReceiptAddressBean) msg.obj;
            showReceiptAddress(receiptAddressBean);
        }
    };


    private void OncreateSelectGoods() {
        llSelectGoods.removeAllViews();
        totalCountPrice = 0.0f;
        for (int i = 0; i < shopCardList.size(); i++) {
            View view = View.inflate(ConfirmOrderActivity.this, R.layout.item_confirm_order_goods, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCount = (TextView) view.findViewById(R.id.tv_count);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            GoodsInfo goodsInfo = shopCardList.get(i);
            tvName.setText(goodsInfo.getName());
            tvCount.setText(goodsInfo.getCount() + "");

            float goodsTotalPrice = goodsInfo.getCount() * goodsInfo.getNewPrice();

            totalCountPrice += goodsTotalPrice;
            tvPrice.setText(CountPriceFormater.format(goodsTotalPrice));
            llSelectGoods.addView(view);


        }

    }

    @OnClick({R.id.tvSubmit, R.id.rl_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                Intent intent = new Intent(this, PayOnlineActivity.class);
                intent.putExtra("shopCartList", (Serializable) shopCardList);
                intent.putExtra("seller", seller);
                startActivity(intent);
                break;
            case R.id.rl_location:
                Intent addressIntent = new Intent(ConfirmOrderActivity.this, AddresssListActivity.class);
                startActivityForResult(addressIntent, 100);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 100 && resultCode == 101) {
            ReceiptAddressBean receiptAddressBean = (ReceiptAddressBean) data.getSerializableExtra("address");
            showReceiptAddress(receiptAddressBean);
        }
    }

    private void showReceiptAddress(ReceiptAddressBean receiptAddressBean) {
        tvName.setText(receiptAddressBean.getName());
        tvSex.setText(receiptAddressBean.getSex());
        //显示必填电话号码和备用电话号码
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            tvPhone.setText(receiptAddressBean.getPhone()
                    + "," + receiptAddressBean.getPhoneOther());
        }
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone())
                && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            tvPhone.setText(receiptAddressBean.getPhone());
        }
        //label(学校,公司,家)
        tvLabel.setText(receiptAddressBean.getLabel());
        //根据label字符串获取tvLabel背景颜色
        int index = getIndex(receiptAddressBean.getLabel());
        tvLabel.setBackgroundColor(bgLabels[index]);

        tvAddress.setText(receiptAddressBean.getReceiptAddress()
                + receiptAddressBean.getDetailAddress());
    }

    private int getIndex(String label) {
        int position = -1;
        for (int i = 0; i < addressLabels.length; i++) {
            if (addressLabels[i].equals(label)) {
                position = i;
                break;
            }
        }
        return position;

    }
}
