package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddMedicine;
import com.zhongmeban.attentionmodle.adapter.AttentionMedicineIndexAdapter;
import com.zhongmeban.adapter.OnIndexClickListener;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.postbody.CreateMedicineRecordListBody;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.AttentionMedicine;
import de.greenrobot.dao.Medicine;
import de.greenrobot.dao.MedicineDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关注药品列表Fragment
 * Created by Chengbin He on 2016/7/19.
 */
public class FragmentAttentionMedicineList extends BaseFragment implements View.OnClickListener {

    private ActivityAttentionAddMedicine parentActivity;
    //    private List<Medicine> daoList;//数据库list
    private List<AttentionMedicine> dataList = new ArrayList<AttentionMedicine>();//显示用list
    private ListView listView;
    private AttentionMedicineIndexAdapter mAdapter;
    private TextView overlay;
    private EditText searchBox;//搜索editText
    private ImageView clearBtn;//清除搜索内容
    private ViewGroup emptyView;
    private TextView tvBeginTime;//开始用药时间
    private TextView tvEndTime;//结束用药时间
    private RelativeLayout rlSymptomatic;//对症用药
    private ImageView ivSymptomatic;//对症用药图标
    private TextView tvSymptomatic;//对症用药文字
    private RelativeLayout rlTargete;//靶向用药
    private ImageView ivTargete;//靶向用药图标
    private TextView tvTargete;//靶向用药文字
    private RelativeLayout rlAnalgesia;//镇痛用药
    private ImageView ivAnalgesia;//镇痛用药图标
    private TextView tvAnalgesia;//镇痛用药文字
    private RelativeLayout rlOther;//其他用药
    private ImageView ivOther;//其他用药图标
    private TextView tvOther;//其他用药文字
    private SideLetterBar mLetterBar;//字母索引栏
    private MedicineDao dao;//检查项目对应数据库表
    private TextView tvSave;//完成删选
    private LinearLayout llEndTime;
    private BottomDialog dialog;
    private BottomDialog.Builder builder;
    private int checkPosition = -1;
    private int type;
    private boolean ISSYMPTOMATIC = false;//对症用药是否选择标记
    private boolean ISTARGETE = false;//靶向用药是否选择标记
    private boolean ISANALGESIA = false;//镇痛用药是否选择标记
    private boolean ISOTHER = false;//其他用药是否选择标记
    public List<CreateMedicineRecordListBody> mMedicineRecordList =
            new ArrayList<CreateMedicineRecordListBody>();//上传用List
    private long beginTime;
    private long endTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionAddMedicine) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention_medicine_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMedicineRecordList.clear();
        mMedicineRecordList.addAll(parentActivity.medicineRecordList);
        initTitle(view);
        initChooseDialog();
        initView(view);
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listview);
        getDataFromeDB();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) parentActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        overlay = (TextView) view.findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) view.findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);


        emptyView = (ViewGroup) view.findViewById(R.id.empty_view);
        clearBtn = (ImageView) view.findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
        searchBox = (EditText) view.findViewById(R.id.et_search);
        searchBox.setOnClickListener(this);
    }

    private void initTitle(View view) {
        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
        title.slideCentertext("请选择药品");
        title.slideRighttext("下一步", this);
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentActivity.finish();
                    }
                });
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Medicine>() {
            @Override
            public void call(Subscriber<? super Medicine> subscriber) {
                Log.i("hcb", " db call");
                //获取治疗部分药箱子数据库类容
                dao = ((MyApplication) parentActivity.getApplication()).getDaoSession().getMedicineDao();
                dataList.clear();
                List<Medicine> daoList = dao.queryBuilder().where(MedicineDao.Properties.IsActive.eq(true))
                        .orderAsc(MedicineDao.Properties.PinyinName)
                        .list();
//                //转换数据类型
//                for (Medicine medicine : daoList) {
//                    AttentionMedicine attentionMedicine = new AttentionMedicine(medicine, 0, 0, "");
//                    dataList.add(attentionMedicine);
//                }

                //转换数据类型
                for (Medicine medicine : daoList) {
                    AttentionMedicine attentionMedicine = null;
                    if (containerBean(medicine)){
                        attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
                    }else {
                        attentionMedicine = new AttentionMedicine(medicine, 2, 0, "");
                    }
                    dataList.add(attentionMedicine);
                }


//                ((MyApplication)parentActivity.getApplication()).closeDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Medicine>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " db onCompleted()");
                        mAdapter = new AttentionMedicineIndexAdapter(parentActivity, dataList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                        listView.setAdapter(mAdapter);
                        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                            @Override
                            public void onLetterChanged(String letter) {
                                int position = mAdapter.getLetterPosition(letter);
                                listView.setSelection(position);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " db onCompleted()");
                    }

                    @Override
                    public void onNext(Medicine medicine) {
                        Log.i("hcb", " db onCompleted()");
                    }
                });
    }

    /**
     * 搜索框输入监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String keyword = s.toString();
            if (TextUtils.isEmpty(keyword)) {
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                mLetterBar.setVisibility(View.VISIBLE);
                getDataFromeDB();
            } else {
                clearBtn.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                mLetterBar.setVisibility(View.GONE);
                dao = ((MyApplication) parentActivity.getApplication()).getDaoSession().getMedicineDao();
                List<Medicine> daoList = dao.queryBuilder()
                        .where(MedicineDao.Properties.IsActive.eq(true),
                                dao.queryBuilder().or(MedicineDao.Properties.MedicineName.like("%" + keyword + "%"),
                                        MedicineDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(MedicineDao.Properties.PinyinName)
                        .list();
                //转换数据类型
                dataList.clear();
                for (Medicine medicine : daoList) {
                    AttentionMedicine attentionMedicine = null;
                    if (containerBean(medicine)){
                        attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
                    }else {
                        attentionMedicine = new AttentionMedicine(medicine, 2, 0, "");
                    }
                    dataList.add(attentionMedicine);
                }
                if (daoList == null || daoList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    listView.setAdapter(mAdapter= new AttentionMedicineIndexAdapter(parentActivity, dataList));
                    mAdapter.setIndexClickListener(onIndexClickListener);
                    mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                        @Override
                        public void onLetterChanged(String letter) {
                            int position = mAdapter.getLetterPosition(letter);
                            listView.setSelection(position);
                        }
                    });
                }
            }
        }
    };

    /**
     * ListView 点击事件回调监听
     */
    OnIndexClickListener onIndexClickListener = new OnIndexClickListener() {

        @Override
        public void onNameClick(final int position) {
            checkPosition = position;
            if (dataList.get(position).getCheckMedicine() == 1) {
                new MaterialDialog.Builder(parentActivity)
                        .content("此条记录已被选中，是否删除？")
                        .positiveText("确定")
                        .positiveColor(getResources().getColor(R.color.button_red_normanl))
                        .negativeText("取消")
                        .negativeColor(getResources().getColor(R.color.app_green))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dataList.get(position).setCheckMedicine(2);
                                long medicineId = dataList.get(position).getMedicine().getMedicineId();
                                for (int i = 0; i < mMedicineRecordList.size(); i++) {
                                    if (mMedicineRecordList.get(i).getMedicineId() == medicineId) {
                                        mMedicineRecordList.remove(i);
                                    }
                                }
                                mAdapter.updateData(dataList);
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();

            } else {
                showChooseDialog(position);
            }


        }

        @Override
        public void onLocateClick() {

        }
    };

    private void initChooseDialog() {
        //创建分享Dialog
        builder = new BottomDialog.Builder(parentActivity);
        dialog = new BottomDialog(parentActivity, builder);
    }

    public boolean containerBean(Medicine medicine){
        for (CreateMedicineRecordListBody bean: mMedicineRecordList) {
            if (bean.getMedicineId()==medicine.getMedicineId()){
//                Logger.e("db true"+bean.toString());
                return true;
            }
        }
        return false;
    }

    /***
     * 显示用药信息输入弹框
     *
     * @param position 点击位置
     */
    private void showChooseDialog(final int position) {
        //显示Dialog
        ISSYMPTOMATIC = false;
        ISOTHER = false;
        ISANALGESIA = false;
        ISTARGETE = false;
        type =0;
        beginTime = 0;
        endTime = 0;
        final View bottomDialogView = LayoutInflater.from(parentActivity).inflate(R.layout.attention_medicine_choose_dialog, null);
        rlSymptomatic = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_symptomatic);
        rlSymptomatic.setOnClickListener(this);
        ivSymptomatic = (ImageView) bottomDialogView.findViewById(R.id.iv_symptomatic);
        tvSymptomatic = (TextView) bottomDialogView.findViewById(R.id.tv_symptomatic);

        rlTargete = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_targete);
        rlTargete.setOnClickListener(this);
        ivTargete = (ImageView) bottomDialogView.findViewById(R.id.iv_targete);
        tvTargete = (TextView) bottomDialogView.findViewById(R.id.tv_targete);

        rlAnalgesia = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_analgesia);
        rlAnalgesia.setOnClickListener(this);
        ivAnalgesia = (ImageView) bottomDialogView.findViewById(R.id.iv_analgesia);
        tvAnalgesia = (TextView) bottomDialogView.findViewById(R.id.tv_analgesia);

        rlOther = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_other);
        rlOther.setOnClickListener(this);
        ivOther = (ImageView) bottomDialogView.findViewById(R.id.iv_other);
        tvOther = (TextView) bottomDialogView.findViewById(R.id.tv_other);

        tvSave = (TextView) bottomDialogView.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成确认
                if (beginTime > 0&&type>0) {
                    if (parentActivity.ISNOW){
                        dataList.get(position).setCheckMedicine(1);
                        mAdapter.updateData(dataList);

                        CreateMedicineRecordListBody createMedicineRecordListBody =
                                new CreateMedicineRecordListBody();
                        createMedicineRecordListBody.setPatientId(parentActivity.patientId);
                        createMedicineRecordListBody
                                .setMedicineId((int) dataList.get(position).getMedicine().getMedicineId());
                        createMedicineRecordListBody
                                .setMedicineName(dataList.get(position).getMedicine().getMedicineName());
                        createMedicineRecordListBody.setStartTime(beginTime);
                        createMedicineRecordListBody.setTypes(type);
                        createMedicineRecordListBody.setStatus(1);
                        mMedicineRecordList.add(createMedicineRecordListBody);
                        dialog.dismiss();
                    }else if (!parentActivity.ISNOW&&endTime>0){
                        dataList.get(position).setCheckMedicine(1);
                        mAdapter.updateData(dataList);

                        CreateMedicineRecordListBody createMedicineRecordListBody =
                                new CreateMedicineRecordListBody();
                        createMedicineRecordListBody.setPatientId(parentActivity.patientId);
                        createMedicineRecordListBody
                                .setMedicineId((int) dataList.get(position).getMedicine().getMedicineId());
                        createMedicineRecordListBody
                                .setMedicineName(dataList.get(position).getMedicine().getMedicineName());
                        createMedicineRecordListBody.setStartTime(beginTime);
                        createMedicineRecordListBody.setTypes(type);
                        createMedicineRecordListBody.setEndTime(endTime);
                        createMedicineRecordListBody.setStatus(2);
                        mMedicineRecordList.add(createMedicineRecordListBody);
                        dialog.dismiss();
                    }else {
                        ToastUtil.showText(parentActivity, "请输入结束用药时间");
                    }
                } else {
                    ToastUtil.showText(parentActivity, "请检查用药类型，用药日期");
                }

            }
        });
        builder.setView(bottomDialogView);
        dialog = builder.showDialog();

        //开始时间录入
        tvBeginTime = (TextView) bottomDialogView.findViewById(R.id.tv_begin_time);
        tvBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(bottomDialogView, tvBeginTime, true);
            }

        });


        //结束时间录入
        llEndTime = (LinearLayout) bottomDialogView.findViewById(R.id.ll_end_time);
        if (parentActivity.ISNOW) {
            llEndTime.setVisibility(View.GONE);
        } else {
            llEndTime.setVisibility(View.VISIBLE);
            tvEndTime = (TextView) bottomDialogView.findViewById(R.id.tv_end_time);
            tvEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(bottomDialogView, tvEndTime, false);
                }
            });
        }

    }


    private void showDatePicker(final View parent, final TextView tv, final boolean ISSTART) {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todyDate = df.format(d);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(parentActivity,
                new OnDatePickedListener() {

                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            long millionSeconds = sdf.parse(dateDesc).getTime();
                            if (ISSTART) {
                                tv.setText(year + "年" + month + "月" + day + "日");
                                beginTime = millionSeconds;
                            } else {
                                if (millionSeconds < beginTime) {
                                    Toast.makeText(parentActivity, "开始时间必须小于结束时间，请从新输入"
                                            , Toast.LENGTH_SHORT).show();
                                } else {
                                    tv.setText(year + "年" + month + "月" + day + "日");
                                    endTime = millionSeconds;
                                }

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }).minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose(todyDate) // date chose when init popwindow
                .build();
        int height = pickerPopWin.getContentView().getMeasuredHeight();
        pickerPopWin.showAtLocation(parent, Gravity.BOTTOM, 0, -height);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_search:
                searchBox.addTextChangedListener(textWatcher);
                break;

            case R.id.right_button://右上角保存
                if(mMedicineRecordList.size()>0){
                    parentActivity.medicineRecordList.clear();
                    parentActivity.medicineRecordList.addAll(mMedicineRecordList);
                    parentActivity.startVerifyMedicine();
                }else {
                    ToastUtil.showText(parentActivity,"请选择用药");
                }

                break;

            case R.id.iv_search_clear://搜索框清空按钮
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                break;

            case R.id.rl_symptomatic://对症用药
                if (ISSYMPTOMATIC) {
                    ISSYMPTOMATIC = false;
                    type -= 1;
                    rlSymptomatic.setBackgroundResource(R.drawable.bg_box);
                    ivSymptomatic.setImageResource(R.drawable.attention_symptomatic_medicine);
                    tvSymptomatic.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    type += 1;
                    ISSYMPTOMATIC = true;
                    rlSymptomatic.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivSymptomatic.setImageResource(R.drawable.attention_check_medicine);
                    tvSymptomatic.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_targete://靶向用药
                if (ISTARGETE) {
                    ISTARGETE = false;
                    type -= 2;
                    rlTargete.setBackgroundResource(R.drawable.bg_box);
                    ivTargete.setImageResource(R.drawable.attention_targete_medicine);
                    tvTargete.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISTARGETE = true;
                    type += 2;
                    rlTargete.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivTargete.setImageResource(R.drawable.attention_check_medicine);
                    tvTargete.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_analgesia://镇痛用药
                if (ISANALGESIA) {
                    ISANALGESIA = false;
                    type -= 4;
                    rlAnalgesia.setBackgroundResource(R.drawable.bg_box);
                    ivAnalgesia.setImageResource(R.drawable.attention_analgesia_medicine);
                    tvAnalgesia.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISANALGESIA = true;
                    type += 4;
                    rlAnalgesia.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivAnalgesia.setImageResource(R.drawable.attention_check_medicine);
                    tvAnalgesia.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_other://其他用药
                if (ISOTHER) {
                    ISOTHER = false;
                    type -= 8;
                    rlOther.setBackgroundResource(R.drawable.bg_box);
                    ivOther.setImageResource(R.drawable.attention_other_medicine);
                    tvOther.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISOTHER = true;
                    type += 8;
                    rlOther.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivOther.setImageResource(R.drawable.attention_check_medicine);
                    tvOther.setTextColor(getResources().getColor(R.color.white));
                }
                break;

        }
    }

    public void clearSearche(){
        searchBox.setText("");
    }
}
