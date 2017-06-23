package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongmeban.R;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.Login;
import com.zhongmeban.bean.LoginIdentifyingCode;
import com.zhongmeban.bean.PatientList;
import com.zhongmeban.bean.postbody.CreatePatientBody;
import com.zhongmeban.bean.postbody.LoginBody;
import com.zhongmeban.net.HttpService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * 登录注册界面
 */
public class ActLoginOrRegister extends Activity implements OnClickListener {

    private Context mContext = ActLoginOrRegister.this;
    private Button btPhone;
    private Button btSkip;
    private EditText etPhone;
    private EditText etPassWord;
    private TextView tvLoginTitle;
    private TextView tvNum;
    private Subscription codeObservable;
    private Subscription loginObservable;
    private String phone ="";//手机号
    private String identifyingCode;//验证码
    private String userId ="2";
    private String token  = "22ffca44-0675-4e0e-8e71-2e4722bc53b3";
    private LoginBody loginBody;
    private CreatePatientBody createPatientBody;
    private boolean result = false;//判断是否成功登录


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_regist);
        SharedPreferences sp = getSharedPreferences("serverTime",MODE_PRIVATE);

        createPatientBody = new CreatePatientBody();
        createPatientBody.setRelation(1);
        createPatientBody.setRelationName("自己");
        createPatientBody.setDiseaseId(2);
        createPatientBody.setDiseaseName("肺癌");
        createPatientBody.setGender(1);
//        createPatientBody.setBirthday(20160501000000000);
//        createPatientBody.setComfirmTime(20160501000000000);
        initView();
    }

    private void initView() {
        btPhone = (Button) findViewById(R.id.bt_getnum);
        btPhone.setOnClickListener(this);
        btSkip = (Button) findViewById(R.id.bt_skip);
        btSkip.setOnClickListener(this);
        tvLoginTitle = (TextView) findViewById(R.id.tv_login_cn);
        tvNum = (TextView) findViewById(R.id.tv_num);
        etPhone = (EditText) findViewById(R.id.et_phone_num);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               phone = s.toString();
            }
        });
        etPassWord = (EditText) findViewById(R.id.et_pass_num);
        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                identifyingCode = s.toString();
                if (identifyingCode!=null&&identifyingCode.length()==6){
                    Toast.makeText(mContext,"正在验证请等待",Toast.LENGTH_SHORT).show();
                    loginBody = new LoginBody();
                    loginBody.setCode(identifyingCode);
                    loginBody.setPhone(phone);

//                    SharedPreferences sp = getSharedPreferences("phone",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("phone", phone);
//                    editor.commit();
//                    Intent intent = new Intent(mContext,ActHome.class);
//                    startActivity(intent);
//                    finish();

                    login(identifyingCode);
//                    createPatientBody.setUserId(userId);
//                    createPatient();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_getnum:
                Pattern p = Pattern.compile("^1[3|4|5|8]\\d{9}$");//正则表达式确定电话号码格式是否正确
                Matcher m = p.matcher(phone);
                if (phone.length()>10&&m.matches()){
                    btPhone.setVisibility(View.GONE);
                    etPhone.setVisibility(View.GONE);
                    etPassWord.setVisibility(View.VISIBLE);
                    tvNum.setVisibility(View.GONE);
                    tvLoginTitle.setText("输入验证码");
                }else {
                    Toast.makeText(mContext,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

//    /**
//     * 获取验证码
//     */
//    private void getIdentifyingCode(){
//        codeObservable = HttpService.getHttpService().getCode(phone)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribeCache(new Subscriber<LoginIdentifyingCode>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(LoginIdentifyingCode loginIdentifyingCode) {
//
//                    }
//                });
//    }


    /**
     * 登录
     */
    private void login(String code){
        loginObservable = HttpService.getHttpService().login(loginBody)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Login>() {
                    @Override
                    public void onCompleted() {
                        if (result){
                            SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("phone", phone);
                            editor.commit();

                            createPatient();

//                            Intent intent = new Intent(mContext,ActHome.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","login onError");
                        Log.i("hcb","e is" + e);
                    }

                    @Override
                    public void onNext(Login login) {
                        result =login.isResult();
                        userId = login.getData().getUserId();
                        token = login.getData().getToken();
                        createPatientBody.setUserId(userId);
                        Log.i("hcb","userId is" + userId);
                        Log.i("hcb","token is " + token);
                        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userId", userId);
                        editor.putString("token", token);
                        editor.commit();

                    }
                });
    }

    /**
     * 获取 patientId（仅开发阶段使用，正式时弃用）
     */
    private void getPatientId(String userId){
        HttpService.getHttpService().getPatientList("0",userId,token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<PatientList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","getPatientId onCompleted()");
                        Intent intent = new Intent(mContext,ActHome.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","getPatientId onError()");
                        Log.i("hcb","e is" +e);
                    }

                    @Override
                    public void onNext(PatientList patientList) {
                        Log.i("hcb","getPatientId onNext()");
                        List<PatientList.Source> source = patientList.getData().getSource();
                        String patientId = source.get(0).getPatientId();
                        Log.i("hcb","patientId is" + patientId);
                        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("patientId", patientId);
                        editor.commit();
                    }
    });
    }

   private void createPatient(){
       HttpService.getHttpService().postCreatePatient(createPatientBody,token)
               .subscribeOn(Schedulers.io())
               .observeOn(Schedulers.io())
               .subscribe(new Subscriber<CreateOrDeleteBean>() {
                   @Override
                   public void onCompleted() {
                       Log.i("hcb","createPatient onCompleted()");
                       getPatientId(userId);
                   }

                   @Override
                   public void onError(Throwable e) {
                       Log.i("hcb","createPatient onError");
                       Log.i("hcb","e is" + e);

                   }

                   @Override
                   public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                       Log.i("hcb"," createPatient onNext");
                        boolean result = createOrDeleteBean.getResult();
                        Log.i("hcb","result is" + result);
                   }
               });
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (codeObservable!=null){
            codeObservable.unsubscribe();
        }
        if (loginObservable != null){
            loginObservable.unsubscribe();
        }
    }
}
