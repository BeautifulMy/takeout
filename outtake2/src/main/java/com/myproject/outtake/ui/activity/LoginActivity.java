package com.myproject.outtake.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myproject.outtake.R;
import com.myproject.outtake.presenter.LoginPresenter;
import com.myproject.outtake.utils.SMSUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends FragmentActivity {

    private static final int GET_CODE_SUCCESS = 100;
    private static final int GET_CODE_FAIL = 101;
    private static final int KEEP_TIME_MINS = 102;//保持时间递减的状态码
    private static final int RESET_TIME = 103;//重置时间为60秒

    private static final int SUBMIT_CODE_SUCCES = 104;//校验验证码成功
    private static final int SUBMIT_CODE_FAIL = 105;//校验验证码失败

    @Bind(R.id.iv_user_back)
    ImageView ivUserBack;
    @Bind(R.id.iv_user_password_login)
    TextView ivUserPasswordLogin;
    @Bind(R.id.et_user_phone)
    EditText etUserPhone;
    @Bind(R.id.tv_user_code)
    TextView tvUserCode;
    @Bind(R.id.et_user_psd)
    EditText etUserPsd;
    @Bind(R.id.et_user_code)
    EditText etUserCode;
    @Bind(R.id.login)
    TextView login;
    private int time = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_SUCCESS:
                    Toast.makeText(LoginActivity.this,"获取验证码成功",Toast.LENGTH_SHORT).show();
                    break;
                case GET_CODE_FAIL:
                    Toast.makeText(LoginActivity.this,"获取验证码失败,",Toast.LENGTH_SHORT).show();
                    break;
                case SUBMIT_CODE_SUCCES:
                    Toast.makeText(LoginActivity.this,"验证码校验成功",Toast.LENGTH_SHORT).show();
                    break;
                case SUBMIT_CODE_FAIL:
                    Toast.makeText(LoginActivity.this,"验证码校验失败",Toast.LENGTH_SHORT).show();
                    break;
                case KEEP_TIME_MINS:
                    tvUserCode.setText("("+time--+")秒后重新发送");
                    break;
                case RESET_TIME:
                    time=60;
                    break;

            }
        }
    };
    EventHandler eventHandler = new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object o) {
            if (result == SMSSDK.RESULT_COMPLETE){
                //成功
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //下发验证码短信成功后,才可以做验证码短信+手机号码校验过程
                    handler.sendEmptyMessage(GET_CODE_SUCCESS);
                }
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //校验验证码成功
                    handler.sendEmptyMessage(SUBMIT_CODE_SUCCES);
                }
            }else{
                //失败
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //下发验证码短信成功后,才可以做验证码短信+手机号码校验过程
                    handler.sendEmptyMessage(GET_CODE_FAIL);
                }
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //校验验证码失败
                    handler.sendEmptyMessage(SUBMIT_CODE_FAIL);
                }
            }
            //做某一个事件结果的监听

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick({R.id.tv_user_code, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_user_code:
                sendCode();
                break;
            case R.id.login:
                checkLogin();
                break;
        }
    }

    private void checkLogin() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        String code = etUserCode.getText().toString().trim();
        if (SMSUtil.isMobileNO(phone)&&!TextUtils.isEmpty(psd)&&!TextUtils.isEmpty(code)){
            login();
        }
    }

    private void login() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        String code = etUserCode.getText().toString().trim();
        if(SMSUtil.isMobileNO(phone) && !TextUtils.isEmpty(psd) && !TextUtils.isEmpty(code)){
            LoginPresenter loginPresenter = new LoginPresenter(this);
            loginPresenter.getLoginData(phone,psd,phone,2);
            finish();
        }


    }

    private void sendCode() {
        String phone = etUserPhone.getText().toString().trim();
        if (SMSUtil.isMobileNO(phone)) {
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
            new Thread() {
                @Override
                public void run() {
                    while (time > 0) {
                        try {
                            Thread.sleep(999);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(KEEP_TIME_MINS);
                    }
                    handler.sendEmptyMessage(RESET_TIME);
                }
            }.start();
        }

    }
}
