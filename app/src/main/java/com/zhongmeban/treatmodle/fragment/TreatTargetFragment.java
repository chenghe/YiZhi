package com.zhongmeban.treatmodle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.TreatMethodCommonBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.DisplayUtil;
import com.zhongmeban.view.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 靶向治疗
 */

public class TreatTargetFragment extends BaseFragmentRefresh {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private int cancerId;
    private RecyclerView rvList;
    private CommonAdapter adapter;
    private List<TreatMethodCommonBean> mDatas = new ArrayList<>();


    public static TreatTargetFragment newInstance(int cancleId) {
        TreatTargetFragment fragment = new TreatTargetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, cancleId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cancerId = getArguments().getInt(ARG_PARAM);
        }
    }


    @Override protected int getLayoutIdRefresh() {

        return R.layout.treat_operation_fragment;
    }


    @Override protected void initToolbarRefresh() {
        mRefreshLayout.setEnabled(false);
    }


    @Override protected void initOnCreateView() {

        initEmptyView();
        for (int i = 0; i < 10; i++) {
            //mDatas.add(new TreatMethodCommonBean(i, "名字", "化学名"));
        }

        rvList = (RecyclerView) mRootView.findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(
            new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        rvList.setAdapter(adapter =
            new CommonAdapter<TreatMethodCommonBean>(getActivity(), R.layout.item_textview_only,
                mDatas) {

                @Override
                protected void convert(ViewHolder holder, final TreatMethodCommonBean medicineTypeBean, final int position) {

                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dip2px(mContext, 60));
                    holder.itemView.setLayoutParams(params);
                    holder.setText(R.id.tv, medicineTypeBean.getShowName());
                    TextView view = holder.getView(R.id.tv_sub_name);
                    view.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_sub_name, medicineTypeBean.getChemicalName());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(mContext, ActPharmaceutiaclDetail.class);
                            String therapeuticId = medicineTypeBean.getId()+"";
                            intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID,
                                therapeuticId);
                            startActivity(intent);
                        }
                    });

                }
            });

        getData();
    }


    private void getData() {

        HttpService.getHttpService().getTargetList(cancerId)
            .compose(RxUtil.<HttpResult<List<TreatMethodCommonBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<TreatMethodCommonBean>>>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    showOnError(e, false);
                }


                @Override
                public void onNext(HttpResult<List<TreatMethodCommonBean>> listHttpResult) {
                    showOnNextLayout(listHttpResult);

                    mDatas.clear();
                    mDatas = listHttpResult.getData();
                    adapter.notifyData(mDatas);
                    hideSwipeRefresh();

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

