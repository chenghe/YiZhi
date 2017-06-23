package com.zhongmeban.treatmodle.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.DieaseTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.view.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 功能描述： 药品列博
 * 0  * 作者：ysf on 2016/12/2 10:52
 */
public class TreatCancerTypeListActivity extends BaseActivityRefresh {

    private RecyclerView rvList;
    private List<DieaseTypeBean> mDatas = new ArrayList<>();
    private CommonAdapter adapter;


    @Override protected int getLayoutIdRefresh() {
        return R.layout.act_treat_cancer_type_list;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("选择癌症种类", "");
        for (int i = 0; i < 10; i++) {
            //mDatas.add(new DieaseTypeBean(i,"癌症分类"));
        }
    }


    @Override protected void initViewRefresh() {

        initEmptyView();
        rvList = (RecyclerView) findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        rvList.setAdapter(adapter =
            new CommonAdapter<DieaseTypeBean>(this, R.layout.item_textview_only, mDatas) {

                @Override
                protected void convert(ViewHolder holder, final DieaseTypeBean trumerSource, int position) {
                    holder.setText(R.id.tv, trumerSource.getName());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(TreatCancerTypeListActivity.this,
                                TreatTreatMethodActivity.class);
                            intent.putExtra(TreatTreatMethodActivity.EXTRA_CANCER_TYPE_BEAN,
                                trumerSource);
                            if (trumerSource != null) {
                                startActivity(intent);
                            }
                        }
                    });
                }
            });

        getData();
    }


    private void getData() {

        HttpService.getHttpService().getDiseaseList()
            .compose(RxUtil.<HttpResult<List<DieaseTypeBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<DieaseTypeBean>>>() {
                @Override public void onCompleted() {
                    hideSwipeRefresh();
                }


                @Override public void onError(Throwable e) {
                    hideSwipeRefresh();
                    showOnError(e, false);
                }


                @Override public void onNext(HttpResult<List<DieaseTypeBean>> listHttpResult) {
                    showOnNextLayout(listHttpResult);

                    mDatas.clear();
                    mDatas = listHttpResult.getData();
                    adapter.notifyData(mDatas);

                    showEmptyLayoutState(
                        mDatas.size() == 0 ? LOADING_STATE_EMPTY : LOADING_STATE_SUCESS);
                }
            });
    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
            showEmptyLayoutState(LOADING_STATE_PEOGRESS);
            getData();
    }
}
