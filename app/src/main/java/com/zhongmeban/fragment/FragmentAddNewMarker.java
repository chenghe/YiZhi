package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAddNewMarker;
import com.zhongmeban.activity.ActivityMarkerDetail;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionHospChooseActivity;
import com.zhongmeban.attentionmodle.adapter.AttentionPlanMarkerAdapter;
import com.zhongmeban.attentionmodle.fragment.AttentionHospChoosePresenter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexItem;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;
import com.zhongmeban.bean.postbody.CreateIndexRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.disk.CacheUtil;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.MyListView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 添加新的标志物主界面Fragment
 * Created by Chengbin He on 2016/7/27.
 */
public class FragmentAddNewMarker extends BaseFragment implements View.OnClickListener {

    public static final String EXTRA_SELECT_HOSPITAL_NAME = "extra_select_hospital_name";
    public static final String EXTRA_LAST_INDEXS = "extra_last_indexs";
    public static final String KEY_CACHE_INDEXS = "key_cache_indexs";
    public static final int RESULT_CODE_ADD_HOSPITAL = 120;

    private ActivityAddNewMarker parentActivity;
    private RelativeLayout rlHospital;
    private RelativeLayout rlDate;
    private Button button;
    private TextView tvHospitalName;
    private TextView tvDate;
    private TextView tvAddMaker;//添加标志物按钮
    private TextView tvDiagnosis;//检查目的，诊断按钮
    private TextView tvReexamination;//检查目的，复查按钮
    private TextView tvEvaluation;//检查目的，诊断按钮
    private TextView tvBodyCheck;//检查目的，诊断按钮
    private MyListView mListView;//计划标志物列表
    private AttentionPlanMarkerAdapter markerAdapter;

    private Subscription subscription;
    private CreateIndexRecordBody createIndexRecordBody;
    private AttentionMarkerIndexItem indexItem;
    private MaterialDialog progressDiaglog;
    //上传index


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAddNewMarker) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_marker, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitle(view);

        createIndexRecordBody = new CreateIndexRecordBody();

        indexItem = new AttentionMarkerIndexItem();
        indexItem.setPatientId(parentActivity.patientId);
        createIndexRecordBody.setIndexItem(indexItem);

        initView(view);

    }


    protected void initTitle(View view) {
        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
        String titleName = "";
        if (parentActivity.ISEDIT) {
            titleName = "编辑标志物记录";
        } else {
            titleName = "添加新标志物记录";
        }
        title.slideCentertext(titleName);
        title.backSlid(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentActivity.finish();
                }
            });
    }


    private void initView(View view) {

        tvDate = (TextView) view.findViewById(R.id.tv_date);
        Log.i("hcb", "parentActivity.chooseDate" + parentActivity.chooseDate);
        if (parentActivity.chooseDate > 0) {//判断是否有初始时间
            tvDate.setText(changeDateToString(parentActivity.chooseDate));
        }
        rlHospital = (RelativeLayout) view.findViewById(R.id.rl_hospital);
        rlHospital.setOnClickListener(this);
        tvHospitalName = (TextView) view.findViewById(R.id.tv_hospital);
        if (!TextUtils.isEmpty(parentActivity.hospName)) {
            tvHospitalName.setText(parentActivity.hospName);
        }

        rlDate = (RelativeLayout) view.findViewById(R.id.rl_date);
        rlDate.setOnClickListener(this);

        tvAddMaker = (TextView) view.findViewById(R.id.tv_addmarker);
        tvAddMaker.setOnClickListener(this);

        mListView = (MyListView) view.findViewById(R.id.plan_marker);
        markerAdapter = new AttentionPlanMarkerAdapter(parentActivity, parentActivity.indexsList);
        markerAdapter.setItemClickListenter(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                parentActivity.indexsList.remove(position);
                markerAdapter.notifyDataSetChanged();
            }


            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        mListView.setAdapter(markerAdapter);

        tvDiagnosis = (TextView) view.findViewById(R.id.tv_diagnosis);
        tvDiagnosis.setOnClickListener(this);
        tvReexamination = (TextView) view.findViewById(R.id.tv_reexamination);
        tvReexamination.setOnClickListener(this);
        tvEvaluation = (TextView) view.findViewById(R.id.tv_evaluation);
        tvEvaluation.setOnClickListener(this);
        tvBodyCheck = (TextView) view.findViewById(R.id.tv_body_check);
        tvBodyCheck.setOnClickListener(this);
        purpose(parentActivity.purposeType);

        button = (Button) view.findViewById(R.id.bt_save);
        button.setOnClickListener(this);

    }


    //检查用户添加的标记物是否有内容
    private boolean isMarkerEditValue() {

        for (AttentionMarkerIndexs index : parentActivity.indexsList) {
            if (TextUtils.isEmpty(index.getValue())) {
                return false;
            }
        }
        return parentActivity.indexsList.size() > 0;
    }


    public void updateMarkerList() {
        markerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_date://检查时间
                showDatePicker(parentActivity, new OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        long startTime = changeDateToLong(dateDesc);
                            tvDate.setText(year + "年" + month + "月" + day + "日");
                            parentActivity.chooseDate = startTime;

                    }
                }, getTodayData());
                break;
            case R.id.rl_hospital://检查医院
                Intent intent = new Intent(parentActivity, AttentionHospChooseActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_addmarker://添加标志物
                parentActivity.startAttentionIndicatorsFragment();
                break;
            case R.id.bt_save://保存按钮
                saveMarkerToNet();
                break;
            case R.id.tv_diagnosis://诊断
                parentActivity.purposeType = 1;
                purpose(parentActivity.purposeType);
                break;
            case R.id.tv_reexamination://复查
                parentActivity.purposeType = 2;
                purpose(parentActivity.purposeType);
                break;
            case R.id.tv_evaluation://疗效评估
                parentActivity.purposeType = 3;
                purpose(parentActivity.purposeType);
                break;
            case R.id.tv_body_check://身体状况检查
                parentActivity.purposeType = 4;
                purpose(parentActivity.purposeType);

                Logger.d("有没有值--" + isMarkerEditValue());
                break;
        }
    }


    private void saveMarkerToNet() {

        if (parentActivity.purposeType < 1) {
            ToastUtil.showText(parentActivity, "请选择检查目的");
        } else if (parentActivity.chooseDate < 1) {
            ToastUtil.showText(parentActivity, "请选择检查日期");
        } else if (parentActivity.hospId < 1) {
            ToastUtil.showText(parentActivity, "请选择检查医院");
        } else if (parentActivity.indexsList.size() < 1) {
            ToastUtil.showText(parentActivity, "请选择标志物");
        } else if (!isMarkerEditValue()) {
            ToastUtil.showText(parentActivity, "请填写标志物的值");
        } else {
            parentActivity.initBody();
            progressDiaglog = showProgressDialog("正在上传数据，请稍后", parentActivity);
            if (parentActivity.ISEDIT) {
                updateHttpData(parentActivity.createIndexRecordBody, parentActivity.token);
            } else {
                postCreateHttpData(parentActivity.createIndexRecordBody, parentActivity.token);
            }
            saveLastIndex();
        }
    }


    private void saveLastIndex() {
        List<AttentionMarkerIndexs> cacheList = new ArrayList<>();
        cacheList.clear();
        cacheList.addAll(parentActivity.indexsList);
        Observable.from(parentActivity.indexsList).map(
            new Func1<AttentionMarkerIndexs, AttentionMarkerIndexs>() {
                @Override public AttentionMarkerIndexs call(AttentionMarkerIndexs markerIndexs) {
                    AttentionMarkerIndexs marker = new AttentionMarkerIndexs();
                    marker.setId(markerIndexs.getId());
                    marker.setIndexId(markerIndexs.getIndexId());
                    marker.setName(markerIndexs.getName());
                    marker.setTime(markerIndexs.getTime());
                    marker.setUnit(markerIndexs.getUnit());
                    marker.setValue("");
                    return marker;
                }
            }).toList().subscribe(new Action1<List<AttentionMarkerIndexs>>() {
            @Override public void call(List<AttentionMarkerIndexs> attentionMarkerIndexses) {
                CacheUtil.getInstance()
                    .getCacheHelper()
                    .put(KEY_CACHE_INDEXS + parentActivity.patientId,
                        (Serializable) attentionMarkerIndexses);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionHospChoosePresenter.AttentionChooseHospOk) {//医院选择回调
            parentActivity.hospName = data.getStringExtra(
                AttentionHospChoosePresenter.AttentionHospName);
            parentActivity.hospId = data.getIntExtra(AttentionHospChoosePresenter.AttentionHospId,
                0);
            tvHospitalName.setText(parentActivity.hospName);
        }
    }


    /**
     * 创建指标检查记录
     */
    public void postCreateHttpData(CreateIndexRecordBody createIndexRecordBody, String token) {

        //        createIndexRecordBody.setIndexs(parentActivity.indexsList);
        //        indexItem.setHospitalId(hospId);
        //        indexItem.setTime(parentActivity.chooseDate);
        //        indexItem.setType(parentActivity.purposeType+"");

        subscription = HttpService.getHttpService()
            .postCreateIndexRecord(createIndexRecordBody, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override
                public void onCompleted() {
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean.getResult()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDiaglog.dismiss();
                                Intent intent = new Intent(parentActivity,
                                    ActivityAttentionList.class);
                                parentActivity.setResult(300, intent);
                                parentActivity.finish();
                            }
                        }, 1000);
                    }
                }
            });
    }


    /**
     * 更新指标检查记录
     */
    public void updateHttpData(CreateIndexRecordBody createIndexRecordBody, String token) {

        //        createIndexRecordBody.setIndexs(parentActivity.indexsList);
        //        indexItem.setId(parentActivity.indexItemId);
        //        indexItem.setHospitalId(hospId);
        //        indexItem.setTime(parentActivity.chooseDate);
        //        indexItem.setType(parentActivity.purposeType+"");

        subscription = HttpService.getHttpService()
            .postUpdateIndexRecord(createIndexRecordBody, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override
                public void onCompleted() {
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean.getResult()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDiaglog.dismiss();
                                Intent intent = new Intent(parentActivity,
                                    ActivityMarkerDetail.class);
                                parentActivity.setResult(300, intent);
                                parentActivity.finish();
                            }
                        }, 1000);
                    }
                }
            });
    }


    /**
     * 更换检查目的
     *
     * @param type 1.诊断 2.复查 3.疗效评估 4.身体状况监测
     */
    private void purpose(int type) {
        switch (type) {
            case 1:
                tvDiagnosis.setBackgroundResource(R.drawable.bg_box_selected);
                tvDiagnosis.setTextColor(getResources().getColor(R.color.white));
                tvReexamination.setBackgroundResource(R.drawable.bg_box);
                tvReexamination.setTextColor(getResources().getColor(R.color.content_two));
                tvEvaluation.setBackgroundResource(R.drawable.bg_box);
                tvEvaluation.setTextColor(getResources().getColor(R.color.content_two));
                tvBodyCheck.setBackgroundResource(R.drawable.bg_box);
                tvBodyCheck.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 2:
                tvDiagnosis.setBackgroundResource(R.drawable.bg_box);
                tvDiagnosis.setTextColor(getResources().getColor(R.color.content_two));
                tvReexamination.setBackgroundResource(R.drawable.bg_box_selected);
                tvReexamination.setTextColor(getResources().getColor(R.color.white));
                tvEvaluation.setBackgroundResource(R.drawable.bg_box);
                tvEvaluation.setTextColor(getResources().getColor(R.color.content_two));
                tvBodyCheck.setBackgroundResource(R.drawable.bg_box);
                tvBodyCheck.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 3:
                tvDiagnosis.setBackgroundResource(R.drawable.bg_box);
                tvDiagnosis.setTextColor(getResources().getColor(R.color.content_two));
                tvReexamination.setBackgroundResource(R.drawable.bg_box);
                tvReexamination.setTextColor(getResources().getColor(R.color.content_two));
                tvEvaluation.setBackgroundResource(R.drawable.bg_box_selected);
                tvEvaluation.setTextColor(getResources().getColor(R.color.white));
                tvBodyCheck.setBackgroundResource(R.drawable.bg_box);
                tvBodyCheck.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 4:
                tvDiagnosis.setBackgroundResource(R.drawable.bg_box);
                tvDiagnosis.setTextColor(getResources().getColor(R.color.content_two));
                tvReexamination.setBackgroundResource(R.drawable.bg_box);
                tvReexamination.setTextColor(getResources().getColor(R.color.content_two));
                tvEvaluation.setBackgroundResource(R.drawable.bg_box);
                tvEvaluation.setTextColor(getResources().getColor(R.color.content_two));
                tvBodyCheck.setBackgroundResource(R.drawable.bg_box_selected);
                tvBodyCheck.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                tvDiagnosis.setBackgroundResource(R.drawable.bg_box);
                tvDiagnosis.setTextColor(getResources().getColor(R.color.content_two));
                tvReexamination.setBackgroundResource(R.drawable.bg_box);
                tvReexamination.setTextColor(getResources().getColor(R.color.content_two));
                tvEvaluation.setBackgroundResource(R.drawable.bg_box);
                tvEvaluation.setTextColor(getResources().getColor(R.color.content_two));
                tvBodyCheck.setBackgroundResource(R.drawable.bg_box);
                tvBodyCheck.setTextColor(getResources().getColor(R.color.content_two));
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

    }
}
