package com.myproject.outtake.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myproject.outtake.Constants;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.GoodsTypeInfo;
import com.myproject.outtake.ui.activity.BusinessActivity;
import com.myproject.outtake.utils.CountPriceFormater;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/22.
 */
public class ShopCardAdapter extends RecyclerView.Adapter {
    BusinessActivity activity;
    ArrayList<GoodsInfo> shopCartList;

    public ShopCardAdapter(BusinessActivity businessActivity) {
        this.activity = businessActivity;

    }

    public void setShopCartList(ArrayList<GoodsInfo> shopCardList) {
        this.shopCartList = shopCardList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_cart, null, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsInfo goodsInfo = shopCartList.get(position);
        ((MyHolder) holder).tvName.setText(goodsInfo.getName());
        ((MyHolder) holder).tvCount.setText(goodsInfo.getCount() + "");
        float typeAllPirce = goodsInfo.getCount() * goodsInfo.getNewPrice();
        ((MyHolder) holder).tvTypeAllPrice.setText(CountPriceFormater.format(typeAllPirce));

        ((MyHolder) holder).setPosition(position);
    }

    @Override
    public int getItemCount() {

        if (shopCartList != null && shopCartList.size() > 0) {
            return shopCartList.size();
        }
        return 0;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_type_all_price)
        TextView tvTypeAllPrice;
        @Bind(R.id.ib_minus)
        ImageButton ibMinus;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.ib_add)
        ImageButton ibAdd;
        @Bind(R.id.ll)
        LinearLayout ll;
        private int position;

        MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @OnClick({R.id.ib_add, R.id.ib_minus})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ib_add:
                    addGoods(Constants.GOOD_ADD);
                    break;
                case R.id.ib_minus:
                    deleteGoods(Constants.GOOD_DELETE);
                    break;
            }
        }

        private void deleteGoods(int goodDelete) {
            updateGoodsAdapter(goodDelete);
            updateGoodsTypeAdapter(goodDelete);
            notifyDataSetChanged();
            activity.updateShopCart();
            if (shopCartList.get(position).getCount() == 0) {
                shopCartList.remove(shopCartList.get(0));
            }
            if (shopCartList.size() == 0) {
                activity.dismissSheetView();
            }
        }

        private void addGoods(int goodAdd) {
            updateGoodsAdapter(goodAdd);
            updateGoodsTypeAdapter(goodAdd);
            notifyDataSetChanged();
            activity.updateShopCart();
        }

        private void updateGoodsTypeAdapter(int oprate) {
            int typeId = shopCartList.get(position).getTypeId();
            List<GoodsTypeInfo> goodsTypeInfos = myFragmentPagerAdapter.getGoodsFragment().goodsTypeAdapter.getData();
            if (goodsTypeInfos != null) {
                for (int i = 0; i < goodsTypeInfos.size(); i++) {
                    if (goodsTypeInfos.get(i).getId() == typeId) {
                        switch (oprate) {
                            case Constants.GOOD_ADD:
                                int i1 = goodsTypeInfos.get(i).getCount() + 1;
                                goodsTypeInfos.get(i).setCount(i1);


                                break;
                            case Constants.GOOD_DELETE:
                                if (goodsTypeInfos.get(i).getCount() > 0) {
                                    int i2 = goodsTypeInfos.get(i).getCount() - 1;
                                    goodsTypeInfos.get(i).setCount(i2);
                                }
                                break;
                        }
                    }
                    myFragmentPagerAdapter.getGoodsFragment().goodsTypeAdapter. notifyDataSetChanged();

                }
            }
        }

        private void updateGoodsAdapter(int operete) {
            int id = shopCartList.get(position).getId();
            ArrayList<GoodsInfo> goodsAdapterInfo = myFragmentPagerAdapter.getGoodsFragment().goodsAdapter.getData();
            if (goodsAdapterInfo != null) {
                for (int i = 0; i < goodsAdapterInfo.size(); i++) {
                    if (goodsAdapterInfo.get(i).getId() == id) {
                        switch (operete) {
                            case Constants.GOOD_ADD:
                                int count = goodsAdapterInfo.get(i).getCount() + 1;
                                goodsAdapterInfo.get(i).setCount(count);
                                break;
                            case Constants.GOOD_DELETE:
                                if (goodsAdapterInfo.get(i).getCount() > 0) {
                                    int count2 = goodsAdapterInfo.get(i).getCount() - 1;
                                    goodsAdapterInfo.get(i).setCount(count2);
                                }

                        }
                    }
                    myFragmentPagerAdapter.getGoodsFragment().goodsAdapter.notifyDataSetChanged();
                }
            }


        }
    }


}
