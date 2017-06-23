package com.zhongmeban.shopmodle.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.address.AddressBean;
import com.zhongmeban.address.BottomDialog;
import com.zhongmeban.address.OnAddressSelectedListener;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.AddressListBean;
import com.zhongmeban.bean.shop.post.CreatAddressBody;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.StringUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.ToolbarView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by User on 2016/10/9.
 */

public class ActAddAddress extends BaseActivityToolBar
    implements View.OnClickListener, OnAddressSelectedListener {

    public static final String EXTRA_IS_UPDATE_ADDRESS = "usUpdateAddress";
    public static final String EXTRA_OLD_ADDRESS = "oldAddress";

    private ToolbarView mToolbar;
    private SmoothCheckBox mCheckBox;
    private LinearLayout mLayout;//默认地址点击事件
    private EditText mEditName;
    private EditText mEditPhone;
    private EditText mEditAddressDetail;
    private TextView mTvCity;
    private TextView mTvDefault;
    private BottomDialog mDialogAddress;

    private CreatAddressBody creatAddressBody = new CreatAddressBody();

    private boolean isUpdate;


    @Override protected int getLayoutId() {
        return R.layout.activity_act_address_add;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("填写地址", "保存");
    }


    @Override
    protected void initView() {

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra(EXTRA_IS_UPDATE_ADDRESS, false);

        mCheckBox = (SmoothCheckBox) findViewById(R.id.id_act_address_add_cb_default);
        mLayout = (LinearLayout) findViewById(R.id.id_act_address_add_layout_default);
        mEditName = (EditText) findViewById(R.id.id_act_address_add_edit_name);
        mEditPhone = (EditText) findViewById(R.id.id_act_address_add_edit_phone);
        mEditAddressDetail = (EditText) findViewById(R.id.id_act_address_add_tv_address_detail);
        mTvCity = (TextView) findViewById(R.id.id_act_address_add_tv_city);
        mTvDefault = (TextView) findViewById(R.id.id_act_address_add_tv_default);
        if (isUpdate) {
            AddressListBean bean = (AddressListBean) intent.getSerializableExtra(EXTRA_OLD_ADDRESS);
            creatAddressBody = new CreatAddressBody(bean);

            mEditName.setText(creatAddressBody.consignee);
            mEditPhone.setText(creatAddressBody.phone);
            mEditAddressDetail.setText(creatAddressBody.address);
            mTvCity.setText(bean.getAreaName());
            mCheckBox.setChecked(creatAddressBody.isDefault, true);
            areaId= creatAddressBody.areaId;
        }

        mLayout.setOnClickListener(this);
        mTvCity.setOnClickListener(this);

        mCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                mTvDefault.setTextColor(isChecked
                                        ? getResources().getColor(R.color.app_green)
                                        : getResources().getColor(R.color.content_two));
            }
        });
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            //点击设为默认地址
            case R.id.id_act_address_add_layout_default:
                mCheckBox.performClick();

                break;
            // 选择城市信息点击事件
            case R.id.id_act_address_add_tv_city:
                showAddressSelect();
                break;

        }
    }

    //保存地址按钮
    public Long id;

    public String consignee;//收貨人

    public String address;

    public String zipCode;

    public String phone;

    public Long areaId;

    public String memberId;

    public boolean isDefault;


    @Override public void clickRight() {

        checkoutAddressInfo();

        creatAddressBody.memberId = 1L;
        creatAddressBody.areaId = areaId;

        if (isUpdate){
            updateAddress(creatAddressBody);
        } else{
            creatAddress(creatAddressBody);
        }
    }


    private void checkoutAddressInfo() {
        if (TextUtils.isEmpty(mEditName.getText())) {
            ToastUtil.showText(this, "姓名不可为空");
            return;
        } else {
            creatAddressBody.consignee = mEditName.getText().toString();
        }

        if (TextUtils.isEmpty(mEditPhone.getText())) {
            ToastUtil.showText(this, "电话不可为空");
            return;
        } else {
            if (!StringUtils.isMobileNO(mEditPhone.getText().toString())) {
                ToastUtil.showText(this, "电话格式不对");
                return;
            }
            creatAddressBody.phone = mEditPhone.getText().toString();
        }

        String addressDetail = mEditAddressDetail.getText().toString();
        if (TextUtils.isEmpty(addressDetail) && addressDetail.length() < 4) {
            ToastUtil.showText(this, "地址不可为空");
            return;
        } else {
            creatAddressBody.address = addressDetail;
        }
        creatAddressBody.isDefault = mCheckBox.isChecked();
        if (areaId!=null&&areaId < 1) {
            return;
        }
    }


    /**
     * 显示地址列表选择项的对框框
     */
    private void showAddressSelect() {
        mDialogAddress = new BottomDialog(ActAddAddress.this);
        mDialogAddress.setOnAddressSelectedListener(ActAddAddress.this);
        mDialogAddress.show();
    }


    @Override
    public void onAddressSelected(AddressBean province, AddressBean city, AddressBean county, AddressBean street) {
        String s =
            (province == null ? "" : province.name) +
                (city == null ? "" : city.name) +
                (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        mTvCity.setText(s);

        Observable.just(province, city, county, street).filter(new Func1<AddressBean, Boolean>() {
            @Override public Boolean call(AddressBean addressBean) {
                return addressBean != null;
            }
        }).last().subscribe(new Action1<AddressBean>() {
            @Override public void call(AddressBean addressBean) {
                areaId = Long.valueOf(addressBean.getId());
                Logger.d("城市--" + addressBean.getName());
            }
        });

        mDialogAddress.dismiss();
    }


    private void creatAddress(CreatAddressBody creatAddressBody) {
        HttpShopMethods.getInstance().creatAddress(creatAddressBody, new Subscriber<HttpResult>() {
            @Override public void onCompleted() {
            }
            @Override public void onError(Throwable e) {
                Logger.e("Throwable==" + e.toString());
            }
            @Override public void onNext(HttpResult httpResult) {
                Logger.i("reult==" + httpResult);
                if (httpResult.errorCode == 0) {
                    finishAndSetResult("保存成功");
                }
            }
        });
    }


    private void updateAddress(final CreatAddressBody body) {

        HttpShopMethods.getInstance().updateAddress(body, new Subscriber<HttpResult>() {
            @Override public void onCompleted() {
            }
            @Override public void onError(Throwable e) {
                ToastUtil.showText(ActAddAddress.this, "操作失败");
            }

            @Override public void onNext(HttpResult httpResult) {
                if (httpResult.errorCode == 0) {
                    finishAndSetResult("修改成功");
                }
            }
        });
    }


    private void finishAndSetResult(String tip) {

        ToastUtil.showText(ActAddAddress.this, tip);
        setResult(RESULT_OK);
        finish();
    }
}
