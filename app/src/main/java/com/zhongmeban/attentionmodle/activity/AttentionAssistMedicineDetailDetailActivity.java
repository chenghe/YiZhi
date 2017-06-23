package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.attentionbean.AssistMedicineListBean;
import com.zhongmeban.bean.postbody.UpdateEndOrDeleteMedicineBody;
import com.zhongmeban.fragment.FragmentAssistMedicineList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.SpannableStringUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.MyListView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 化疗详情Activity
 * Created by
 */
public class AttentionAssistMedicineDetailDetailActivity extends BaseActivityToolBar
    implements View.OnClickListener {

    public static final String EXTRA_ASSIST_MEDICINE_RECORD_INFO
        = "extra_assist_medicine_record_info";

    private Activity mContext = AttentionAssistMedicineDetailDetailActivity.this;
    private Subscription dbSubscription;
    private Button btDelete;
    private Button btStop;
    private String pationId;
    private TextView tvInfo;
    private MyListView listView;
    private boolean isStop;

    private AssistMedicineListBean.MedicineRecordBean mDatasRecord;


    @Override protected int getLayoutId() {
        return R.layout.activity_attention_assist_medicine_detail;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("用药详情", "");
    }


    @Override protected void initView() {

        initUI();
        initData();
    }


    private void initUI() {
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        btStop = (Button) findViewById(R.id.bt_edit);
        btStop.setOnClickListener(this);

        tvInfo = (TextView) findViewById(R.id.tv_assist_medicine_info);
        listView = (MyListView) findViewById(R.id.lv_medicine_list_detail);
    }


    private void initData() {

        mDatasRecord = (AssistMedicineListBean.MedicineRecordBean) getIntent().getSerializableExtra(
            EXTRA_ASSIST_MEDICINE_RECORD_INFO);
        isStop = mDatasRecord.getEndTime() == null;
        btStop.setVisibility(isStop?View.VISIBLE:View.GONE);// true 代表没停止，正在服用中

        setTvInfo();
        listView.setAdapter(
            new com.zhongmeban.base.baseadapterlist.CommonAdapter<AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean>(
                this, R.layout.item_attention_edit_add_medicine,
                mDatasRecord.getMedicineRecordDbs()) {
                @Override
                protected void convert(com.zhongmeban.base.baseadapterlist.ViewHolder viewHolder, AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean item, int position) {
                    viewHolder.setText(R.id.tv_edit_add_medicine_showname,
                        item.getMedicineName()+"("+item.getChemicalName()+")");
                    viewHolder.getView(R.id.iv_edit_add_medicine_delete)
                        .setVisibility(View.GONE);
                    viewHolder.getView(R.id.tv_edit_add_medicine_name)
                        .setVisibility(View.GONE);
                }
            });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbSubscription != null && dbSubscription.isUnsubscribed()) {
            dbSubscription.unsubscribe();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete://删除
                showDeleteDialog(mContext, positiveCallback, "是否删除这条记录？");
                break;

            case R.id.bt_edit://编辑
                showDataDialog(mDatasRecord.getId());
                break;
        }
    }


    /**
     * 删除Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback
        = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            progressDiaglog = showProgressDialog("正在删除数据，请稍后", mContext);
            deleteMedicineRecord(mDatasRecord.getId());
            dialog.dismiss();
        }
    };


    // true 代表开始时间
    private void showDataDialog(final int recordId) {
        showDatePicker(this, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                stopMedicineRecord(recordId,TimeUtils.changeDateToLong(dateDesc));
            }
        }, getTodayData());
    }
    UpdateEndOrDeleteMedicineBody postBody;

    private void stopMedicineRecord(int id,Long endTime) {
        postBody =new UpdateEndOrDeleteMedicineBody(id,endTime);

        progressDiaglog = showProgressDialog("正在停止，请稍后", mContext);
        HttpService.getHttpService().updateEndMedicine(postBody)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    if (httpResult.isResult()) {
                        setResult(FragmentAssistMedicineList.RESLUT_CODE_DELETE);
                        finish();
                    } else {
                        ToastUtil.showText(mContext, httpResult.getErrorMessage());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    ToastUtil.showText(mContext, "请求失败");
                }
            });
    }

    private void deleteMedicineRecord(int id) {
        postBody =new UpdateEndOrDeleteMedicineBody(id,System.currentTimeMillis());
        HttpService.getHttpService().deleteMedicine(postBody)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    if (httpResult.isResult()) {
                        setResult(FragmentAssistMedicineList.RESLUT_CODE_DELETE);
                        finish();
                    } else {
                        ToastUtil.showText(mContext, httpResult.getErrorMessage());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    ToastUtil.showText(mContext, "请求失败");
                }
            });
    }


    // 设置标记物各种信息
    private void setTvInfo() {

        int content = ContextCompat.getColor(this, R.color.content_two);
        int textOne = ContextCompat.getColor(this, R.color.text_one);
        SpannableStringBuilder builder = SpannableStringUtils.getBuilder("开始日期：\n")
            .setForegroundColor(content)
            .append(TimeUtils.formatTime(mDatasRecord.getStartTime()) + "\n")
            .setForegroundColor(textOne)
            .append(isStop ? "" : "结束日期：\n")
            .setForegroundColor(content)
            .append(isStop? "": TimeUtils.formatTime(mDatasRecord.getEndTime()) + "\n")
            .setForegroundColor(textOne)
            .append("用药状态：\n")
            .setForegroundColor(content)
            .append(mDatasRecord.getEndTime() == null
                    ? "服用中\n"
                    : "已停用\n")
            .setForegroundColor(textOne)
            .append("辅助用药：")
            .setForegroundColor(content).create();
        tvInfo.setText(builder);
    }

}