package com.myproject.outtake.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;
import com.myproject.outtake.ui.adapter.AddressListAdapter;
import com.myproject.outtake.utils.ReceiptAddressDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddresssListActivity extends FragmentActivity {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_receipt_address)
    RecyclerView rvReceiptAddress;
    @Bind(R.id.tv_add_address)
    TextView tvAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReceiptAddressDao receiptAddressDao = new ReceiptAddressDao(this);
        List<ReceiptAddressBean> receiptAddressBeen = receiptAddressDao.queryUserAllAddress(MyApp.userId);
        AddressListAdapter addressListAdapter = new AddressListAdapter(receiptAddressBeen,this);
        rvReceiptAddress.setLayoutManager(new LinearLayoutManager(AddresssListActivity.this,LinearLayout.VERTICAL,false));
        rvReceiptAddress.setAdapter(addressListAdapter);
    }

    @OnClick({R.id.ib_back, R.id.tv_title, R.id.rv_receipt_address, R.id.tv_add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.rv_receipt_address:
                break;
            case R.id.tv_add_address:
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
