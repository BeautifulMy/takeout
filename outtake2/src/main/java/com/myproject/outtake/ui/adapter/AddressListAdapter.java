package com.myproject.outtake.ui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;
import com.myproject.outtake.ui.activity.AddAddressActivity;
import com.myproject.outtake.ui.activity.AddresssListActivity;
import com.myproject.outtake.utils.ReceiptAddressDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/23.
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyHolder> {
    private final List<ReceiptAddressBean> receiptAddressBean;
    private final AddresssListActivity activity;
    String[] addressLabels = new String[]{"家", "公司", "学校"};

    //家  橙色
    //公司 蓝色
    //学校   绿色
    int[] bgLabels = new int[]{
            Color.parseColor("#fc7251"),//家  橙色
            Color.parseColor("#468ade"),//公司 蓝色
            Color.parseColor("#02c14b"),//学校   绿色
    };
    private final ReceiptAddressDao receiptAddressDao;

    public AddressListAdapter(List<ReceiptAddressBean> receiptAddressBean, AddresssListActivity addresssListActivity) {
        this.receiptAddressBean = receiptAddressBean;
        this.activity = addresssListActivity;
        receiptAddressDao = new ReceiptAddressDao(activity);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_receipt_address, null);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ReceiptAddressBean receiptAddressList = receiptAddressBean.get(position);
        holder.tvName.setText(receiptAddressList.getName());
        holder.tvSex.setText(receiptAddressList.getSex());
        if (!TextUtils.isEmpty(receiptAddressList.getPhone())
                && !TextUtils.isEmpty(receiptAddressList.getPhoneOther())) {
            holder.tvPhone.setText(receiptAddressList.getPhone()
                    + "," + receiptAddressList.getPhoneOther());
        }
        if (!TextUtils.isEmpty(receiptAddressList.getPhone())
                && TextUtils.isEmpty(receiptAddressList.getPhoneOther())) {
            holder.tvPhone.setText(receiptAddressList.getPhone());
        }
        holder.tvLabel.setText(receiptAddressList.getLabel());
        holder.tvLabel.setBackgroundColor(bgLabels[getIndex(receiptAddressList.getLabel())]);
        holder.tvAddress.setText(receiptAddressList.getReceiptAddress() + receiptAddressList.getDetailAddress());
        if (receiptAddressList.isSelect() == 1) {
            //此条目选中送货地址

            holder.cb.setChecked(true);
        } else {

            holder.cb.setChecked(false);
        }
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return receiptAddressBean.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb)
        CheckBox cb;
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
        @Bind(R.id.iv_edit)
        ImageView ivEdit;
        private int position;

        MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < receiptAddressBean.size(); i++) {
                        ReceiptAddressBean receiptAddressList = receiptAddressBean.get(i);
                        if (i==position){
                            receiptAddressList.setSelect(1);

                        }else{
                            receiptAddressList.setSelect(0);
                        }
                       receiptAddressDao.update(receiptAddressList);
                    }
                    notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.putExtra("address",receiptAddressBean.get(position));
                    activity.setResult(101,intent);
                    activity.finish();
                }
            });
        }
        @OnClick(R.id.iv_edit)
        public void onClick(View view){
            switch (view.getId()){
                case R.id.iv_edit:
                    Intent intent = new Intent(activity, AddAddressActivity.class);
                    intent.putExtra("address",receiptAddressBean.get(position));
                    activity.startActivity(intent);
                    break;
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }

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
