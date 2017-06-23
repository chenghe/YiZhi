package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.orhanobut.logger.Logger;
import com.zhongmeban.Interface.RecyclerViewAnimListener;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionAddAssistMedicineActivity;
import com.zhongmeban.attentionmodle.activity.AttentionAssistMedicineDetailDetailActivity;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.attentionbean.AssistMedicineListBean;
import com.zhongmeban.bean.postbody.UpdateEndOrDeleteMedicineBody;
import com.zhongmeban.net.HttpResultFunc;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.DividerItemDecoration;
import com.zhongmeban.view.MyListView;
import de.greenrobot.dao.attention.AssistMedicineRecord;
import de.greenrobot.dao.attention.AssistMedicineRecordDao;
import de.greenrobot.dao.attention.MedicineRecord;
import de.greenrobot.dao.attention.MedicineRecordDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

/**
 * 辅助用药记录List
 * //ActivityAttentionMedicineList 旧的
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentAssistMedicineList extends BaseFragmentRefresh
    implements View.OnClickListener {

    public static int RESLUT_CODE_SAVE = 1;
    public static int RESLUT_CODE_DELETE = 2;

    private RecyclerView recyclerView;
    private CommonAdapter mAdapter;
    private FloatingActionButton btnAdd;
    private ActivityAttentionList parentActivity;
    private SharedPreferences serverTimeSP;
    private AssistMedicineRecordDao recordDao;
    private MedicineRecordDao medicineDao;//用药
    private String mServerTime;//Servertime
    private List<AssistMedicineListBean.MedicineRecordBean> httpList
        = new ArrayList<>();//网络辅助用药
    private List<AssistMedicineListBean.MedicineRecordBean> mDatasRecord
        = new ArrayList<>();//本地查出来的记录
    private String patientId;


    public static FragmentAssistMedicineList newInstance() {

        Bundle args = new Bundle();

        FragmentAssistMedicineList fragment = new FragmentAssistMedicineList();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionList) context;
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

        initEmptyView();
        serverTimeSP = parentActivity.getSharedPreferences(SPInfo.SPServerTime,
            Context.MODE_PRIVATE);
        mServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_medicineRecordServerTime,
            SPInfo.ServerTimeDefaultValue);

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.addItemDecoration(
            new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        btnAdd = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(btnAdd));
        btnAdd.setOnClickListener(this);
        setAdapter();
        updateList();
    }


    private void setAdapter() {
        recyclerView.setAdapter(
            mAdapter = new CommonAdapter<AssistMedicineListBean.MedicineRecordBean>(getActivity(),
                R.layout.item_attention_use_medicine_record, mDatasRecord) {
                @Override
                protected void convert(final ViewHolder holder, final AssistMedicineListBean.MedicineRecordBean recordBean, int position) {
                    holder.setText(R.id.tv_time, TimeUtils.formatTime(recordBean.getStartTime()));
                    TextView medicineState = holder.getView(R.id.tv_medicine_state);
                    TextView stopMedicie = holder.getView(R.id.tv_stop_medicine);
                    MyListView listView = holder.getView(R.id.lv_medicine_list);
                    if (recordBean.getEndTime() == null) {
                        holder.getView(R.id.ll_stop_layout).setVisibility(View.VISIBLE);
                        medicineState.setText("正在服用中");
                    } else {
                        holder.getView(R.id.ll_stop_layout).setVisibility(View.GONE);
                        medicineState.setText("已停用");
                    }
                    listView.setAdapter(
                        new com.zhongmeban.base.baseadapterlist.CommonAdapter<AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean>(
                            getActivity(), R.layout.item_attention_edit_add_medicine,
                            recordBean.getMedicineRecordDbs()) {
                            @Override
                            protected void convert(com.zhongmeban.base.baseadapterlist.ViewHolder viewHolder, AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean item, int position) {
                                viewHolder.setText(R.id.tv_edit_add_medicine_showname,
                                    item.getMedicineName());
                                viewHolder.setText(R.id.tv_edit_add_medicine_name,
                                    item.getChemicalName());
                                viewHolder.getView(R.id.iv_edit_add_medicine_delete)
                                    .setVisibility(View.GONE);

                                viewHolder.getConvertView().setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override public void onClick(View v) {
                                            holder.itemView.performClick();
                                        }
                                    });
                            }
                        });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),
                                AttentionAssistMedicineDetailDetailActivity.class);
                            intent.putExtra(
                                AttentionAssistMedicineDetailDetailActivity.EXTRA_ASSIST_MEDICINE_RECORD_INFO,
                                recordBean);
                            startActivityForResult(intent, RESLUT_CODE_DELETE);
                        }
                    });

                    stopMedicie.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            showDataDialog(recordBean.getId());
                        }
                    });
                }
            });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intentNew = new Intent(getActivity(),
                    AttentionAddAssistMedicineActivity.class);
                startActivityForResult(intentNew, 110);
                //startActivityForResult(intentNew, RESLUT_CODE_SAVE);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(resultCode+"---");
        if (resultCode == RESLUT_CODE_SAVE || resultCode == RESLUT_CODE_DELETE) {//删除回掉
            updateList();
        }
    }


    private void updateList() {
        mServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_medicineRecordServerTime,
            SPInfo.ServerTimeDefaultValue);
        patientId = (String) SPUtils.getParams(parentActivity, SPInfo.UserKey_patientId, "",
            SPInfo.SPUserInfo);
        getHttpList(mServerTime, patientId);
    }


    /**
     * 获取网络化疗数据
     */
    private void getHttpList(String serverTime, String patientId) {
        HttpService.getHttpService().getMedicineRecord(serverTime, patientId)
            .compose(RxUtil.<HttpResult<AssistMedicineListBean>>normalSchedulers())
            .map(new HttpResultFunc<AssistMedicineListBean>())
            .subscribe(new Action1<AssistMedicineListBean>() {
                @Override public void call(AssistMedicineListBean bean) {
                    showEmptyLayoutState(LOADING_STATE_SUCESS);

                    if (bean.getSource().size() > 0) {
                        httpList = bean.getSource();
                        SharedPreferences.Editor editor = serverTimeSP.edit();
                        mServerTime = bean.getServerTime() + "";
                        editor.putString(SPInfo.ServerTimeKey_medicineRecordServerTime,
                            mServerTime);
                        editor.commit();
                    }
                    insertDB();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                    insertDB();
                }
            });
    }


    UpdateEndOrDeleteMedicineBody deleteBody;


    // true 代表开始时间
    private void showDataDialog(final int recordId) {
        showDatePicker(getActivity(), new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                stopMedicineRecord(recordId, TimeUtils.changeDateToLong(dateDesc));
            }
        }, getTodayData());
    }


    private void stopMedicineRecord(int id, Long endTime) {
        deleteBody = new UpdateEndOrDeleteMedicineBody(id, endTime);
        HttpService.getHttpService().updateEndMedicine(deleteBody)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    if (httpResult.isResult()) {
                        updateList();
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


    /**
     * 插入化疗数据库数据
     */
    private void insertDB() {

        recordDao = ((MyApplication) (parentActivity.getApplication()))
            .getAttentionDaoSession().getAssistMedicineRecordDao();
        medicineDao = ((MyApplication) (parentActivity.getApplication()))
            .getAttentionDaoSession().getMedicineRecordDao();
        if (httpList.size() > 0) {
            for (int i = 0; i < httpList.size(); i++) {
                //辅助用药入库
                AssistMedicineListBean.MedicineRecordBean record = httpList.get(i);
                long id = record.getId();
                int patientId = record.getPatientId();
                Long startTime = record.getStartTime();
                Long endTime = record.getEndTime();
                boolean isActive = record.isIsActive();
                AssistMedicineRecord dbRecord = new AssistMedicineRecord();

                dbRecord.setId(id);
                dbRecord.setPatientId(patientId + "");
                dbRecord.setStartTime(startTime);
                dbRecord.setEndTime(endTime);
                dbRecord.setIsActive(isActive);

                long count = recordDao.queryBuilder()
                    .where(AssistMedicineRecordDao.Properties.Id.eq(id)).count();
                if (count > 0) {
                    recordDao.update(dbRecord);
                } else {
                    recordDao.insert(dbRecord);
                }

                //用药列表入库
                List<AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean> medicinesList
                    = record.getMedicineRecordDbs();
                for (AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean medcineBean : medicinesList) {
                    int idM = medcineBean.getId();
                    Long startTimeM = medcineBean.getStartTime();
                    Long endTimeM = medcineBean.getEndTime();
                    int patientIdM = medcineBean.getPatientId();
                    int status = medcineBean.getStatus();
                    int medicineId = medcineBean.getMedicineId();
                    int medicineItemId = medcineBean.getMedicineItemId();
                    boolean isActiveM = medcineBean.isActive();
                    String medicineName = medcineBean.getMedicineName();
                    String chemicalName = medcineBean.getChemicalName();

                    MedicineRecord dbRecordMedcine = new MedicineRecord();
                    dbRecordMedcine.setId(idM);
                    dbRecordMedcine.setStartTime(startTimeM);
                    dbRecordMedcine.setEndTime(endTimeM);
                    dbRecordMedcine.setPatientId(patientIdM + "");
                    dbRecordMedcine.setStatus(Long.valueOf(status));
                    dbRecordMedcine.setMedicineId(Long.valueOf(medicineId));
                    dbRecordMedcine.setMedicineItemId(Long.valueOf(medicineItemId));
                    dbRecordMedcine.setMedicineName(medicineName);
                    dbRecordMedcine.setChemicalName(chemicalName);
                    dbRecordMedcine.setIsActive(isActiveM);
                    long countM = medicineDao.queryBuilder()
                        .where(MedicineRecordDao.Properties.Id.eq(idM)).count();
                    if (countM > 0) {
                        medicineDao.update(dbRecordMedcine);
                    } else {
                        medicineDao.insert(dbRecordMedcine);
                    }

                }
            }
        }
        getDBData();
    }


    /**
     * 获取数据库数据
     */
    private void getDBData() {

        Observable.create(new Observable.OnSubscribe<List<AssistMedicineRecord>>() {

            @Override public void call(Subscriber<? super List<AssistMedicineRecord>> subscriber) {

                mDatasRecord.clear();

                List<AssistMedicineRecord> list = recordDao.queryBuilder()
                    .where(AssistMedicineRecordDao.Properties.PatientId.eq(patientId),
                        AssistMedicineRecordDao.Properties.IsActive.eq(true))
                    .orderDesc(AssistMedicineRecordDao.Properties.StartTime)
                    .list();

                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<List<AssistMedicineRecord>, Observable<AssistMedicineRecord>>() {
            @Override
            public Observable<AssistMedicineRecord> call(List<AssistMedicineRecord> assistMedicineRecords) {

                return Observable.from(assistMedicineRecords);
            }
        }).map(new Func1<AssistMedicineRecord, AssistMedicineListBean.MedicineRecordBean>() {
            @Override
            public AssistMedicineListBean.MedicineRecordBean call(AssistMedicineRecord assistRecord) {
                AssistMedicineListBean.MedicineRecordBean bean
                    = new AssistMedicineListBean.MedicineRecordBean();
                List<MedicineRecord> listMedicineDb = medicineDao.queryBuilder()
                    .where(MedicineRecordDao.Properties.PatientId.eq(patientId),
                        MedicineRecordDao.Properties.MedicineItemId.eq(assistRecord.getId())
                        , MedicineRecordDao.Properties.IsActive.eq(true))
                    .orderDesc(MedicineRecordDao.Properties.StartTime)
                    .list();

                List<AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean> listMedicines
                    = new ArrayList<>();
                for (MedicineRecord beanM : listMedicineDb) {
                    AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean item
                        = new AssistMedicineListBean.MedicineRecordBean.MedicineRecordDbsBean(
                        beanM);
                    listMedicines.add(item);
                }
                bean.setEndTime(assistRecord.getEndTime());
                bean.setStartTime(assistRecord.getStartTime());
                bean.setPatientId(Integer.valueOf(assistRecord.getPatientId()));
                bean.setMedicineRecordDbs(listMedicines);
                bean.setId((int) assistRecord.getId());
                return bean;
            }
        }).subscribe(new Subscriber<AssistMedicineListBean.MedicineRecordBean>() {
            @Override public void onCompleted() {
                mAdapter.notifyDataSetChanged();
                if (mDatasRecord.size() == 0) {
                    showEmptyLayoutState(LOADING_STATE_EMPTY);
                }
            }


            @Override public void onError(Throwable e) {
            }


            @Override
            public void onNext(AssistMedicineListBean.MedicineRecordBean medicineRecordBean) {
                mDatasRecord.add(medicineRecordBean);
            }
        });

    }
}
