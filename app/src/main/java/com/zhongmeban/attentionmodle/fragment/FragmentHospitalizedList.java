package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.Interface.RecyclerViewAnimListener;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.AttentionHospitalizedDetailActivity;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionAddHospitalizedActivity;
import com.zhongmeban.attentionmodle.adapter.AttentionHospListAdapter;
import com.zhongmeban.attentionmodle.contract.AttentionHospitalizedListContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddHospitalizedPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionHospitalizedDetailPresenter;
import com.zhongmeban.base.BaseFragmentRefresh;

import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;

/**
 * 住院记录List
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentHospitalizedList extends BaseFragmentRefresh implements AttentionHospitalizedListContract.View{


    private AttentionHospitalizedListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private AttentionHospListAdapter mAdapter;
    private FloatingActionButton floatingActionButton;
    private ActivityAttentionList parentActivity;


    public static FragmentHospitalizedList newInstance() {

        Bundle args = new Bundle();

        FragmentHospitalizedList fragment = new FragmentHospitalizedList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionList) context;
    }


    @Override
    protected int getLayoutIdRefresh() {
        return R.layout.fragment_attention_list_common;
    }

    @Override
    protected void initToolbarRefresh() {

    }

    @Override
    protected void initOnCreateView() {
        initEmptyView();
//        exitData();

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        floatingActionButton = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity, AttentionAddHospitalizedActivity.class);
                startActivityForResult(intent,1);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(floatingActionButton));

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getData();
    }

    @Override
    public void exitData() {
        showEmptyLayoutState(LOADING_STATE_SUCESS);
    }

    @Override
    public void noData() {
        showEmptyLayoutState(LOADING_STATE_EMPTY);
    }

    @Override
    public void noNet() {
        showEmptyLayoutState(LOADING_STATE_NO_NET);
    }

    @Override
    public void showData(final List<Hospitalized> dbList) {
        if (mAdapter == null) {
            mAdapter = new AttentionHospListAdapter(parentActivity,dbList);
            mAdapter.setItemClickListener(new AdapterClickInterface() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(parentActivity, AttentionHospitalizedDetailActivity.class);
                    presenter.startHospitalizedDetail(intent,position);
                    startActivityForResult(intent,1);
                }

                @Override
                public void onItemLongClick(View v, int position) {

                }
            });
            recyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.updateData(dbList);
        }
    }

    @Override
    public void setPresenter(AttentionHospitalizedListContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionHospitalizedDetailPresenter.DeleteHospitalizedCodeOk){//删除住院项
            presenter.getData();
        } else if (resultCode == AttentionAddHospitalizedPresenter.AddOrEditHospitalizedCodeOk) {//创建,更新 住院
            presenter.getData();
        }
    }

}
