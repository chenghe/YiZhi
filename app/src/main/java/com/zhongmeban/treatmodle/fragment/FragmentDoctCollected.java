package com.zhongmeban.treatmodle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmeban.R;
import com.zhongmeban.treatmodle.adapter.DoctListsAdapter;
import com.zhongmeban.base.BaseFragment;

/**
 * 我收藏的医生Fragment
 * Created by Chengbin He on 2016/10/25.
 */

public class FragmentDoctCollected extends BaseFragment {

    private DoctListsAdapter mAdapter;
    private RecyclerView recyclerView;

    public static FragmentDoctCollected newInstance() {

        Bundle args = new Bundle();

        FragmentDoctCollected fragment = new FragmentDoctCollected();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_recyclerview, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 网络请求获取数据
     */
    private void getHttpData() {
//        HttpService.getHttpService().getFavoriteDoctor(,1,10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DoctorList>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "http onCompleted()");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshlayout.setRefreshing(false);
//                            }
//                        }, 1000);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "http onError()");
//                        Log.i("hcb", "e " + e);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshlayout.setRefreshing(false);
//                            }
//                        }, 2000);
//                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
//                        recyclerView.setVisibility(View.GONE);
//                        tv_noentity.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onNext(DoctorList doctorList) {
//                        Log.i("hcb", "http onNext()");
//                        if (doctorList.getData().getSourceItems().size() > 0) {
//                            refreshlayout.setVisibility(View.VISIBLE);
//                            tv_noentity.setVisibility(View.GONE);
//                            if (pageIndex == 1) {
//                                httpList.clear();
//                                httpList = doctorList.getData().getSourceItems();
//                                Log.i("hcb", "httpList.size()" + httpList.size());
//                            } else {
//                                onPostLoadMore();
//
//                                httpList.addAll(doctorList.getData().getSourceItems());
//                            }
//                            mAdapter.notifyData(httpList);
//                        }else{
//                            refreshlayout.setVisibility(View.GONE);
//                            tv_noentity.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
    }
}