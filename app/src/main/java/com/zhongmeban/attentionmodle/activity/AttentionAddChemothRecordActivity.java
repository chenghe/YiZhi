package com.zhongmeban.attentionmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyBody;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.activity.TreatMedicineListSelectActivity;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.StringUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.CommonEditView;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.DatePicker.OnSinglePickedListener;
import com.zhongmeban.view.DatePicker.SinglePickerPopWin;
import com.zhongmeban.view.MyListView;
import com.zhongmeban.view.TagFlowBoxLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.functions.Action1;

import static com.zhongmeban.attentionmodle.activity.AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_INFO;
import static com.zhongmeban.attentionmodle.activity.AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_MEDICINES;
import static com.zhongmeban.treatmodle.activity.TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY;

/**
 * 功能描述：增加化疗
 * ActivityAttentionAddChemotherapy 这是旧的
 * 作者：ysf on 2016/12/29 16:51
 */
public class AttentionAddChemothRecordActivity extends BaseActivityToolBar
    implements View.OnClickListener {

    public static final int EXTRA_CLASS_TYPE = 1;
    public static final String EXTRA_IS_FROM_EDIT = "extra_is_from_edit";

    private boolean isFromEdit = false;

    private CommonEditView etDoctor;
    private CommonEditView etTime;
    private CommonEditView etCircle;
    private EditText etRemark;
    private Context mContext;
    private Button btnSave;
    private TextView tvAdd;
    private CommonAdapter medicineAdapter;
    private MyListView listMedicine;

    private String[] targetArray = new String[] { "根治性化疗", "姑息性化疗", "辅助化疗", "新辅助化疗", "局部化疗" };
    private String[] remarkArray = new String[] { "周期结束", "终止化疗", "更换方案", "治疗进行中" };
    private List<ChemotherapyRecordBean.Medicines> mDataMedicine = new ArrayList<>();

    private CreateOrUpdateChemotherapyBody postBody;
    private List<Integer> listMedicineId = new ArrayList<>();

    private String patientId;
    private long id;
    private long startTime;
    private int chemotherapyAim;//	化疗目的
    private int weeksCount = 0;//	化疗周期
    private String notes;
    private int notesType;//治疗备注
    private String doctorName;//
    private TagFlowBoxLayout tagViewRemark;
    private TagFlowBoxLayout tagViewTarget;

    private ChemotherapyRecordBean.Record mDatasRecord;


    @Override protected int getLayoutId() {
        return R.layout.act_attention_add_chemoth_record;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("新增化疗记录", "");
    }


    @Override protected void initView() {

        initUI();

        handUI();

        initData();
    }


    private void initData() {
        isFromEdit = getIntent().getBooleanExtra(EXTRA_IS_FROM_EDIT, false);
        if (isFromEdit) {
            mDatasRecord = (ChemotherapyRecordBean.Record) getIntent().getSerializableExtra(
                EXTRA_CHEMOTH_RECORD_INFO);
            List<ChemotherapyRecordBean.Medicines> oldList
                = (List<ChemotherapyRecordBean.Medicines>) getIntent().getSerializableExtra(
                EXTRA_CHEMOTH_RECORD_MEDICINES);
            mDataMedicine.addAll(oldList);
            medicineAdapter.notifyDataSetChanged();
            for (ChemotherapyRecordBean.Medicines medicine : mDataMedicine) {
                listMedicineId.add(medicine.getMedicineId());
            }

            chemotherapyAim = mDatasRecord.getChemotherapyAim();
            doctorName = mDatasRecord.getDoctorName();
            startTime = mDatasRecord.getStartTime();
            weeksCount = mDatasRecord.getWeeksCount();
            notesType = mDatasRecord.getNotesType();
            notes = mDatasRecord.getNotes();
            showData();
        }

    }


    private void showData() {
        tagViewTarget.setSelect(AttentionUtils.getChemotherapyTargetPosByType(chemotherapyAim));
        etDoctor.setRightEtText(doctorName);
        etTime.setRightText(TimeUtils.formatTime(startTime));
        etCircle.setRightText(weeksCount + "个周期");
        tagViewRemark.setSelect(AttentionUtils.getChemotherapyRemarkPosByType(notesType));
        etRemark.setText(notes);
    }


    private void handPostBody() {
        patientId = (String) SPUtils.getParams(mContext, SPInfo.UserKey_patientId, "",
            SPInfo.SPUserInfo);

        for (ChemotherapyRecordBean.Medicines medicine : mDataMedicine) {
            listMedicineId.add(medicine.getMedicineId());
        }
        doctorName = etDoctor.getEditString();
        notes = etRemark.getText().toString();
        postBody = new CreateOrUpdateChemotherapyBody();
        postBody.getRecord().setPatientId(patientId);
        postBody.getRecord().setChemotherapyAim(chemotherapyAim);
        postBody.getRecord().setDoctorName(doctorName);
        postBody.getRecord().setStartTime(startTime);
        postBody.getRecord().setWeeksCount(weeksCount);
        postBody.getRecord().setNotes(notes);
        postBody.getRecord().setNotesType(notesType);
        postBody.setMedicineIds(listMedicineId);

        //创建的时候没有id，但是编辑更新的时候必须带着id
        if (isFromEdit) {
            postBody.getRecord().setId(mDatasRecord.getId());
        }

    }


    private void handUI() {
        mContext = this;

        tagViewTarget.setData(Arrays.asList(targetArray));
        tagViewTarget.setOnTagClickListener(new TagFlowBoxLayout.OnTagClickListener() {
            @Override public void onTagClick(ViewGroup parent, View view, int position) {
                chemotherapyAim = AttentionUtils.getChemotherapyAnimByPos(position);
            }
        });

        tagViewRemark.setData(Arrays.asList(remarkArray));
        tagViewRemark.setOnTagClickListener(new TagFlowBoxLayout.OnTagClickListener() {
            @Override public void onTagClick(ViewGroup parent, View view, int position) {
                notesType = AttentionUtils.getChemotherapyRemarkByPos(position);
            }
        });

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
            case R.id.et_chemoth_circle:
                showSelectDialog();
                break;
            case R.id.et_chemoth_time:
                showDataDialog();
                break;
            case R.id.tv_add_medicine:

                Intent intent = new Intent(this,
                    TreatMedicineListSelectActivity.class);
                intent.putExtra(TreatMedicineListSelectActivity.EXTRA_MEDICINE_TYPE_BEAN,
                    new MedicineTypeBean(16, "化疗用药"));
                intent.putExtra(TreatMedicineListSelectActivity.EXTRA_MEDICINE_SELECT_LIST,
                    (Serializable) mDataMedicine);
                intent.putExtra(EXTRA_FROM_ACTIVITY, EXTRA_CLASS_TYPE);//用来表示 哪个类启动的药品列表
                startActivityForResult(intent, 1);

                //Intent intent = new Intent(this, TreatMedicineTypeListActivity.class);
                //intent.putExtra(EXTRA_FROM_ACTIVITY,
                //    EXTRA_CLASS_TYPE);//1  用来表示 哪个类启动的药品列表
                //startActivity(intent);
                break;
            case R.id.bt_save:
                saveDataToNet();
                break;
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (TreatMedicineListSelectActivity.RESLUT_CODE_SELECT == resultCode) {
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


    private void showSelectDialog() {
        List<String> mdata = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mdata.add((i + 1) + "");
        }
        new SinglePickerPopWin.Builder(this, new OnSinglePickedListener() {
            @Override public void onPickCompleted(int postion) {
                weeksCount = postion + 1;
                etCircle.setRightText(weeksCount + "个周期");
            }
        }).currentPos(weeksCount - 1).setData(mdata).build().showPopWin(this);
    }


    private void showDataDialog() {
        showDatePicker(this, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                startTime = TimeUtils.changeDateToLong(dateDesc);
                etTime.setRightText(dateDesc);
            }
        }, getTodayData());
    }


    private void initUI() {
        mContext = this;
        tagViewRemark = (TagFlowBoxLayout) findViewById(R.id.id_flow_tag);
        tagViewTarget = (TagFlowBoxLayout) findViewById(R.id.lvh_check_target);
        etDoctor = (CommonEditView) findViewById(R.id.et_chemoth_doctor);
        etTime = (CommonEditView) findViewById(R.id.et_chemoth_time);
        etCircle = (CommonEditView) findViewById(R.id.et_chemoth_circle);
        tvAdd = (TextView) findViewById(R.id.tv_add_medicine);
        btnSave = (Button) findViewById(R.id.bt_save);
        etRemark = (EditText) findViewById(R.id.et_content_remark);
        listMedicine = (MyListView) findViewById(R.id.lv_attention_chemo_add_medicine);

        etTime.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etCircle.setOnClickListener(this);
    }


    private void creatChemothRecord(CreateOrUpdateChemotherapyBody bean) {
        HttpService.getHttpService().postCreateChemotherapyRecord(bean)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    dissmisProgress();
                    if (httpResult.isResult()) {
                        ToastUtil.showText(mContext, "创建成功");
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


    private void updateChemothRecord(CreateOrUpdateChemotherapyBody bean) {
        HttpService.getHttpService().postUpdateChemotherapyRecord(bean)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    dissmisProgress();
                    if (httpResult.isResult()) {
                        ToastUtil.showText(mContext, "修改成功");
                        Intent intent = new Intent(AttentionAddChemothRecordActivity.this,
                            ActivityAttentionList.class);
                        intent.putExtra("attentionType", 4);
                        startActivity(intent);

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

        if (chemotherapyAim < 1) {
            ToastUtil.showText(mContext, "请选择化疗目的");
        } else if (!StringUtils.matchChart(etDoctor.getEditString())) {
            ToastUtil.showText(mContext, "医生只能输入中文和英文");
        } else if (startTime < 100) {
            ToastUtil.showText(mContext, "请选择开始日期");
        } else if (weeksCount < 1) {
            ToastUtil.showText(mContext, "请选择周期数");
        } else if (mDataMedicine.size() < 1) {
            ToastUtil.showText(mContext, "请选择药物");
        } else if (notesType < 1) {
            ToastUtil.showText(mContext, "请选择治疗备注");
        } else {
            showProgressDialog("正在上传数据，请稍后", mContext);
            handPostBody();

            if (isFromEdit) {
                updateChemothRecord(postBody);
            } else {
                creatChemothRecord(postBody);
            }

        }
    }


    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
