package com.zhongmeban.attentionmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.attentionbean.postbody.CreatAssistMedicineBean;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.fragment.FragmentAssistMedicineList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.activity.TreatMedicineListSelectActivity;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.CommonEditView;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.MyListView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhongmeban.treatmodle.activity.TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY;

/**
 * 功能描述：增加辅助用药
 * ActivityAttentionAddChemotherapy 这是旧的
 * 作者：ysf on 2016/12/29 16:51
 */
public class AttentionAddAssistMedicineActivity extends BaseActivityToolBar
    implements View.OnClickListener {

    public static final int EXTRA_CLASS_TYPE = 2;
    private boolean isFromEdit = false;

    private CommonEditView etStartTime;
    private CommonEditView etEndTime;
    private Context mContext;
    private Button btnSave;
    private TextView tvAdd;
    private CommonAdapter medicineAdapter;
    private MyListView listMedicine;
    private List<ChemotherapyRecordBean.Medicines> mDataMedicine = new ArrayList<>();

    private CreatAssistMedicineBean postBody;
    private List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean> listMedicineId
        = new ArrayList<>();
    private List<CreatAssistMedicineBean.ItemRecordsBean> listMedicineRecord = new ArrayList<>();

    private String patientId;
    private long id;
    private Long startTime;
    private Long endTime;

    private ChemotherapyRecordBean.Record mDatasRecord;


    //act_attention_add_chemoth_record
    @Override protected int getLayoutId() {
        return R.layout.act_attention_add_use_medicine_record;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("新增用药记录", "");
    }


    @Override protected void initView() {

        initUI();

        handUI();

        initData();
    }


    private void initData() {

    }


    private void handPostBody() {
        patientId = (String) SPUtils.getParams(mContext, SPInfo.UserKey_patientId, "",
            SPInfo.SPUserInfo);
        for (ChemotherapyRecordBean.Medicines medicine : mDataMedicine) {
            listMedicineId.add(new CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean(medicine.getMedicineId()));
        }

        //如果选了,结束时间不为空
        if (!checkTime(endTime)) {
            //long endTime, String patientId, long startTime, List<MedicineRecordDbsBean> medicineRecordDbs
            listMedicineRecord.add(
                new CreatAssistMedicineBean.ItemRecordsBean(endTime, patientId, startTime,
                    listMedicineId));
        } else {
            Observable.from(listMedicineId).map(
                new Func1<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean, List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean>>() {
                    @Override
                    public List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean> call(CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean medicineRecordDbsBean) {
                        List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean> items
                            = new ArrayList<>();
                        items.add(medicineRecordDbsBean);
                        return items;
                    }
                }).subscribe(
                new Action1<List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean>>() {
                    @Override
                    public void call(List<CreatAssistMedicineBean.ItemRecordsBean.MedicineRecordDbsBean> medicineRecordDbsBeen) {
                        listMedicineRecord.add(
                            new CreatAssistMedicineBean.ItemRecordsBean(endTime, patientId,
                                startTime,
                                medicineRecordDbsBeen));
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Logger.e("出错了--" + throwable.toString());
                    }
                });

        }
        postBody = new CreatAssistMedicineBean(listMedicineRecord);
    }


    private void handUI() {
        mContext = this;

        listMedicine.setAdapter(
            medicineAdapter = new CommonAdapter<ChemotherapyRecordBean.Medicines>(this,
                R.layout.item_attention_edit_add_medicine, mDataMedicine) {
                @Override
                protected void convert(ViewHolder viewHolder, ChemotherapyRecordBean.Medicines item, final int position) {
                    viewHolder.setText(R.id.tv_edit_add_medicine_showname, item.getMedicineName());
                    viewHolder.setText(R.id.tv_edit_add_medicine_name, item.getChemicalName());

                    viewHolder.setOnClickListener(R.id.iv_edit_add_medicine_delete,
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                mDataMedicine.remove(position);
                                medicineAdapter.notifyDataSetChanged();
                            }
                        });
                }
            });

    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_medicine_start_time:
                showDataDialog(true);
                break;
            case R.id.et_medicine_end_time:
                showDataDialog(false);
                break;
            case R.id.tv_add_medicine:

                Intent intent = new Intent(this,
                    TreatMedicineListSelectActivity.class);
                intent.putExtra(TreatMedicineListSelectActivity.EXTRA_MEDICINE_TYPE_BEAN,
                    new MedicineTypeBean(0,   "所有药品"));
                intent.putExtra(TreatMedicineListSelectActivity.EXTRA_MEDICINE_SELECT_LIST,
                    (Serializable) mDataMedicine);
                intent.putExtra(EXTRA_FROM_ACTIVITY, EXTRA_CLASS_TYPE);//用来表示 哪个类启动的药品列表
                startActivityForResult(intent,1);
                break;
            case R.id.bt_save:
                saveDataToNet();
                break;
        }
    }


    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (TreatMedicineListSelectActivity.RESLUT_CODE_SELECT==resultCode){
            List<ChemotherapyRecordBean.Medicines> selectData
                = (List<ChemotherapyRecordBean.Medicines>) data.getSerializableExtra(
                TreatMedicineListSelectActivity.EXTRA_MEDICINE_SELECT_LIST);
            Logger.v("newIntent--" + selectData.size());
            if (selectData == null) return;
            mDataMedicine.clear();
            mDataMedicine.addAll(selectData);

            //medicineAdapter.updataList(mDataMedicine);
            medicineAdapter.notifyDataSetChanged();

        }

    }


    // true 代表开始时间
    private void showDataDialog(final boolean isStartTime) {
        showDatePicker(this, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                if (isStartTime) {
                    startTime = TimeUtils.changeDateToLong(dateDesc);
                    etStartTime.setRightText(dateDesc);
                } else {
                    endTime = TimeUtils.changeDateToLong(dateDesc);
                    etEndTime.setRightText(dateDesc);
                }
            }
        }, getTodayData());
    }


    private void initUI() {
        mContext = this;
        etStartTime = (CommonEditView) findViewById(R.id.et_medicine_start_time);
        etEndTime = (CommonEditView) findViewById(R.id.et_medicine_end_time);
        tvAdd = (TextView) findViewById(R.id.tv_add_medicine);
        btnSave = (Button) findViewById(R.id.bt_save);
        listMedicine = (MyListView) findViewById(R.id.lv_attention_medicine_add_medicine);

        etStartTime.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
    }


    private void creatMedicnetRecord(CreatAssistMedicineBean bean) {
        HttpService.getHttpService().createMedicineRecordList(bean)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    dissmisProgress();
                    if (httpResult.isResult()) {
                        ToastUtil.showText(mContext, "创建成功");
                        setResult(FragmentAssistMedicineList.RESLUT_CODE_SAVE);
                        finish();
                    } else {
                        ToastUtil.showText(mContext, httpResult.getErrorMessage());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    dissmisProgress();
                    ToastUtil.showText(mContext, "请求失败");
                    Logger.e("1111" + throwable.toString());
                }
            });
    }


    private void saveDataToNet() {
        if (checkTime(startTime)) {
            ToastUtil.showText(mContext, "请选择开始日期");
        } else if (mDataMedicine.size() < 1) {
            ToastUtil.showText(mContext, "请选择药物");
        } else {
            showProgressDialog("正在上传数据，请稍后", mContext);
            handPostBody();

            creatMedicnetRecord(postBody);

        }
    }


    /**
     * \
     *
     * @return true 代表 无效时间
     */
    private boolean checkTime(Long time) {
        return time == null || time < 10000;
    }


    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
