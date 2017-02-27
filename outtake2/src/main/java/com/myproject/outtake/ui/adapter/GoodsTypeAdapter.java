package com.myproject.outtake.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myproject.outtake.Constants;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsTypeInfo;
import com.myproject.outtake.ui.fragment.GoodsFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/18.
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter {
    public final GoodsFragment goodsFragment;
    public int currentPosition = 0;
    private List<GoodsTypeInfo> goodsTypelist;

    public GoodsTypeAdapter(GoodsFragment goodsFragment, ShopCardAdapter shopCardAdapter) {
        this.goodsFragment = goodsFragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, null, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).type.setText(goodsTypelist.get(position).getName());
        ((MyHolder) holder).setPosition(position);
        if (position == currentPosition) {
            ((MyHolder) holder).itemView.setBackgroundColor(Color.WHITE);
            ((MyHolder) holder).type.setTextColor(Color.GREEN);

        } else {
            ((MyHolder) holder).itemView.setBackgroundColor(Color.LTGRAY);
            ((MyHolder) holder).type.setTextColor(Color.BLACK);
        }if (goodsTypelist.get(position).getCount()>0){
            ((MyHolder) holder).tvCount.setText((goodsTypelist.get(position).getCount()+""));
            ((MyHolder) holder).tvCount.setVisibility(View.VISIBLE);
        }else{
            ((MyHolder) holder).tvCount.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        if (goodsTypelist != null && goodsTypelist.size() > 0){
            return goodsTypelist.size();
        }
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvCount)
        TextView tvCount;
        @Bind(R.id.type)
        TextView type;
        private int position;

        MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = position;
                    notifyDataSetChanged();
                    goodsFragment.switchGoodsInfo(goodsTypelist.get(position).getId());
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public void setData(List<GoodsTypeInfo> goodsTypelist) {
        this.goodsTypelist = goodsTypelist;
        notifyDataSetChanged();
    }
    public List<GoodsTypeInfo> getData(){
        return goodsTypelist;
    }
    public void refreGoodtypeInfo(int oprate,int typeId){
        for (int i = 0; i < goodsTypelist.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = goodsTypelist.get(i);
            if (goodsTypeInfo.getId()==typeId){
                switch (oprate){
                    case Constants.GOOD_ADD:
                        goodsTypeInfo.setCount( goodsTypeInfo.getCount() + 1);
                        break;
                    case Constants.GOOD_DELETE:
                        if (goodsTypeInfo.getCount()>0){
                            goodsTypeInfo.setCount(goodsTypeInfo.getCount()-1);
                        }
                        break;
                }

            }
            notifyDataSetChanged();
        }
    }
}
