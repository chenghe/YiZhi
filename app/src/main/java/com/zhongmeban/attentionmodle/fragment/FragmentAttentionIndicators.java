package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAddNewMarker;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexSelectBean;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;
import com.zhongmeban.net.HttpResultFunc;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.genericity.SPInfo;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 关注模块，新增标志物记录，添加标志物Fragment
 * Created by Chengbin He on 2016/7/27.
 */
public class FragmentAttentionIndicators extends BaseFragmentRefresh{

    private ListView listView;
    private ActivityAddNewMarker parentActivity;
    private CommonAdapter adapter;
    private List<AttentionMarkerIndexs> mIndexsList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAddNewMarker) context;
    }


    @Override protected int getLayoutIdRefresh() {
        return R.layout.fragment_attention_medicine_list;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("请选择指标项", "完成");
    }


    @Override protected void initOnCreateView() {
        mIndexsList.clear();
        initEmptyView();
        getData();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initView(view);
    }


    private void initView(final View view) {
        listView = (ListView) view.findViewById(R.id.listview);


        listView.setAdapter(adapter = new CommonAdapter<AttentionMarkerIndexs>(parentActivity,
            R.layout.item_marker_select, mIndexsList) {
            @Override
            protected void convert(ViewHolder viewHolder, AttentionMarkerIndexs item, final int position) {
                SmoothCheckBox cb = viewHolder.getView(R.id.cb_marler_select);
                viewHolder.setText(R.id.tv_marler_name, item.getName());


                cb.setChecked(parentActivity.indexsList.contains(mIndexsList.get(position)));

                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        updateSelectList(position);
                    }
                });
            }
        });

    }


    private void updateSelectList(int pos) {
        AttentionMarkerIndexs markerIndexs = mIndexsList.get(pos);

        if (parentActivity.indexsList.contains(markerIndexs)) {
            parentActivity.indexsList.remove(markerIndexs);
        } else {
            parentActivity.indexsList.add(markerIndexs);
        }

        adapter.notifyDataSetChanged();

    }


    @Override public void clickRight() {
        super.clickRight();
        parentActivity.fragmentAddNewMarker.updateMarkerList();
        parentActivity.onBackPressed();
    }


    @Override public void clickLeft() {
        super.clickLeft();
        parentActivity.onBackPressed();
    }


    private void getData() {

        int dieaseId = (int) SPUtils.getParams(parentActivity, SPInfo.UserKey_patientDiseaseId, 0,
            SPInfo.SPUserInfo);
        HttpService.getHttpService().getMarkerSelectList(dieaseId, true, "")
            .map(new HttpResultFunc<List<AttentionMarkerIndexSelectBean>>())
            .compose(RxUtil.<List<AttentionMarkerIndexSelectBean>>normalSchedulers())
            .subscribe(new Subscriber<List<AttentionMarkerIndexSelectBean>>() {
                @Override public void onCompleted() {

                }
                @Override public void onError(Throwable e) {
                    Logger.e("错误" + e.toString());
                    showEmptyLayoutState(LOADING_STATE_NO_NET);
                }


                @Override public void onNext(List<AttentionMarkerIndexSelectBean> resultList) {

                    showEmptyLayoutState(LOADING_STATE_SUCESS);

                    if (resultList.size() == 0) {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }
                    for (AttentionMarkerIndexSelectBean bean : resultList) {
                        mIndexsList.add(new AttentionMarkerIndexs(0,bean.getId(),"",0L,bean.getName(),bean.getUnit()));
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }


    @Override public void onDestroyView() {
        super.onDestroyView();

    }


    @Override public void onDestroy() {
        super.onDestroy();
    }
}
