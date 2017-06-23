//package com.zhongmeban.view;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.zhongmeban.R;
//import com.zhongmeban.bean.postbody.CreateMedicineRecordListBody;
//import com.zhongmeban.utils.TimeUtils;
//import com.zhongmeban.view.BottomDialog.BottomDialog;
//import com.zhongmeban.view.DatePicker.DatePickerPopWin;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Created by Chengbin He on 2016/10/18.
// */
//
//public class AttentionChooseMedicineDialog {
//    private static boolean ISSYMPTOMATIC;
//    private static boolean ISOTHER;
//    private static boolean ISANALGESIA;
//    private static boolean ISTARGETE;
//    private static long bodyStartTime;
//    private static long bodyEndTime;
//    private static int type;
//    private static RelativeLayout rlSymptomatic;
//    private static ImageView ivSymptomatic;
//    private static TextView tvSymptomatic;
//    private static RelativeLayout rlTargete;
//    private static ImageView ivTargete;
//    private static TextView tvTargete;
//    private static RelativeLayout rlAnalgesia;
//    private static ImageView ivAnalgesia;
//    private static TextView tvAnalgesia;
//    private static RelativeLayout rlOther;
//    private static ImageView ivOther;
//    private static TextView tvOther;
//    private static CreateMedicineRecordListBody mBody;
//    private static Context parentActivity;
//
//
//    private static void initData(CreateMedicineRecordListBody body) {
//        bodyStartTime = body.getStartTime();
//        bodyEndTime = body.getEndTime();
//        type = body.getTypes();
//        Log.i("hcb", "body.getTypes()" + body.getTypes());
//        if ((type & 1) != 0) {
//            ISSYMPTOMATIC = true;
//        }
//        if ((type & 2) != 0) {
//            ISOTHER = true;
//        }
//        if ((type & 4) != 0) {
//            ISANALGESIA = true;
//        }
//        if ((type & 8) != 0) {
//            ISTARGETE = true;
//        }
//
//        Log.i("hcb", "body.getTypes()" + body.getTypes());
//    }
//
//    /**
//     * 初始化药品目的布局
//     *
//     * @param bottomDialogView
//     * @param ISSYMPTOMATIC
//     * @param ISOTHER
//     * @param ISANALGESIA
//     * @param ISTARGETE
//     */
//    private static void initChooseDialogView(View bottomDialogView, boolean ISSYMPTOMATIC, boolean ISOTHER,
//                                      boolean ISANALGESIA, boolean ISTARGETE) {
//        rlSymptomatic = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_symptomatic);
//        rlSymptomatic.setOnClickListener(onClickListener);
//        ivSymptomatic = (ImageView) bottomDialogView.findViewById(R.id.iv_symptomatic);
//        tvSymptomatic = (TextView) bottomDialogView.findViewById(R.id.tv_symptomatic);
//        if (ISSYMPTOMATIC) {
//            rlSymptomatic.setBackgroundColor(parentActivity.getResources().getColor(R.color.app_green));
//            ivSymptomatic.setImageResource(R.drawable.attention_check_medicine);
//            tvSymptomatic.setTextColor(parentActivity.getResources().getColor(R.color.white));
//        }
//
//        rlTargete = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_targete);
//        rlTargete.setOnClickListener(onClickListener);
//        ivTargete = (ImageView) bottomDialogView.findViewById(R.id.iv_targete);
//        tvTargete = (TextView) bottomDialogView.findViewById(R.id.tv_targete);
//        if (ISOTHER) {
//            rlTargete.setBackgroundColor(parentActivity.getResources().getColor(R.color.app_green));
//            ivTargete.setImageResource(R.drawable.attention_check_medicine);
//            tvTargete.setTextColor(parentActivity.getResources().getColor(R.color.white));
//        }
//
//        rlAnalgesia = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_analgesia);
//        rlAnalgesia.setOnClickListener(onClickListener);
//        ivAnalgesia = (ImageView) bottomDialogView.findViewById(R.id.iv_analgesia);
//        tvAnalgesia = (TextView) bottomDialogView.findViewById(R.id.tv_analgesia);
//        if (ISANALGESIA) {
//            rlAnalgesia.setBackgroundColor(parentActivity.getResources().getColor(R.color.app_green));
//            ivAnalgesia.setImageResource(R.drawable.attention_check_medicine);
//            tvAnalgesia.setTextColor(parentActivity.getResources().getColor(R.color.white));
//        }
//
//        rlOther = (RelativeLayout) bottomDialogView.findViewById(R.id.rl_other);
//        rlOther.setOnClickListener(onClickListener);
//        ivOther = (ImageView) bottomDialogView.findViewById(R.id.iv_other);
//        tvOther = (TextView) bottomDialogView.findViewById(R.id.tv_other);
//        if (ISTARGETE) {
//            rlOther.setBackgroundColor(parentActivity.getResources().getColor(R.color.app_green));
//            ivOther.setImageResource(R.drawable.attention_check_medicine);
//            tvOther.setTextColor(parentActivity.getResources().getColor(R.color.white));
//        }
//    }
//
//    static View.OnClickListener  onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };
//
//    /***
//     * 显示用药信息输入弹框
//     */
//    private static CreateMedicineRecordListBody showChooseDialog(CreateMedicineRecordListBody body, final Context parentActivity, final boolean ISNOW) {
//        mBody = body;
//
//        initData(mBody);
//
//
//        BottomDialog.Builder builder = new BottomDialog.Builder(parentActivity);
//        BottomDialog dialog = new BottomDialog(parentActivity, builder);
//
//        final View bottomDialogView = LayoutInflater.from(parentActivity).inflate(R.layout.attention_medicine_choose_dialog, null);
//
//        initChooseDialogView(bottomDialogView, ISSYMPTOMATIC, ISOTHER, ISANALGESIA, ISTARGETE);
//        TextView tvSave = (TextView) bottomDialogView.findViewById(R.id.tv_save);
//        tvSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CreateMedicineRecordListBody createMedicineRecordListBody = new CreateMedicineRecordListBody();
//                if (ISNOW) {
//                    createMedicineRecordListBody.setEndTime(endTime);
//                    createMedicineRecordListBody.setStatus(2);
//                } else {
//                    createMedicineRecordListBody.setStatus(1);
//                }
//                createMedicineRecordListBody.setStartTime(beginTime);
//
//                dialog.dismiss();
//                mAdapter.updateData(mMedicineRecordList);
//
//                return mBody;
//            }
//        });
//        builder.setView(bottomDialogView);
//        dialog = builder.showDialog();
//
//        //开始时间录入
//        TextView  tvBeginTime = (TextView) bottomDialogView.findViewById(R.id.tv_begin_time);
//        if (bodyStartTime > 0) {
//            tvBeginTime.setText(TimeUtils.changeDateToString(bodyStartTime));
//        }
//        tvBeginTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePicker(bottomDialogView, tvBeginTime, true);
//            }
//
//        });
//
//
//        //结束时间录入
//        LinearLayout llEndTime = (LinearLayout) bottomDialogView.findViewById(R.id.ll_end_time);
//
//        if (ISNOW) {
//            llEndTime.setVisibility(View.GONE);
//        } else {
//            llEndTime.setVisibility(View.VISIBLE);
//            TextView tvEndTime = (TextView) bottomDialogView.findViewById(R.id.tv_end_time);
//            if (bodyEndTime > 0) {
//                tvEndTime.setText(TimeUtils.changeDateToString(bodyStartTime));
//            }
//            tvEndTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showDatePicker(bottomDialogView, tvEndTime, false);
//                }
//            });
//        }
//
//        return mBody;
//    }
//
//    private static void showDatePicker(View parent, final TextView tv, final boolean ISSTART) {
//        Date d = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String todyDate = df.format(d);
//        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(parentActivity,
//                new OnDatePickedListener() {
//
//                    @Override
//                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                        tv.setText(year + "年" + month + "月" + day + "日");
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        try {
//                            long millionSeconds = sdf.parse(dateDesc).getTime();
//                            if (ISSTART) {
//                                beginTime = millionSeconds;
//                            } else {
//                                endTime = millionSeconds;
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).minYear(1990) //min year in loop
//                .maxYear(2550) // max year in loop
//                .dateChose(todyDate) // date chose when init popwindow
//                .build();
//        int height = pickerPopWin.getContentView().getMeasuredHeight();
//        pickerPopWin.showAtLocation(parent, Gravity.BOTTOM, 0, -height);
//    }
//}
