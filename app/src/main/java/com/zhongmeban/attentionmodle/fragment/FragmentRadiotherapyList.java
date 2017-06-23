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
import com.zhongmeban.adapter.RadiotherapyListAdapter;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionAddRadiotherapyActivity;
import com.zhongmeban.attentionmodle.activity.AttentionRadiotherapyDetailActivity;
import com.zhongmeban.attentionmodle.contract.AttentionRadiotherapyListContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddRadiotherapyPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionRadiotherapyDetailPresenter;
import com.zhongmeban.base.BaseFragmentRefresh;
import de.greenrobot.dao.attention.Radiotherapy;
import java.util.List;

/**
 * 放疗记录List
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentRadiotherapyList extends BaseFragmentRefresh implements  AttentionRadiotherapyListContract.View{


    private AttentionRadiotherapyListContract.Presenter presenter;
    private RecyclerView recyclerView;
    private RadiotherapyListAdapter mAdapter;
    private FloatingActionButton floatingActionButton;
    private ActivityAttentionList parentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionList) context;
    }

    public static FragmentRadiotherapyList newInstance() {

        Bundle args = new Bundle();
        FragmentRadiotherapyList fragment = new FragmentRadiotherapyList();
        fragment.setArguments(args);
        return fragment;
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

        initEmptyView( );
//        showEmptyLayoutState(LOADING_STATE_SUCESS);

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        floatingActionButton = (FloatingActionButton)mRootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity,AttentionAddRadiotherapyActivity.class);
                startActivityForResult(intent,1);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(floatingActionButton));


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.start();
        presenter.getData();
    }



    @Override
    public void setPresenter(AttentionRadiotherapyListContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }


    @Override public void onResume() {
        super.onResume();
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
    public void showData(List<Radiotherapy> dbList) {
        if (mAdapter == null) {
            mAdapter = new RadiotherapyListAdapter(parentActivity,dbList);
            mAdapter.setItemClickListener(new AdapterClickInterface() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(parentActivity, AttentionRadiotherapyDetailActivity.class);
                    presenter.startRadiotherapyDetail(intent,position);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionRadiotherapyDetailPresenter.DeleteRadiotherapyCodeOk|| resultCode == AttentionAddRadiotherapyPresenter.AddOrEditRadiotherapyCodeOk){
            presenter.getData();
        }
    }

}
