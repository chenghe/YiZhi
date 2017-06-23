package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddMedicine;
import com.zhongmeban.attentionmodle.adapter.AttentionVerifyMedicineAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.postbody.CreateMedicineRecordListBody;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 核对清单
 * Created by Chengbin He on 2016/8/5.
 */
public class FragmentAttentionVerifyMedicine extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ActivityAttentionAddMedicine parentActivity;
    private AttentionVerifyMedicineAdapter mAdapter;
    private FloatingActionButton fab;
    private boolean ISSYMPTOMATIC = false;//对症用药是否选择标记
    private boolean ISTARGETE = false;//靶向用药是否选择标记
    private boolean ISANALGESIA = false;//镇痛用药是否选择标记
    private boolean ISOTHER = false;//其他用药是否选择标记
    private TextView tvBeginTime;//开始用药时间
    private TextView tvEndTime;//结束用药时间
    private LinearLayout llEndTime;
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
    private TextView tvSave;//完成删选
    private BottomDialog dialog;
    private BottomDialog.Builder builder;
    private MaterialDialog progressDialog;
    private int bodyType;//每个itembody
    private long bodyStartTime;
    private long bodyEndTime;
    public List<CreateMedicineRecordListBody> mMedicineRecordList =
            new ArrayList<>();//上传用List

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
        View view = inflater.inflate(R.layout.fragment_attention_verify_medicine, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitle(view);
        initView(view);

    }


    private void initView(View view) {
        mMedicineRecordList.clear();
        mMedicineRecordList.addAll(parentActivity.medicineRecordList);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mAdapter = new AttentionVerifyMedicineAdapter(parentActivity, mMedicineRecordList);
        mAdapter.setItemClickListener(itemClickListenter);
        mAdapter.setDeleteOnclickListenter(deleteClickListenter);
        recyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMedicineRecordList.size()>0){
                    parentActivity.medicineRecordList.clear();
                    parentActivity.medicineRecordList.addAll(mMedicineRecordList);
                    progressDialog = showProgressDialog("正在上传数据，请稍后", parentActivity);
                    postHttpData();
                }else {
                    ToastUtil.showText(parentActivity,"请选择用药");
                }

            }
        });
    }

    protected void initTitle(View view) {
        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
        title.slideCentertext("核对清单");
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentActivity.onBackPressed();
                    }
                });
    }


    /**
     * 创建用药记录
     */
    private void postHttpData() {
        //AttentionMedicineRecordsBody body = new AttentionMedicineRecordsBody(parentActivity.medicineRecordList);
        //
        //HttpService.getHttpService().createMedicineRecordList(body, parentActivity.token)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Subscriber<CreateMedicineRecord>() {
        //            @Override
        //            public void onCompleted() {
        //                Log.i("hcb", "FragmentAttentionVerifyMedicine onCompleted");
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                Log.i("hcb", "FragmentAttentionVerifyMedicine onError");
        //                Log.i("hcb", "e" + e);
        //            }
        //
        //            @Override
        //            public void onNext(CreateMedicineRecord createMedicineRecord) {
        //                Log.i("hcb", "FragmentAttentionVerifyMedicine onNext");
        //                if (createMedicineRecord.isResult()) {
        //                    new Handler().postDelayed(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            progressDialog.dismiss();
        //                            Intent intent = new Intent(parentActivity, ActivityAttentionMedicineList.class);
        //                            parentActivity.setResult(200, intent);
        //                            parentActivity.finish();
        //                        }
        //                    }, 1000);
        //                }
        //            }
        //        });
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
        bodyEndTime = 0;
        bodyStartTime = 0;

        final CreateMedicineRecordListBody body = mMedicineRecordList.get(position);
         bodyStartTime = body.getStartTime();
         bodyEndTime = body.getEndTime();
         bodyType = body.getTypes();
        Log.i("hcbtest", "body.getTypes()" + body.getTypes());
        if ((bodyType & 1) != 0) {
            ISSYMPTOMATIC = true;
        }
        if ((bodyType & 2) != 0) {
            ISOTHER = true;
        }
        if ((bodyType & 4) != 0) {
            ISANALGESIA = true;
        }
        if ((bodyType & 8) != 0) {
            ISTARGETE = true;
        }


        builder = new BottomDialog.Builder(parentActivity);
        dialog = new BottomDialog(parentActivity, builder);

        final View bottomDialogView = LayoutInflater.from(parentActivity).inflate(R.layout.attention_medicine_choose_dialog, null);

        initChooseDialogView(bottomDialogView, ISSYMPTOMATIC, ISOTHER, ISANALGESIA, ISTARGETE);
        tvSave = (TextView) bottomDialogView.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("hcbtest","bodyType" +bodyType);
               if (parentActivity.ISNOW){
                   if (bodyStartTime>0&&bodyType>1){
                       body.setStartTime(bodyStartTime);
                       body.setEndTime(bodyEndTime);
                       body.setTypes(bodyType);
                       dialog.dismiss();
                       mAdapter.updateData(mMedicineRecordList);
                   }else {
                       ToastUtil.showText(parentActivity,"请检查信息输入是否正确");
                   }
               }else {
                   if (bodyStartTime>0&&bodyType>1&&bodyEndTime>0&&bodyStartTime<bodyEndTime){
                       body.setStartTime(bodyStartTime);
                       body.setEndTime(bodyEndTime);
                       body.setTypes(bodyType);
                       dialog.dismiss();
                       mAdapter.updateData(mMedicineRecordList);
                   }else {
                       ToastUtil.showText(parentActivity,"请检查信息输入是否正确");
                   }
               }
            }
        });
        builder.setView(bottomDialogView);
        dialog = builder.showDialog();

        //开始时间录入
        tvBeginTime = (TextView) bottomDialogView.findViewById(R.id.tv_begin_time);
        if (bodyStartTime>0){
            tvBeginTime.setText(changeDateToString(bodyStartTime));
        }
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
            if (bodyEndTime>0){
                tvEndTime.setText(changeDateToString(bodyEndTime));
            }
            tvEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(bottomDialogView, tvEndTime, false);
                }
            });
        }

    }

    private void showDatePicker(View parent, final TextView tv, final boolean ISSTART) {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todyDate = df.format(d);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(parentActivity,
                new OnDatePickedListener() {

                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        tv.setText(year + "年" + month + "月" + day + "日");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            long millionSeconds = sdf.parse(dateDesc).getTime();
                            if (ISSTART) {
                                bodyStartTime = millionSeconds;
                            } else {
//                                if (bodyStartTime>0&&bodyEndTime>bodyStartTime){
                                    bodyEndTime = millionSeconds;
//                                }
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

    /**
     * 初始化药品目的布局
     *
     * @param bottomDialogView
     * @param ISSYMPTOMATIC
     * @param ISOTHER
     * @param ISANALGESIA
     * @param ISTARGETE
     */
    private void initChooseDialogView(View bottomDialogView, boolean ISSYMPTOMATIC, boolean ISOTHER,
                                      boolean ISANALGESIA, boolean ISTARGETE) {
        rlSymptomatic = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_symptomatic);
        rlSymptomatic.setOnClickListener(this);
        ivSymptomatic = (ImageView) bottomDialogView.findViewById(R.id.iv_symptomatic);
        tvSymptomatic = (TextView) bottomDialogView.findViewById(R.id.tv_symptomatic);
        if (ISSYMPTOMATIC) {
            rlSymptomatic.setBackgroundColor(getResources().getColor(R.color.app_green));
            ivSymptomatic.setImageResource(R.drawable.attention_check_medicine);
            tvSymptomatic.setTextColor(getResources().getColor(R.color.white));
        }

        rlTargete = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_targete);
        rlTargete.setOnClickListener(this);
        ivTargete = (ImageView) bottomDialogView.findViewById(R.id.iv_targete);
        tvTargete = (TextView) bottomDialogView.findViewById(R.id.tv_targete);
        if (ISOTHER) {
            rlTargete.setBackgroundColor(getResources().getColor(R.color.app_green));
            ivTargete.setImageResource(R.drawable.attention_check_medicine);
            tvTargete.setTextColor(getResources().getColor(R.color.white));
        }

        rlAnalgesia = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_analgesia);
        rlAnalgesia.setOnClickListener(this);
        ivAnalgesia = (ImageView) bottomDialogView.findViewById(R.id.iv_analgesia);
        tvAnalgesia = (TextView) bottomDialogView.findViewById(R.id.tv_analgesia);
        if (ISANALGESIA) {
            rlAnalgesia.setBackgroundColor(getResources().getColor(R.color.app_green));
            ivAnalgesia.setImageResource(R.drawable.attention_check_medicine);
            tvAnalgesia.setTextColor(getResources().getColor(R.color.white));
        }

        rlOther = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_other);
        rlOther.setOnClickListener(this);
        ivOther = (ImageView) bottomDialogView.findViewById(R.id.iv_other);
        tvOther = (TextView) bottomDialogView.findViewById(R.id.tv_other);
        if (ISTARGETE) {
            rlOther.setBackgroundColor(getResources().getColor(R.color.app_green));
            ivOther.setImageResource(R.drawable.attention_check_medicine);
            tvOther.setTextColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * RecyclerView点击事件
     */
    AdapterClickInterface itemClickListenter = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            showChooseDialog(position);
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    /**
     * RecyclerView 删除点击事件
     */
    AdapterClickInterface deleteClickListenter = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, final int position) {
            showDeleteDialog(parentActivity, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    removeItem(position);
                }
            }, "是否要删除这条用药记录？");
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    /**
     * 删除这条用药记录
     *
     * @param position
     */
    private void removeItem(int position) {
        mMedicineRecordList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_symptomatic://对症用药
                if (ISSYMPTOMATIC) {
                    ISSYMPTOMATIC = false;
                    if (bodyType>1){
                        bodyType -= 1;
                    }
                    rlSymptomatic.setBackgroundResource(R.drawable.bg_box);
                    ivSymptomatic.setImageResource(R.drawable.attention_symptomatic_medicine);
                    tvSymptomatic.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISSYMPTOMATIC = true;
                    bodyType += 1;
                    rlSymptomatic.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivSymptomatic.setImageResource(R.drawable.attention_check_medicine);
                    tvSymptomatic.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_targete://靶向用药
                if (ISTARGETE) {
                    ISTARGETE = false;
                    if (bodyType>2){
                        bodyType -= 2;
                    }
                    rlTargete.setBackgroundResource(R.drawable.bg_box);
                    ivTargete.setImageResource(R.drawable.attention_targete_medicine);
                    tvTargete.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISTARGETE = true;
                    bodyType += 2;
                    rlTargete.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivTargete.setImageResource(R.drawable.attention_check_medicine);
                    tvTargete.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_analgesia://镇痛用药
                if (ISANALGESIA) {
                    ISANALGESIA = false;
                    if (bodyType>4){
                        bodyType -= 4;
                    }
                    rlAnalgesia.setBackgroundResource(R.drawable.bg_box);
                    ivAnalgesia.setImageResource(R.drawable.attention_analgesia_medicine);
                    tvAnalgesia.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISANALGESIA = true;
                    bodyType += 4;
                    rlAnalgesia.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivAnalgesia.setImageResource(R.drawable.attention_check_medicine);
                    tvAnalgesia.setTextColor(getResources().getColor(R.color.white));
                }
                break;

            case R.id.rl_other://其他用药
                if (ISOTHER) {
                    ISOTHER = false;
                    if (bodyType>8){
                        bodyType -= 8;
                    }
                    rlOther.setBackgroundResource(R.drawable.bg_box);
                    ivOther.setImageResource(R.drawable.attention_other_medicine);
                    tvOther.setTextColor(getResources().getColor(R.color.content_two));
                } else {
                    ISOTHER = true;
                    bodyType += 8;
                    rlOther.setBackgroundColor(getResources().getColor(R.color.app_green));
                    ivOther.setImageResource(R.drawable.attention_check_medicine);
                    tvOther.setTextColor(getResources().getColor(R.color.white));
                }
                break;
        }
        if (bodyType>15){
            bodyType =15;
        }else if (bodyType<0){
            bodyType = 0;
        }
        Log.i("hcbtest","bodyType" +bodyType);
    }


}

