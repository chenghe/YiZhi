package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.adapter.AttentionTreatByDisAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.TreatMethodLists;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关注获取某种癌症的某种治疗方法分类的所有治疗方法
 * Created by Chengbin He on 2016/8/10.
 */
public class ActivityAttentionTherapeuticByDisId extends BaseActivity {


    private Activity mContext = ActivityAttentionTherapeuticByDisId.this;
    private ListView listView;
    private AttentionTreatByDisAdapter mAdapter;
    private Subscription subscription;
    private String name = "";
    private ImageView ivCancel;//取消筛选
    private EditText searchBox;//搜索框
    private ImageView clearBtn;//清空搜索
    private int diseaseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_attention_choose_operation);

        Intent intent = getIntent();
        diseaseId = intent.getIntExtra("diseaseId", 0);
//        diseaseId =1;
        initTitle();
        initView();
        getHttpData();


    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);

        //搜索部分
        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(onClickListener);
        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(textWatcher);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_search_clear://清除搜索内容
                    searchBox.setText("");
                    clearBtn.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void initTitle() {

        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择手术");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            name = s.toString();
            if (TextUtils.isEmpty(name)) {
                clearBtn.setVisibility(View.GONE);
            } else {
                clearBtn.setVisibility(View.VISIBLE);
            }

            getHttpData();
        }
    };

    /**
     * 获取网络数据
     */
    private void getHttpData() {
        subscription = HttpService.getHttpService().getTherapeuticByDisId(diseaseId + "", "1", name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreatMethodLists>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "ActivityAttentionTherapeuticByDisId onCompleted()");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "ActivityAttentionTherapeuticByDisId onError()");
                        Log.i("hcb", "ActivityAttentionTherapeuticByDisId e is" + e);
                    }

                    @Override
                    public void onNext(final TreatMethodLists treatMethodLists) {
                        Log.i("hcb", "ActivityAttentionTherapeuticByDisId onNext()");
                        if (treatMethodLists.getData() != null && treatMethodLists.getData().size() > 0) {
                            mAdapter = new AttentionTreatByDisAdapter(mContext, treatMethodLists.getData());
                            listView.setAdapter(mAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String therapeuticName = treatMethodLists.getData().get(position).getTherapeuticName();
                                    int therapeuticId = Integer.parseInt(treatMethodLists.getData().get(position).getTherapeuticId());
                                    Intent intent = new Intent(mContext,
                                            ActivityAttentionAddOperation.class);
                                    intent.putExtra("therapeuticName", therapeuticName);
                                    intent.putExtra("therapeuticId", therapeuticId);
                                    mContext.setResult(200, intent);
                                    finish();


                                }
                            });
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
