package com.zhongmeban.treatmodle.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.disk.CacheUtil;
import com.zhongmeban.utils.genericity.CacheKey;
import com.zhongmeban.view.DividerItemDecoration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 功能描述： 药品分类列博
 * 作者：ysf on 2016/12/2 10:52
 */
public class TreatMedicineTypeListActivity extends BaseActivityRefresh {

    public static final String EXTRA_FROM_ACTIVITY = "extra_from_activity";// 是否来自编辑界面，如果不是就正常展示药品详情

    private RecyclerView rvList;
    private List<MedicineTypeBean> mDatas = new ArrayList<>();
    private CommonAdapter adapter;
    private int fromActivityType;// 来自哪个界面启动


    @Override protected int getLayoutIdRefresh() {
        return R.layout.act_treat_medicine_list;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("药品分类", "");
        mToolbar.setActionName(R.drawable.home_search)
            .setActionSecond(R.drawable.medicine_brand);

        for (int i = 0; i < 10; i++) {
            //mDatas.add(new MedicineTypeBean(i, "化学药品"));
        }
    }


    @Override protected void initViewRefresh() {

        initEmptyView();
        mRefreshLayout.setEnabled(false);
        fromActivityType = getIntent().getIntExtra(EXTRA_FROM_ACTIVITY, 0);

        rvList = (RecyclerView) findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        rvList.setAdapter(adapter =
            new CommonAdapter<MedicineTypeBean>(this, R.layout.item_textview_only, mDatas) {

                @Override
                protected void convert(final ViewHolder holder, final MedicineTypeBean medicineTypeBean, int position) {
                    holder.setText(R.id.tv, medicineTypeBean.getName());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(TreatMedicineTypeListActivity.this,
                                TreatMedicineListActivity.class);
                            intent.putExtra(TreatMedicineListActivity.EXTRA_MEDICINE_TYPE_BEAN,
                                medicineTypeBean);
                            intent.putExtra(EXTRA_FROM_ACTIVITY, fromActivityType);//用来表示 哪个类启动的药品列表
                            startActivity(intent);
                        }
                    });
                }
            });

        getData();
    }


    @Override public void clickRightSecond() {
        startActivity(new Intent(this, TreatMedicineBrandListActivity.class));
    }


    private void getData() {
        HttpService.getHttpService().getMedicineTypeList()
            .compose(RxUtil.<HttpResult<List<MedicineTypeBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<MedicineTypeBean>>>() {
                           @Override public void onCompleted() {
                               hideSwipeRefresh();
                           }


                           @Override public void onError(Throwable e) {
                               showOnError(e, false);
                           }


                           @Override
                           public void onNext(HttpResult<List<MedicineTypeBean>> httpResult) {
                               showOnNextLayout(httpResult);
                               Logger.v(new Exception("500") + "--onNext==" + httpResult.toString());

                               //mDatas.clear();
                               mDatas.addAll(httpResult.getData());
                               adapter.notifyDataSetChanged();
                               CacheUtil.getInstance()
                                   .getCacheHelper()
                                   .put(CacheKey.MEDICINE_TYPE, (Serializable) mDatas);
                               showEmptyLayoutState(LOADING_STATE_SUCESS);
                           }
                       }

            );
    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        getData();
    }


    @Override public void clickRight() {
        super.clickRight();
        Intent intent = new Intent(this, TreatCommonSeatchListActivity.class);
        intent.putExtra(TreatCommonSeatchListActivity.EXTRA_SEARCH_TYPE,
            TreatCommonSeatchListActivity.SEARCH_MEDICINE_TYPE);
        startActivity(intent);
    }


    @Override public void onRefresh() {
        super.onRefresh();
        mDatas.clear();
        getData();
    }
}
