package com.zhongmeban.attentionmodle.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.AttentionInfoSwitchBean;
import com.zhongmeban.utils.AttentionChatUtil;
import com.zhongmeban.utils.ToastUtil;

import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.RecordIndex;
import de.greenrobot.dao.attention.RecordIndexDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关注图表详情Activity
 * Created by Chengbin He on 2016/9/21.
 */
public class ActivityAttentionChatDetail extends BaseActivity {

    public static final int COLOR_GREEN = Color.parseColor("#58c4a5");
    public static final int COLOR_BLUE = Color.parseColor("#9fb4cc");
    public static final int COLOR_YELLOW = Color.parseColor("#f3b562");
    public static final int[] COLORS = new int[]{COLOR_GREEN, COLOR_BLUE, COLOR_YELLOW};

    private LineChartView chart;
    private LineChartData data;
    private int maxNumberOfLines = 3;//最大曲线个数
    private int numberOfPoints = 1;//最大点个数
    private int numberOfLines = 2;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = false;//是否显示拐点坐标
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;//是否为圆滑曲线
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private Integer[] selectedType = new Integer[]{};//dialog 选中item位置
    private TextView tvSwitch;//筛选
    private TextView tvMarker1;//指标1描述
    private TextView tvMarker2;//指标2描述
    private TextView tvMarker3;//指标3描述
    private String patientId;
    private RecordIndexDao recordIndexDao;
    private List<AttentionInfoSwitchBean> indexList = new ArrayList<>();
    private List<Long> chooseIndexIdList = new ArrayList<>();//选中IndexId
    private List<Long> indexIdList = new ArrayList<>();
    private List<Integer> xpointList = new ArrayList<>();//记录坐标点数
    ;//全部IndexId
    private List<AttentionInfoSwitchBean> chooseIndexList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_chat_detail);
        recordIndexDao = ((MyApplication) getApplication())
                .getAttentionDaoSession().getRecordIndexDao();

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        patientId = sp.getString("patientId", "");

        tvMarker1 = (TextView) findViewById(R.id.tv_marker1);
        tvMarker2 = (TextView) findViewById(R.id.tv_marker2);
        tvMarker3 = (TextView) findViewById(R.id.tv_marker3);

        chart = (LineChartView) findViewById(R.id.chart);
        chart.setScrollEnabled(true);

        chart.setZoomEnabled(true);

        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        //chart.setZoomLevel(0f,0f,15.0f);
        //chart.setMaxZoom(50f);
        chart.setZoomType(ZoomType.HORIZONTAL);

        getDBMaker(patientId);
        //初始化选中全3条
        if (indexIdList.size() >= 3) {
            chooseIndexIdList.add(indexIdList.get(0));
            chooseIndexIdList.add(indexIdList.get(1));
            chooseIndexIdList.add(indexIdList.get(2));

        } else {
            for (int i = 0; i < indexIdList.size(); i++) {
                chooseIndexIdList.add(indexIdList.get(i));
            }
        }

        generateMakerDesc(indexList, indexIdList.size());
        generateChart(chooseIndexIdList);

        tvSwitch = (TextView) findViewById(R.id.tv_switch);
        tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectedType(indexList);
                showMultiChoiceDialog(indexList);
            }
        });

    }


    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 12;
        v.left = 0;
        //横屏最佳点数15个
        if (numberOfPoints > 15) {
            v.right = numberOfPoints + 1;
        } else {
            v.right = 15;
        }

        chart.setMaximumViewport(v);
        final Viewport vCurrent = new Viewport(chart.getMaximumViewport());
        vCurrent.bottom = 0;
        vCurrent.top = 12;
        vCurrent.left = 0;
        vCurrent.right = 15;
        chart.setCurrentViewport(vCurrent);

        chart.setZoomEnabled(false);
    }


    private void getDBMaker(final String patientId) {
        //去重获取指标Id
        indexIdList.clear();
        indexList.clear();
        Cursor c = ((MyApplication) getApplication())
                .getAttentionDaoSession().getDatabase()
                .rawQuery(
                        "select INDEX_ID , INDEX_NAME from RECORD_INDEX where IS_ACTIVE = 1 and PATIENT_ID = "
                                + patientId
                                + " group by INDEX_ID , INDEX_NAME order  by count(*) desc, max(TIME) desc", null);
        try {
            if (c.moveToFirst()) {
                do {
                    indexIdList.add(c.getLong(c.getColumnIndex("INDEX_ID")));
                    int id = (int) c.getLong(c.getColumnIndex("INDEX_ID"));
                    String name = c.getString(c.getColumnIndex("INDEX_NAME"));
                    AttentionInfoSwitchBean bean = new AttentionInfoSwitchBean(id, name);
                    indexList.add(bean);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        Log.i("hcb", "list.size" + indexIdList.size());

    }


    /**
     * 生成底部指标描述
     */
    private void generateMakerDesc(List<AttentionInfoSwitchBean> indexList, int size) {
        switch (size) {
            case 1:
                tvMarker1.setText(indexList.get(0).getName());
                tvMarker2.setText("");
                tvMarker3.setText("");
                break;
            case 2:
                tvMarker1.setText(indexList.get(0).getName());
                tvMarker2.setText(indexList.get(1).getName());
                tvMarker3.setText("");
                break;
            case 3:
                tvMarker1.setText(indexList.get(0).getName());
                tvMarker2.setText(indexList.get(1).getName());
                tvMarker3.setText(indexList.get(2).getName());
                break;
            case 0:
                tvMarker1.setText("");
                tvMarker2.setText("");
                tvMarker3.setText("");
                break;
            default:
                tvMarker1.setText(indexList.get(0).getName());
                tvMarker2.setText(indexList.get(1).getName());
                tvMarker3.setText(indexList.get(2).getName());
                break;
        }
    }


    private void generateChart(List<Long> indexIdList) {
        numberOfPoints = 0;
        //取出指标，并分组
        Map<Long, List<RecordIndex>> indexMap = new HashMap<Long, List<RecordIndex>>();
        List<RecordIndex> indexDose = recordIndexDao.queryBuilder()
                .where(recordIndexDao.queryBuilder().and(RecordIndexDao.Properties.PatientId.eq(patientId),
                        RecordIndexDao.Properties.IsActive.eq(true),
                        RecordIndexDao.Properties.IndexId.in(indexIdList)))
                .orderAsc(RecordIndexDao.Properties.Time)
                .list();
        List<Long> times = new ArrayList<Long>();
        for (RecordIndex index : indexDose) {

            if (indexMap.containsKey(index.getIndexId())) {
                List<RecordIndex> list = indexMap.get(index.getIndexId());
                list.add(index);
            } else {
                List<RecordIndex> list = new ArrayList<RecordIndex>();
                indexMap.put(index.getIndexId(), list);
                list.add(index);
            }

            if (!times.contains(index.getTime())) {
                times.add(index.getTime());
            }
        }

        //生成坐标轴
        Axis axisX = new Axis();

        List<AxisValue> xList = new ArrayList<AxisValue>();
        Map<Long, Float> xMap = new HashMap<>();//生成坐标字典
        for (int i = 1; i <= times.size(); i++) {
            AxisValue value = new AxisValue(2 * i - 1);
            value.setLabel(changeDateToPointString(times.get(i - 1)));
            xList.add(value);
            xMap.put(times.get(i - 1), (float) (2 * i - 1));
        }
        axisX.setValues(xList);

        Axis axisY = new Axis();
        List<AxisValue> yList = new ArrayList<AxisValue>();
        for (int i = 0; i < 11; i++) {
            //生成10个坐标点
            AxisValue value = new AxisValue(i);
            value.setLabel(30 * i - 100 + "");
            yList.add(value);
        }
        axisY.setValues(yList);
        axisY.setTextSize(8);
        axisY.setName("百分比");
        axisY.setHasLines(false);
        axisY.setTextColor(getResources().getColor(R.color.text_one));

        //生成坐标点
        List<Line> lines = new ArrayList<Line>();

        for (int i = 0; i < indexIdList.size(); i++) {
            List<RecordIndex> list = indexMap.get(indexIdList.get(i));
            List<PointValue> values = new ArrayList<PointValue>();
            numberOfPoints += list.size();
            Log.i("hcb", "numberOfPoints" + numberOfPoints);
            for (RecordIndex index : list) {
                //生成横坐标
                long time = index.getTime();
                float x = xMap.get(time);
                //生成纵坐标
                float value = Float.parseFloat(index.getValue());
                float normal=0;
                if (index.getNormalMax()!=null && index.getNormalMax()!=0){
                    normal = index.getNormalMax();
                }
                float y = AttentionChatUtil.changeValueToY(value, normal);
                Log.i("hcb", "x" + x);
                Log.i("hcb", "y" + y);
                values.add(new PointValue(x, y));
            }

            Line line = new Line(values);
            line.setColor(COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(hasLines);
            line.setHasPoints(true);//显示点
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }
        data = new LineChartData(lines);
        //        if (hasAxesNames) {
        //            axisX.setName("Axis X");
        //            axisY.setName("Axis Y");
        //        }
        axisX.setTextColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

        resetViewport();
    }


    /**
     * 获取标志物本地数据库数据
     */
    private void getDBMarkerIndexData(final String patientId) {
        Observable
                .create(new Observable.OnSubscribe<MarkerSource>() {
                            @Override
                            public void call(Subscriber<? super MarkerSource> subscriber) {
                                Log.i("hcb", "call");
                                RecordIndexDao recordIndexDao = ((MyApplication) getApplication())
                                        .getAttentionDaoSession().getRecordIndexDao();

                                //去重获取指标Id
                                List<Long> indexIdList = new ArrayList<Long>();
                                Cursor c = ((MyApplication) getApplication())
                                        .getAttentionDaoSession().getDatabase()
                                        .rawQuery(
                                                "select distinct INDEX_ID from RECORD_INDEX where IS_ACTIVE = 1 and PATIENT_ID = "
                                                        + patientId
                                                        + " group by INDEX_ID order by max(TIME) desc limit 3", null);
                                try {
                                    if (c.moveToFirst()) {
                                        do {
                                            indexIdList.add(c.getLong(c.getColumnIndex("INDEX_ID")));
                                        } while (c.moveToNext());
                                    }
                                } finally {
                                    c.close();
                                }
                                Log.i("hcb", "list.size" + indexIdList.size());

                                //取出指标，并分组
                                Map<Long, List<RecordIndex>> indexMap = new HashMap<Long, List<RecordIndex>>();
                                List<RecordIndex> indexDose = recordIndexDao.queryBuilder()
                                        .where(recordIndexDao.queryBuilder()
                                                .and(RecordIndexDao.Properties.PatientId.eq(patientId),
                                                        RecordIndexDao.Properties.IsActive.eq(true),
                                                        RecordIndexDao.Properties.IndexId.in(indexIdList)))
                                        .orderAsc(RecordIndexDao.Properties.Time)
                                        .list();
                                List<Long> times = new ArrayList<Long>();
                                for (RecordIndex index : indexDose) {

                                    if (indexMap.containsKey(index.getIndexId())) {
                                        List<RecordIndex> list = indexMap.get(index.getIndexId());
                                        list.add(index);
                                    } else {
                                        List<RecordIndex> list = new ArrayList<RecordIndex>();
                                        indexMap.put(index.getIndexId(), list);
                                        list.add(index);
                                    }

                                    if (!times.contains(index.getTime())) {
                                        times.add(index.getTime());
                                    }
                                }

                                //生成坐标轴
                                Axis axisX = new Axis();

                                List<AxisValue> xList = new ArrayList<AxisValue>();
                                Map<Long, Float> xMap = new HashMap<>();//生成坐标字典
                                for (int i = 1; i <= times.size(); i++) {
                                    AxisValue value = new AxisValue(2 * i - 1);
                                    value.setLabel(changeDateToPointString(times.get(i - 1)));
                                    xList.add(value);
                                    xMap.put(times.get(i - 1), (float) (2 * i - 1));
                                }
                                axisX.setValues(xList);
                                Axis axisY = new Axis();

                                //生成坐标点
                                List<Line> lines = new ArrayList<Line>();
                                for (int i = 0; i < indexIdList.size(); i++) {
                                    List<RecordIndex> list = indexMap.get(indexIdList.get(i));
                                    List<PointValue> values = new ArrayList<PointValue>();
                                    if (list.size() > 1) {//防止只显示一个点
                                        for (RecordIndex index : list) {
                                            //生成横坐标
                                            long time = index.getTime();
                                            float x = xMap.get(time);
                                            //生成纵坐标
                                            float value = Float.parseFloat(index.getValue());
                                            float y = AttentionChatUtil.changeValueToY(value, 50);
                                            Log.i("hcb", "x" + x);
                                            Log.i("hcb", "y" + y);
                                            values.add(new PointValue(x, y));
                                        }
                                        Line line = new Line(values);
                                        line.setColor(ChartUtils.COLORS[i]);
                                        line.setShape(shape);
                                        line.setCubic(isCubic);
                                        line.setFilled(isFilled);
                                        line.setHasLabels(hasLabels);
                                        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                                        line.setHasLines(hasLines);
                                        line.setHasPoints(true);
                                        if (pointsHaveDifferentColor) {
                                            line.setPointColor(
                                                    ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                                        }
                                        lines.add(line);
                                    }

                                }

                                data = new LineChartData(lines);

                                if (hasAxesNames) {
                                    axisX.setName("Axis X");
                                    axisY.setName("Axis Y");
                                }
                                data.setAxisXBottom(axisX);
                                data.setAxisYLeft(axisY);

                                data.setBaseValue(Float.NEGATIVE_INFINITY);
                                chart.setLineChartData(data);

                                subscriber.onCompleted();
                            }
                        }
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MarkerSource>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getMarkerIndexBDData onCompleted");
                        //                        generateValues();
                        //                        generateData(times);
                        resetViewport();

                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getMarkerIndexBDData onError");
                        Log.i("hcb", "e" + e);
                    }


                    @Override
                    public void onNext(MarkerSource markerSource) {
                        Log.i("hcb", "getMarkerIndexBDData onNext");
                    }
                });
    }

    /**
     * 初始化筛选内容
     *
     * @param list
     */
    private void initSelectedType(List<AttentionInfoSwitchBean> list) {
        if (selectedType.length > 0 && selectedType[0] > -1) {

        } else {
            switch (list.size()) {
                case 0:
                    selectedType = new Integer[]{-1};
                    break;
                case 1:
                    selectedType = new Integer[]{0};
                    break;
                case 2:
                    selectedType = new Integer[]{0, 1};
                    break;
                default://大于等于3个选择前三个
                    selectedType = new Integer[]{0, 1, 2};
                    break;
            }
        }

    }


    /**
     * 详细记录删选
     */
    private void showMultiChoiceDialog(List<AttentionInfoSwitchBean> list) {
        Log.i("hcb", "showMultiChoiceDialog list" + list.size());

        new MaterialDialog.Builder(this)
                .title("请选择筛选内容")
                .items(list)
                .itemsCallbackMultiChoice(selectedType, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        return true; // allow selection
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .autoDismiss(false)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i("hcb",
                                "dialog.getSelectedIndices().length" + dialog.getSelectedIndices().length);
                        if (dialog.getSelectedIndices().length > 0) {
                            //判断是否选中
                            if (dialog.getSelectedIndices().length > 3) {
                                ToastUtil.showText(ActivityAttentionChatDetail.this,
                                        "筛选内容不能超过3个，请重新选择");
                            } else {
                                dialog.getSelectedIndices().toString();
                                chooseIndexIdList.clear();
                                chooseIndexList.clear();
                                for (int i = 0; i < dialog.getSelectedIndices().length; i++) {
                                    chooseIndexIdList.add(
                                            indexIdList.get(dialog.getSelectedIndices()[i]));
                                    chooseIndexList.add(indexList.get(dialog.getSelectedIndices()[i]));
                                }
                                selectedType = dialog.getSelectedIndices();
                                generateChart(chooseIndexIdList);
                                generateMakerDesc(chooseIndexList, chooseIndexList.size());
                                dialog.dismiss();
                            }
                        } else {
                            ToastUtil.showText(ActivityAttentionChatDetail.this, "请选择筛选内容");
                        }

                    }
                })
                .show();

    }


    @Override
    protected void initTitle() {

    }
}
