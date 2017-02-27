package com.myproject.outtake.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.utils.CountPriceFormater;
import com.myproject.outtake.utils.OrderInfoUtil2_0;
import com.myproject.outtake.utils.PayResult;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayOnlineActivity extends FragmentActivity {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_residualTime)
    TextView tvResidualTime;
    @Bind(R.id.tv_order_name)
    TextView tvOrderName;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv_order_detail)
    TextView tvOrderDetail;
    @Bind(R.id.iv_triangle)
    ImageView ivTriangle;
    @Bind(R.id.ll_order_toggle)
    RelativeLayout llOrderToggle;
    @Bind(R.id.tv_receipt_connect_info)
    TextView tvReceiptConnectInfo;
    @Bind(R.id.tv_receipt_address_info)
    TextView tvReceiptAddressInfo;
    @Bind(R.id.ll_goods)
    LinearLayout llGoods;
    @Bind(R.id.ll_order_detail)
    LinearLayout llOrderDetail;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.iv_pay_alipay)
    ImageView ivPayAlipay;
    @Bind(R.id.cb_pay_alipay)
    CheckBox cbPayAlipay;
    @Bind(R.id.tv_selector_other_payment)
    TextView tvSelectorOtherPayment;
    @Bind(R.id.ll_hint_info)
    LinearLayout llHintInfo;
    @Bind(R.id.iv_pay_wechat)
    ImageView ivPayWechat;
    @Bind(R.id.cb_pay_wechat)
    CheckBox cbPayWechat;
    @Bind(R.id.iv_pay_qq)
    ImageView ivPayQq;
    @Bind(R.id.cb_pay_qq)
    CheckBox cbPayQq;
    @Bind(R.id.iv_pay_fenqile)
    ImageView ivPayFenqile;
    @Bind(R.id.cb_pay_fenqile)
    CheckBox cbPayFenqile;
    @Bind(R.id.ll_other_payment)
    LinearLayout llOtherPayment;
    @Bind(R.id.bt_confirm_pay)
    Button btConfirmPay;
    private float totalCountPrice;
    private List<GoodsInfo> shopCartList;
    private Seller seller;
    public static final String APPID = "2016112003012990";
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMPjSeEXkAqakBi2enQ1Chi+PDWC5hyKZs4tNwoeCrwJncoIerYSNW7IMYPmbiZ0bQwlZNx9EhYA3Bbm0mA23vUIJNm5EUfchr4AM4DFPqA40UZb6Mz5pGzyA4Rb+bTwO4VrAmf8iOFq26eBZWpr85jiVk/7B0dW/HGddxy8c/FZAgMBAAECgYBl7HmgeVYlbk7TzP7iQEbEoRdK8JUy/ICJftVImmETfh1v5gGTgt3yio/ZBakCsUmcLEjSwPEMKd5avDdygJp5EnD+D1Nuow6YxUQfExVji4ZocIvRivZj3QZo4b7Xt06oWPh0OwYJJ0UAVC8CdnyTxzfuWcoYWDAKFwEZIAxykQJBAPY6JSkbaE+xcTQU8kai/lxAmRe4qiF/RCvd/hZzd+IPLq/hJcUqNsWkT9a5NDOuMKg1vEuoHyko23OqajiFjI8CQQDLqah1SH/MRqHIB1dlj1xOirOFMxfkjDqORmRtZ8cdMzfVZkl8wCP0PLiAm0kRJm4N5W/3nnFX5QvqWo4NF+eXAkEAlx/Y7wIDY+ZktLKmgPRJahW74PNWeHjEPqhh6yWzzuvCm/B0Xi8qruPKnN/PSmj/ND7G8yic94Y8KyHNUCOnwQJBAJvNdqoChIHppuo3c4ymV59eTGeh5q1Y+ZLOFxX7Rj/4ZsZCMgXVl6vIp/z6zrLoC1lmJHnyJBNxjeQC0pkBqJECQC3mfJ+1XXfkv7Zj7zmuywuLNAB0VkCOsDpywmHWN9ZZ2z97wSjHBfz2xmkrM+T3gMXWHbj22cBIPeKLvuREI+Y=";
    private static final int SDK_PAY_FLAG = 100;
    @SuppressLint("HandlerLeak")
   private Handler handler =  new Handler(){
        @SuppressWarnings("unused")
        @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case SDK_PAY_FLAG:
                   /**
                    对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    */
                   PayResult payResult = new PayResult((Map<String,String>)msg.obj);
                   String resultStatus = payResult.getResultStatus();
                   if (TextUtils.equals(resultStatus, "9000")){
                       // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                       Toast.makeText(PayOnlineActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                       //给公司的服务器发送请求,告知服务器客户端是支付成功了,服务器你也改一下支付状态吧
                       //好的,我的状态改过了,你给用户显示这个支付成功的结果吧
                   }else{
                       // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                       Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                   }
                   break;

           }
       }
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        ButterKnife.bind(this);
        shopCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCartList");
        seller = (Seller) getIntent().getSerializableExtra("seller");
        onCreateSelectGoodsList();

        //获取运费总金额
        String deliveryFee = seller.getDeliveryFee();
        float floatDeliveryFee = Float.parseFloat(deliveryFee);

        //获取商品总金额
        float totalMoney = totalCountPrice+floatDeliveryFee;

        tvPayMoney.setText(CountPriceFormater.format(totalMoney));

    }
    @OnClick({R.id.iv_triangle,R.id.bt_confirm_pay})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_triangle:
                int visibility = llOrderDetail.getVisibility();
                if(visibility == View.VISIBLE){
                    llOrderDetail.setVisibility(View.GONE);
                }else{
                    llOrderDetail.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bt_confirm_pay:
                payV2();
                break;
        }
    }

    private void payV2() {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayOnlineActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message message = new Message();
                message.what = SDK_PAY_FLAG;
                message.obj = result;
                handler.sendMessage(message);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private void onCreateSelectGoodsList() {
        llGoods.removeAllViews();
        for (int i = 0; i < shopCartList.size(); i++) {
            View view = View.inflate(this, R.layout.item_confirm_order_goods, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCount = (TextView) view.findViewById(R.id.tv_count);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            GoodsInfo goodsInfo = shopCartList.get(i);
            tvName.setText(goodsInfo.getName());
            tvCount.setText(goodsInfo.getCount() + "");

            float goodsTotalPrice = goodsInfo.getCount() * goodsInfo.getNewPrice();
            totalCountPrice += goodsTotalPrice;

            tvPrice.setText(CountPriceFormater.format(goodsTotalPrice));

            llGoods.addView(view);
        }
    }
}
