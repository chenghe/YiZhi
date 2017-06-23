package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityTherapeuticDetail;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.TreatMethodCommonBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 手术中手术项目选择
 * Created by Chengbin He on 2017/1/4.
 */

public class AttentionSurgeryListActivity extends BaseActivityToolBar{

    public static final int SurgeryListOK = 300;
    public static final String SurgeryName = "surgeryName";
    public static final String SurgeryId = "surgeryId";

    private Activity mContext = AttentionSurgeryListActivity.this;
    private int cancerId;
    private RecyclerView rvList;
    private CommonAdapter adapter;
    private List<TreatMethodCommonBean> mDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_surgery_list;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("请选择手术项目","");
    }

    @Override
    protected void initView() {
        initEmptyView();
        SharedPreferences sp = getSharedPreferences(SPInfo.SPUserInfo,1);
        cancerId = sp.getInt(SPInfo.UserKey_patientDiseaseId,0);

        rvList = (RecyclerView) findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.addItemDecoration(
                new DividerItemDecoration(mContext ,DividerItemDecoration.VERTICAL_LIST));

        rvList.setAdapter(adapter =
                new CommonAdapter<TreatMethodCommonBean>(mContext, R.layout.item_textview_only,
                        mDatas) {

                    @Override
                    protected void convert(ViewHolder holder, final TreatMethodCommonBean medicineTypeBean, int position) {
                        holder.setText(R.id.tv, medicineTypeBean.getName());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra(SurgeryName,medicineTypeBean.getName());
                                intent.putExtra(SurgeryId,medicineTypeBean.getId());
                                setResult(SurgeryListOK,intent);
                                finish();
                            }
                        });
                    }
                });

        getData();
    }

    private void getData() {

        HttpService.getHttpService().getSurgeryList(cancerId)
                .compose(RxUtil.<HttpResult<List<TreatMethodCommonBean>>>normalSchedulers())
                .subscribe(new Subscriber<HttpResult<List<TreatMethodCommonBean>>>() {
                    @Override public void onCompleted() {

                    }


                    @Override public void onError(Throwable e) {
                        showEmptyLayoutState(LOADING_STATE_NO_NET);
                    }


                    @Override
                    public void onNext(HttpResult<List<TreatMethodCommonBean>> listHttpResult) {
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

