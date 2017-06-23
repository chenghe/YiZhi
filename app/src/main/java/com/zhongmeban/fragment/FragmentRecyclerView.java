package com.zhongmeban.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmeban.R;
import com.zhongmeban.adapter.RecyclerViewSingleAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * Created by Chengbin He on 2016/3/23.
 */
public class FragmentRecyclerView extends BaseFragment {
    private View mView;
    private RecyclerView recyclerView;
    private BaseRecyclerViewAdapter mAdapter;
    private Context mContext ;
    private String adapterType; //Activity 传入，动态改变Fragment Adapter类型

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null){
            adapterType = (String) getArguments().get("FragmentRecyclerView");
        }else{
            adapterType = "";

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_with_recyclerview,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mView = view;
        //根据adapterType 的不同初始化不同Adapter
        if (adapterType.equals("medicine")){
            mAdapter = new RecyclerViewSingleAdapter(mContext);
        }else{
            mAdapter = new RecyclerViewSingleAdapter(mContext);
        }
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
    }
}
