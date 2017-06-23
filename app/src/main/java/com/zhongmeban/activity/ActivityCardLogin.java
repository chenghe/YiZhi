package com.zhongmeban.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.Login;
import com.zhongmeban.bean.LoginIdentifyingCode;
import com.zhongmeban.bean.postbody.IdentityCodeBody;
import com.zhongmeban.bean.postbody.LoginBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.CountDownTimerUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.VirtualKeyboardView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2016/8/31.
 */
public class ActivityCardLogin extends BaseActivity implements View.OnClickListener {

    private Context mContext = ActivityCardLogin.this;
    private RelativeLayout rlNum;
    private RelativeLayout rlPass;
    private Button btPass;
    private Button btNum;
    private CardView cvNum;//手机号码页面
    private CardView cvPass;//验证码页面
    private EditText edPhone;
    private TextView[] tvList = new TextView[4];      //用数组保存4个TextView，为什么用数组？
    private VirtualKeyboardView virtualKeyboardView;
    private ArrayList<Map<String, String>> valueList;
    private GridView gridView;
    private ImageView ivNumClose;//号码界面取消登录
    private ImageView ivPassClose;//验证码界面取消登录
    private ImageView ivPassBack;//验证码界面返回号码界面
    private ImageView ivClearPhone;//清理手机号码
    private TextView tvNumError;
    private TextView tvPhoneNum;
    private Subscription loginObservable;
    private String identifyingCode;//验证码
    private int step;
    private LoginBody loginBody;
    private boolean result = false;//判断是否成功登录
    private CountDownTimerUtils countDownTimerUtils;

    private int currentIndex = -1;    //用于记录当前输入密码格位置
    private String phone;//手机号码
    private String beforePhone;//前一个手机号，防止同意号码重复登陆


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_card);

        initView();
        valueList = virtualKeyboardView.getValueList();
        countDownTimerUtils = new CountDownTimerUtils(btPass,60000, 1000);

    }

    private void initView() {

        ivNumClose = (ImageView) findViewById(R.id.iv_login_close);
        ivNumClose.setOnClickListener(this);
        ivPassClose = (ImageView) findViewById(R.id.iv_pass_close);
        ivPassClose.setOnClickListener(this);
        ivPassBack = (ImageView) findViewById(R.id.iv_back);
        ivPassBack.setOnClickListener(this);
        tvNumError = (TextView) findViewById(R.id.tv_error_num);
        tvPhoneNum = (TextView) findViewById(R.id.tv_pass_phone);

        ivClearPhone = (ImageView) findViewById(R.id.iv_phone_clear);
        ivClearPhone.setOnClickListener(this);

        rlNum = (RelativeLayout) findViewById(R.id.rl_num);
        rlPass = (RelativeLayout) findViewById(R.id.rl_pass);
        btPass = (Button) findViewById(R.id.bt_pass);
        btPass.setOnClickListener(this);
        btNum = (Button) findViewById(R.id.bt_num);
        btNum.setOnClickListener(this);
        checkNumButtonClickAble(phone);
        cvNum = (CardView) findViewById(R.id.cv_num);
        cvPass = (CardView) findViewById(R.id.cv_pass);
        edPhone = (EditText) findViewById(R.id.ed_num);
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString();
                if (TextUtils.isEmpty(num)){
                    phone = num;
                    ivClearPhone.setVisibility(View.GONE);
                    checkNumButtonClickAble(phone);
                }else {
                    ivClearPhone.setVisibility(View.VISIBLE);
                    Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");//正则表达式确定电话号码格式是否正确
                    Matcher m = p.matcher(num);
                    if (num.length() > 10) {
                        if (m.matches()) {
                            phone = num;
                            tvNumError.setVisibility(View.INVISIBLE);
                            checkNumButtonClickAble(num);
                        } else {
                            tvNumError.setVisibility(View.VISIBLE);
                            checkNumButtonClickAble(num);
                        }

                    } else {
                        tvNumError.setVisibility(View.INVISIBLE);
                        checkNumButtonClickAble(num);
                    }
                }

            }
        });

        tvList[0] = (TextView) findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) findViewById(R.id.tv_pass4);
        tvList[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {

                    identifyingCode = "";
                    for (int i = 0; i < 4; i++) {
                        identifyingCode += tvList[i].getText().toString().trim();
                        Log.i("hcb","hcbtext identifyingCode "+i +identifyingCode);
                    }
                    if (identifyingCode != null && identifyingCode.length() == 4) {
                    loginBody = new LoginBody();
                    loginBody.setCode(identifyingCode);//测试使用，正式时请打开注释
                    loginBody.setPhone(phone);
                    login(identifyingCode);
                    }
                }
            }
        });
        virtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtualKeyboardView);
        gridView = virtualKeyboardView.getGridView();
        gridView.setOnItemClickListener(phoneOnItemClickListener);

        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            edPhone.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edPhone, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_phone_clear:
                edPhone.setText("");
                ivClearPhone.setVisibility(View.GONE);
                break;
            case R.id.bt_num://获取验证码
                if (TextUtils.isEmpty(beforePhone)){
                    countDownTimerUtils.cancel();
                    tvPhoneNum.setText(phone);
                    getIdentifyingCode(phone);
                    countDownTimerUtils.start();
                    animateRevealShow();
                }else if(!TextUtils.isEmpty(beforePhone)&&phone.equals(beforePhone)){
                    //两次手机号码相同
                    tvPhoneNum.setText(phone);
                    getIdentifyingCode(phone);
                    animateRevealShow();
                }else {
                    countDownTimerUtils.cancel();
                    tvPhoneNum.setText(phone);
                    getIdentifyingCode(phone);
                    countDownTimerUtils.start();
                    animateRevealShow();
                }


                break;
            case R.id.bt_pass://重新获取验证码
                countDownTimerUtils.cancel();
//                animateRevealClose();
                getIdentifyingCode(phone);
                countDownTimerUtils.start();
                break;
            case R.id.iv_login_close://取消登录
                finish();
                break;
            case R.id.iv_back://返回输入手机号码
                animateRevealClose();
                break;
            case R.id.iv_pass_close:
                finish();
                break;
        }
    }

    public void animateRevealShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils
                    .createCircularReveal(cvPass, 0, 0, 0, cvPass.getWidth());
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    cvPass.setVisibility(View.VISIBLE);
                    edPhone.setVisibility(View.GONE);
                    btNum.setEnabled(false);
//                    cvNum.setVisibility(View.GONE);
                    super.onAnimationStart(animation);
                    gridView.setOnItemClickListener(passWordOnItemClickListener);
                }
            });
            mAnimator.start();
        } else {
            cvPass.setVisibility(View.VISIBLE);
            cvNum.setVisibility(View.GONE);
//            btNum.setVisibility(View.GONE);
//            edPhone.setVisibility(View.GONE);
            gridView.setOnItemClickListener(passWordOnItemClickListener);
        }
    }

    public void animateRevealClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils
                    .createCircularReveal(cvPass, 0, 0, cvPass.getWidth(), 0);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cvPass.setVisibility(View.INVISIBLE);
                    edPhone.setVisibility(View.VISIBLE);
                    btNum.setEnabled(true);
                    super.onAnimationEnd(animation);
                    gridView.setOnItemClickListener(phoneOnItemClickListener);
//                RegisterActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        } else {
            cvPass.setVisibility(View.GONE);
            gridView.setOnItemClickListener(phoneOnItemClickListener);
            cvNum.setVisibility(View.VISIBLE);
//            btNum.setVisibility(View.VISIBLE);
        }
    }

    AdapterView.OnItemClickListener phoneOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            if (position < 11 && position != 9) {    //点击0~9按钮

                String amount = edPhone.getText().toString().trim();
                amount = amount + valueList.get(position).get("name");

                edPhone.setText(amount);

                Editable ea = edPhone.getText();
                edPhone.setSelection(ea.length());
            } else {

                if (position == 9) {      //点击退格键
                    String amount = edPhone.getText().toString().trim();
                    if (!amount.contains(".")) {
                        amount = amount + valueList.get(position).get("name");
                        edPhone.setText(amount);

                        Editable ea = edPhone.getText();
                        edPhone.setSelection(ea.length());
                    }
                }

                if (position == 11) {      //点击退格键
                    String amount = edPhone.getText().toString().trim();
                    if (amount.length() > 0) {
                        amount = amount.substring(0, amount.length() - 1);
                        edPhone.setText(amount);

                        Editable ea = edPhone.getText();
                        edPhone.setSelection(ea.length());
                    }
                }
            }
        }
    };


    AdapterView.OnItemClickListener passWordOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < 11 && position != 9) {    //点击0~9按钮
                if (currentIndex >= -1 && currentIndex < 3) {      //判断输入位置————要小心数组越界

                    ++currentIndex;
                    Log.i("hcbtest", "valueList.get(position).get(\"name\")" + valueList.get(position).get("name"));
                    tvList[currentIndex].setText(valueList.get(position).get("name") + "");

                }
            } else {
                if (position == 11) {      //点击退格键
                    if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界
                        tvList[currentIndex].setText("");
                        currentIndex--;
                    }
                }
            }

        }
    };


    @Override
    protected void initTitle() {

    }

    /**
     * 登录
     */
    private void login(String code) {
        loginObservable = HttpService.getHttpService().login(loginBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login>() {
                    @Override
                    public void onCompleted() {
                        if (result) {
                            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("phone", phone);
                            editor.commit();


                            Intent intent = new Intent(ActivityCardLogin.this, ActHome.class);
                            setResult(RESULT_OK, intent);

                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "login onError");
                        Log.i("hcb", "e is" + e);
                    }

                    @Override
                    public void onNext(Login login) {
                        result = login.isResult();
                        if (result){
                            String userId = login.getData().getUserId();
                            String token = login.getData().getToken();
                            String userAvatar = login.getData().getUser().getAvatar();
                            String nickname = login.getData().getUser().getNickname();

                            Log.i("hcb", "userId is" + userId);
                            Log.i("hcb", "token is " + token);
                            Log.i("hcb", "userAvatar is " + userAvatar);
                            Log.i("hcb", "nickname is " + nickname);

                            SharedPreferences sp = getSharedPreferences(SPInfo.SPUserInfo, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(SPInfo.UserKey_userId, userId);
                            editor.putString(SPInfo.UserKey_token, token);
                            editor.putString(SPInfo.UserKey_userAvatar, userAvatar);
                            editor.putString(SPInfo.UserKey_nickname, nickname);
                            editor.commit();
                        }else {
                            ToastUtil.showText(mContext,"验证码输入错误");
                            tvList[0].setText("");
                            tvList[1].setText("");
                            tvList[2].setText("");
                            tvList[3].setText("");
                            currentIndex = -1;
                        }


                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getIdentifyingCode(String phoneNum){
        IdentityCodeBody body = new IdentityCodeBody(phoneNum);
        HttpService.getHttpService().getCode(body)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginIdentifyingCode>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getIdentifyingCode onCompleted");
                        beforePhone = phone;

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getIdentifyingCode onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(LoginIdentifyingCode loginIdentifyingCode) {
                        Log.i("hcb", "getIdentifyingCode onNext");

                    }
                });
    }

    private void checkNumButtonClickAble(String test) {
        if (phone != null && test.length() == 11) {
            btNum.setEnabled(true);
            btNum.setTextColor(getResources().getColor(R.color.white));
        } else {
            btNum.setEnabled(false);
            btNum.setTextColor(getResources().getColor(R.color.content_two));
        }
    }
}
