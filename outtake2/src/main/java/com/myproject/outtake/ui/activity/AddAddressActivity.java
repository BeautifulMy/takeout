package com.myproject.outtake.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.ReceiptAddressBean;
import com.myproject.outtake.utils.ReceiptAddressDao;
import com.myproject.outtake.utils.SMSUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddressActivity extends FragmentActivity implements View.OnFocusChangeListener {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ib_delete)
    ImageButton ibDelete;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.rb_man)
    RadioButton rbMan;
    @Bind(R.id.rb_women)
    RadioButton rbWomen;
    @Bind(R.id.rg_sex)
    RadioGroup rgSex;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.ib_delete_phone)
    ImageButton ibDeletePhone;
    @Bind(R.id.ib_add_phone_other)
    ImageButton ibAddPhoneOther;
    @Bind(R.id.et_phone_other)
    EditText etPhoneOther;
    @Bind(R.id.ib_delete_phone_other)
    ImageButton ibDeletePhoneOther;
    @Bind(R.id.rl_phone_other)
    RelativeLayout rlPhoneOther;
    @Bind(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    @Bind(R.id.et_detail_address)
    EditText etDetailAddress;
    @Bind(R.id.tv_label)
    TextView tvLabel;
    @Bind(R.id.ib_select_label)
    ImageView ibSelectLabel;
    @Bind(R.id.bt_ok)
    Button btOk;
    String[] addressLabels = new String[]{"家", "公司", "学校"};
    int[] bgLabels = new int[]{
            Color.parseColor("#fc7251"),//家  橙色
            Color.parseColor("#468ade"),//公司 蓝色
            Color.parseColor("#02c14b")};//学校   绿色
    private ReceiptAddressBean receiptAddressBean;
    private ReceiptAddressDao receiptAddressDao;
    private String receiptAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_receipt_address);
        ButterKnife.bind(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    ibDeletePhone.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhone.setVisibility(View.GONE);
                }
            }
        });
        etPhoneOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneOther = etPhoneOther.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneOther)) {
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }
            }
        });
        receiptAddressDao = new ReceiptAddressDao(this);


        etPhone.setOnFocusChangeListener(this);
        etPhoneOther.setOnFocusChangeListener(this);
        receiptAddressBean = (ReceiptAddressBean) getIntent().getSerializableExtra("address");
        showReceiptAddressBean();

    }

    private void showReceiptAddressBean() {
        if (receiptAddressBean!=null){
            etName.setText(receiptAddressBean.getName());
            String sex = receiptAddressBean.getSex();
            if (sex.equals("男性")) {
                rgSex.check(R.id.rb_man);
            } else {
                rgSex.check(R.id.rb_women);
            }
            etPhone.setText(receiptAddressBean.getPhone());
            etPhoneOther.setText(receiptAddressBean.getPhoneOther());
            tvReceiptAddress.setText(receiptAddressBean.getReceiptAddress());
            etDetailAddress.setText(receiptAddressBean.getDetailAddress());

            tvLabel.setText(receiptAddressBean.getLabel());
            int index = getIndex(receiptAddressBean.getLabel());
            tvLabel.setBackgroundColor(bgLabels[index]);
        }
    }

    private int getIndex(String label) {
        int position = -1;
        for (int i = 0; i < addressLabels.length; i++) {
            if (addressLabels[i].equals(label)){
                position = i;
                break;
            }
        }
        return position;
    }



    @OnClick({R.id.tv_receipt_address, R.id.ib_back, R.id.ib_delete, R.id.ib_delete_phone, R.id.ib_add_phone_other, R.id.et_phone_other, R.id.ib_delete_phone_other, R.id.ib_select_label, R.id.bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_delete:
                deleteAdress();

                break;
            case R.id.ib_delete_phone:
                etPhone.setText("");
                break;
            case R.id.ib_add_phone_other:
                if (rlPhoneOther.getVisibility() == View.VISIBLE) {
                    rlPhoneOther.setVisibility(View.GONE);
                } else {
                    rlPhoneOther.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ib_delete_phone_other:
                etPhoneOther.setText("");
                break;
            case R.id.ib_select_label:
                showLabelDialog();
                break;
            case R.id.bt_ok:
                if (checkData()) {
                    if (receiptAddressBean == null) {
                        createReceiptAdress();
                    } else {
                        updateReceiptAdress();
                    }
                }
                finish();
                break;
            case R.id.tv_receipt_address:
                Intent intent = new Intent(this, LocalSourseActivity.class);
                startActivityForResult(intent,100);
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null&&requestCode==100&&resultCode==101){
            String title = data.getStringExtra("title");
            String snippt = data.getStringExtra("snippt");

            tvReceiptAddress.setText(title);
        }
    }

    private void deleteAdress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否删除此地址?");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //对不为空receiptAddressBean进行删除
                if (receiptAddressBean != null) {
                    receiptAddressDao.delete(receiptAddressBean);
                    finish();
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void updateReceiptAdress() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String otherPhone = etPhoneOther.getText().toString().trim();
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String detailAddress = etDetailAddress.getText().toString().trim();
        String tvLableString = tvLabel.getText().toString();
        int checkId = rgSex.getCheckedRadioButtonId();
        String sex = "";
        if (checkId == R.id.rb_man) {
            sex = "男性";
        } else {
            sex = "女性";
        }
        receiptAddressBean.setName(name);
        receiptAddressBean.setPhone(phone);
        receiptAddressBean.setPhoneOther(otherPhone);
        receiptAddressBean.setReceiptAddress(receiptAddress);
        receiptAddressBean.setDetailAddress(detailAddress);
        receiptAddressBean.setLabel(tvLableString);
        receiptAddressBean.setSex(sex);

        receiptAddressDao.update(receiptAddressBean);
    }

    private void createReceiptAdress() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String otherPhone = etPhoneOther.getText().toString().trim();
        //      String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String detailAddress = etDetailAddress.getText().toString().trim();
        String tvLableString = tvLabel.getText().toString();
        int checkId = rgSex.getCheckedRadioButtonId();
        String sex = "";
        if (checkId == R.id.rb_man) {
            sex = "男性";
        } else {
            sex = "女性";
        }
        receiptAddressBean = new ReceiptAddressBean(MyApp.userId, name, sex, phone, otherPhone, receiptAddress, detailAddress, tvLableString, checkId);
        receiptAddressDao.insert(receiptAddressBean);
    }

    private boolean checkData() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!SMSUtil.isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        receiptAddress = tvReceiptAddress.getText().toString().trim();
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        String address = etDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
        if (checkedRadioButtonId != R.id.rb_man && checkedRadioButtonId != R.id.rb_women) {
            //2个不相等，则说明没有选中任意一个
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }

        String tvLableString = tvLabel.getText().toString();
        if (TextUtils.isEmpty(tvLableString)) {
            Toast.makeText(this, "请输入标签信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择标签");
        builder.setItems(addressLabels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvLabel.setText(addressLabels[i]);
                tvLabel.setBackgroundColor(bgLabels[i]);
            }
        });
        builder.show();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.et_phone:
                String phone = etPhone.getText().toString();
                if (!TextUtils.isEmpty(phone) && b) {
                    ibDeletePhone.setVisibility(View.VISIBLE);

                } else {
                    ibDeletePhone.setVisibility(View.GONE);
                }
                break;
            case R.id.et_phone_other:
                String phoneOther = etPhoneOther.getText().toString();
                if (!TextUtils.isEmpty(phoneOther) && b) {
                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
                } else {
                    ibDeletePhoneOther.setVisibility(View.GONE);
                }
                break;
        }
    }
}
