package com.zhongmeban.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmeban.R;
import com.zhongmeban.adapter.AnswersAdapter;
import com.zhongmeban.base.BaseFragment;

/**
 * Created by Chengbin He on 2016/3/23.
 */
public class FragmentAnswer extends BaseFragment{
    private View mView;
    private RecyclerView recyclerView;
    private AnswersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_with_recyclerview,null);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AnswersAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        return mView;
    }
}
