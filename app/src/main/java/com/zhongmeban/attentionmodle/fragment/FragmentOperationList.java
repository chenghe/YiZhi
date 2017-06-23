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
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionOperationDetailActivity;
import com.zhongmeban.attentionmodle.activity.AttentionAddOperationActivity;
import com.zhongmeban.attentionmodle.adapter.AttentionOperationListAdapter;
import com.zhongmeban.attentionmodle.contract.AttentionOperationListContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddOperationPresent;
import com.zhongmeban.base.BaseFragmentRefresh;

import java.util.List;

import de.greenrobot.dao.attention.SurgerySource;

/**
 * 关注手术记录List
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentOperationList extends BaseFragmentRefresh implements AttentionOperationListContract.View{

    private RecyclerView recyclerView;
    private AttentionOperationListContract.Presenter presenter;
    private AttentionOperationListAdapter mAdapter;
    private FloatingActionButton floatingActionButton;
    private ActivityAttentionList parentActivity;



    public static FragmentOperationList newInstance() {
        
        Bundle args = new Bundle();

        FragmentOperationList fragment = new FragmentOperationList();
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
//        showEmptyLayoutState(LOADING_STATE_SUCESS);

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        floatingActionButton = (FloatingActionButton)mRootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity,AttentionAddOperationActivity.class);
                startActivityForResult(intent,1);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(floatingActionButton));

//        presenter.start();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionAddOperationPresent.AddOrEditOperationCodeOk){
            //新增（编辑）返回
            presenter.getData();
        }else if (resultCode == 300){
            //删除返回
            presenter.getData();
        }
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
    public void showData(List<SurgerySource> dbList) {
        if (mAdapter == null) {
            mAdapter = new AttentionOperationListAdapter(parentActivity,dbList);
            mAdapter.setItemClickListener(new AdapterClickInterface() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(parentActivity, AttentionOperationDetailActivity.class);
                    presenter.startOperationDetail(intent,position);
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
    public void setPresenter(AttentionOperationListContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }
}
