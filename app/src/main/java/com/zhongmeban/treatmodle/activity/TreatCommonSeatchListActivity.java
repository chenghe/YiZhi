package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.treatmodle.fragment.TreatDoctSearchFragment;
import com.zhongmeban.treatmodle.fragment.TreatHospitalSearchFragment;
import com.zhongmeban.treatmodle.fragment.TreatMedicineSearchFragment;
import com.zhongmeban.treatmodle.presenter.TreatSearchDoctListPresenter;
import com.zhongmeban.treatmodle.presenter.TreatSearchHospListPresenter;
import com.zhongmeban.utils.ToastUtil;

/**
 * 功能描述： 搜索Activity
 * 作者：ysf on 2016/12/2 10:52
 */
public class TreatCommonSeatchListActivity extends BaseActivityRefresh
    implements View.OnClickListener {

    public static final String EXTRA_SEARCH_TYPE = "extra_search_type_bean";

    public static final int SEARCH_MEDICINE_TYPE = 9;
    public static final int SEARCH_HOSPITAL_TYPE = 7;
    public static final int SEARCH_DOCTOR_TYPE = 8;
    public static final int SEARCH_TYPE = SEARCH_MEDICINE_TYPE;

    private static final int REQUEST_COUNT = 20;

    private int fromActivityType;// 来自哪个界面启动
    private int searchType;

    private EditText etSearch;
    private TextView tvClose;
    private ImageView ivDelete;
    private String mSearchName = "";

    private TreatMedicineSearchFragment medicineSearchFragment;
    private TreatHospitalSearchFragment treatHospitalSearchFragment;
    private TreatDoctSearchFragment treatDoctSearchFragment;
    private ISearchListener searchListener;


    @Override protected int getLayoutIdRefresh() {
        return R.layout.act_treat_common_search_list;
    }


    @Override protected void initToolbarRefresh() {
        //搜索不需要标题
    }


    @Override protected void initViewRefresh() {
        searchType = getIntent().getIntExtra(EXTRA_SEARCH_TYPE, 0);
        fromActivityType = getIntent().getIntExtra(
            TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY, 0);
        etSearch = (EditText) findViewById(R.id.et_search_common);
        tvClose = (TextView) findViewById(R.id.tv_search_cancle_common);
        ivDelete = (ImageView) findViewById(R.id.iv_search_clear_common);

        tvClose.setOnClickListener(this);
        ivDelete.setOnClickListener(this);

        etSearch.addTextChangedListener(textWatcher);
        showFragment(searchType);
    }


    public interface ISearchListener {
        void searchChangeListener(String text);
    }


    public void setSearchListener(ISearchListener listener) {
        this.searchListener = listener;
    }


    public void showFragment(int type) {

        if (type == 0) {
            ToastUtil.showText(this, "加载类型错误");
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //获取治疗的种类 1 手术 ，2 化疗，3 放疗,4 靶向治疗
        switch (type) {

            case SEARCH_MEDICINE_TYPE:
                fragmentTransaction.replace(R.id.fmyt_common_search_container,
                    medicineSearchFragment = TreatMedicineSearchFragment
                        .newInstance(fromActivityType));
                break;
            case SEARCH_HOSPITAL_TYPE:
                //医院搜索
                treatHospitalSearchFragment = TreatHospitalSearchFragment.newInstance();
                fragmentTransaction.replace(R.id.fmyt_common_search_container,
                    treatHospitalSearchFragment);

                new TreatSearchHospListPresenter(this, treatHospitalSearchFragment);
                break;
            case SEARCH_DOCTOR_TYPE:
                //医生搜索
                treatDoctSearchFragment = TreatDoctSearchFragment.newInstance();
                fragmentTransaction.replace(R.id.fmyt_common_search_container,
                    treatDoctSearchFragment);

                new TreatSearchDoctListPresenter(this, treatDoctSearchFragment);
                break;
        }
        fragmentTransaction.commit();
    }


    /**
     * 搜索框，搜素内容监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }


        @Override
        public void afterTextChanged(Editable s) {
            mSearchName = s.toString();
            if (TextUtils.isEmpty(mSearchName)) {
                ivDelete.setVisibility(View.GONE);
            } else {
                ivDelete.setVisibility(View.VISIBLE);
            }
            if (searchListener != null && !TextUtils.isEmpty(mSearchName)) {
                searchListener.searchChangeListener(mSearchName);
            }
        }
    };


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search_common:
                break;
            case R.id.iv_search_clear_common:

                etSearch.setText("");
                mSearchName = "";
                ivDelete.setVisibility(View.GONE);
                break;
            case R.id.tv_search_cancle_common:
                hideKeyBoard();
                onBackPressed();
                break;
        }
    }


    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        if (imm.isActive(etSearch)) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
