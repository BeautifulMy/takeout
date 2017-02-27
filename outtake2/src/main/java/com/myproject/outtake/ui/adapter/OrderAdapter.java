package com.myproject.outtake.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.outtake.Orderobserver;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.Order;
import com.myproject.outtake.ui.fragment.OrderDetailActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/25.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> implements Observer {
    private Activity activity;
    private List<Order> data;


    public OrderAdapter(Activity activity) {
        this.activity = activity;
        Orderobserver.getInstance().addObserver(this);

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_order_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tvOrderItemSellerName.setText(data.get(position).getSeller().getName());
        getType(data.get(position).getType());

        holder.tvOrderItemType.setText(text);
        holder.setPosition(position);
    }

    private String text;

    private void getType(String type) {
        switch (type) {
            case Orderobserver.ORDERTYPE_UNPAYMENT:
                text = "未支付";
                break;
            case Orderobserver.ORDERTYPE_SUBMIT:
                text = "已提交订单";
                break;
            case Orderobserver.ORDERTYPE_RECEIVEORDER:
                text = "商家接单";
                break;
            case Orderobserver.ORDERTYPE_DISTRIBUTION:
                text = "配送中,等待送达";
                break;
            case Orderobserver.ORDERTYPE_SERVED:
                text = "已送达";
                break;
            case Orderobserver.ORDERTYPE_CANCELLEDORDER:
                text = "取消的订单";
                break;
        }
    }

    public void setData(List<Order> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap<String, String> hashmap = (HashMap<String, String>) o;
        String orderId = hashmap.get("orderId");
        String type = hashmap.get("type");
        int position = -1;
        for (int i = 0; i < data.size(); i++) {

            String id = data.get(i).getId();
            if (id.equals(orderId)) {
                data.get(i).setType(type);
                position = i;
                break;
            }

        }
        if(position!=-1)

        ;{
            notifyItemChanged(position);
    }


    }

    class MyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_order_item_seller_logo)
        ImageView ivOrderItemSellerLogo;
        @Bind(R.id.tv_order_item_seller_name)
        TextView tvOrderItemSellerName;
        @Bind(R.id.tv_order_item_type)
        TextView tvOrderItemType;
        @Bind(R.id.tv_order_item_time)
        TextView tvOrderItemTime;
        @Bind(R.id.tv_order_item_foods)
        TextView tvOrderItemFoods;
        @Bind(R.id.tv_order_item_money)
        TextView tvOrderItemMoney;
        @Bind(R.id.tv_order_item_multi_function)
        TextView tvOrderItemMultiFunction;
        private int position;

        MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, OrderDetailActivity.class);
                    intent.putExtra("orderId", data.get(position).getId());
                    intent.putExtra("type", data.get(position).getType());
                    activity.startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
