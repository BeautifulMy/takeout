package com.myproject.outtake.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.myproject.outtake.R;
import com.myproject.outtake.ui.activity.LocalSourseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/25.
 */
public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.MyHolder> {
    private final LocalSourseActivity activity;
    private final ArrayList<PoiItem> poiItems;

    public AddressSelectAdapter(LocalSourseActivity localSourseActivity, ArrayList<PoiItem> poiItems) {
        this.activity = localSourseActivity;
        this.poiItems = poiItems;
    }

    @Override
    public AddressSelectAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_select_receipt_address, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tvTitle.setText(poiItems.get(position).getTitle());
        holder.tvSnippet.setText(poiItems.get(position).getSnippet());
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (poiItems != null && poiItems.size() > 0) {
            return poiItems.size();
        }
        return 0;
    }

 class MyHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_snippet)
        TextView tvSnippet;
     private int position;

     MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 PoiItem poiItem = poiItems.get(position);
                 Intent intent = new Intent();
                 intent.putExtra("title",poiItem.getTitle());
                 intent.putExtra("snippt",poiItem.getSnippet());
                 activity.setResult(101,intent);
                 activity.finish();
             }
         });
        }

     public void setPosition(int position) {
         this.position = position;
     }
 }
}
