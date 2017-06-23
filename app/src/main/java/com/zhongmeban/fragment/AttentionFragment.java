package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActHome;
import com.zhongmeban.activity.ActivityAddAttention;
import com.zhongmeban.activity.ActivityCardLogin;
import com.zhongmeban.adapter.FragAttentionGridAdapter;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionChatDetail;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionNoticePop;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionNoticesList;
import com.zhongmeban.attentionmodle.adapter.AttentionInfoTimeAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.AttentionInfoSwitchBean;
import com.zhongmeban.bean.AttentionNoticesBean;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.IndexRecords;
import com.zhongmeban.bean.PatientList;
import com.zhongmeban.bean.RecordlistBean;
import com.zhongmeban.bean.postbody.DeletePatientBody;
import com.zhongmeban.bean.postbody.RecordListBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.AttentionChatUtil;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.CircleTransform;
import com.zhongmeban.utils.CircularProgressBar.CircularProgressBar;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.DividerGridItemDecoration;
import com.zhongmeban.view.ScrollLinearLayout;
import de.greenrobot.dao.attention.AttentionNotices;
import de.greenrobot.dao.attention.AttentionNoticesDao;
import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.Patient;
import de.greenrobot.dao.attention.PatientDao;
import de.greenrobot.dao.attention.RecordIndex;
import de.greenrobot.dao.attention.RecordIndexDao;
import de.greenrobot.dao.attention.RecordIndexItem;
import de.greenrobot.dao.attention.RecordIndexItemDao;
import java.lang.reflect.Method;
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

import static android.app.Activity.RESULT_OK;

/**
 * 关注页面
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener {

    private ActHome parentActivity;
    private RelativeLayout rlNoneAttention;//无关注记录界面
    private RelativeLayout rlAlter;//意识注意事项
    private ObservableScrollView obContent;//有记录内容页
    private TextView tvAdd;//新增按钮
    private TextView tvSwitch;//详细记录中排序按钮
    private TextView tvName;//患者姓名
    private TextView tvAge;//患者年龄
    private TextView tvDiagnosis;//确诊标题
    private TextView tvReView;//复查标题
    private TextView tvPill;//服药标题
    private TextView tvNoneInfo;//无详细记录
    private TextView tvAddAttention;
    private ImageView ivMore;
    private ImageView ivIcon;//用户头像
    private ImageView ivAlter;
    private RecyclerView gridRecyclerview;
    private FragAttentionGridAdapter fragAttentionGridAdapter;
    private LineChartView chart;
    private LineChartData data;
    private int maxNumberOfLines = 3;//最大曲线个数
    private int numberOfPoints = 20;//最大点个数
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
    private ScrollLinearLayout scrollLinearLayout;//详细记录
    private boolean ISBYTYPE = true;
    public static boolean LATER = false;//稍后查看
    private boolean UPDATEPATION = true;//正常更新标记
    private long recentlyCheck;//最近检查时间
    //    private LinearLayout infoByTypeContent;
    private SharedPreferences userInfoSP;
    private SharedPreferences serverTimeSP;
    private String pationListServerTime;//关注列表servertime
    private String attentionNoticesServerTime;//异常提示servertime
    private String markerServerTime;//标志物servertime
    private String token;
    private String userId;
    private String patientId;
    private String stopTitle = "";//暂停更新标题
    private Patient patient;
    private List<AttentionNotices> attentionNoticeDBList = new ArrayList<>();
    private RecordListBody recordListBody = new RecordListBody();
    private List<AttentionInfoSwitchBean> switchList = new ArrayList<AttentionInfoSwitchBean>();
    private List<Integer> recordTypesList = new ArrayList<Integer>();//上传筛选信息List
    private Integer[] selectedType = new Integer[] {};
    private List<Long> times = new ArrayList<>();//横坐标时间
    private Map<Long, List<RecordIndex>> indexMap = new HashMap<>();
    private List<Long> indexIdList = new ArrayList<Long>();
    ;//图表查询指标ID
    private TextView tvMarker1;//指标1描述
    private TextView tvMarker2;//指标2描述
    private TextView tvMarker3;//指标3描述
    private CircularProgressBar progressBar;
    private List<String> indexNameList = new ArrayList<String>();//指标名称List
    private int pageIndex = 1;//首页关注分页
    private List<RecordlistBean.SourceItems> infoList = new ArrayList<>();//详细记录List
    private int takingMedicine;
    private int totalPage;
    //    public Bitmap iconPhoto;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActHome) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hcb", "AttentionFragment onCreateView start");
        View view = inflater.inflate(R.layout.frag_attention, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("hcb", "AttentionFragment onViewCreated start");

        initData();
        initView(view);

        //        getHttpPatientList(pationListServerTime, userId, token);
    }


    private void initData() {
        userInfoSP = parentActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = userInfoSP.getString("token", "");
        userId = userInfoSP.getString("userId", "");
        UPDATEPATION = userInfoSP.getBoolean("UPDATEPATION", true);
        recentlyCheck = userInfoSP.getLong("recentlyCheck", 0);
        takingMedicine = userInfoSP.getInt("takingMedicine", 0);

        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        pationListServerTime = serverTimeSP.getString("pationListServerTime", "0");
        attentionNoticesServerTime = serverTimeSP.getString("attentionNoticesServerTime", "0");
        markerServerTime = serverTimeSP.getString("markerServerTime", "0");

        //初始化筛选Dailog内容
        switchList.add(new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.INDEX_RECORD, "标志物记录"));
        switchList.add(
            new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.MEDICINE_RECORD, "用药记录"));
        switchList.add(
            new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.HOSPITAL_RECORD, "住院记录"));
        switchList.add(
            new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.SURGERY_RECORD, "手术记录"));
        switchList.add(
            new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.RADIOTHERAPY_RECORD, "放疗记录"));
        //        switchList.add(new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.RADIOTHERAPY_SUSPENDED_RECORD, "放疗暂停记录"));
        switchList.add(
            new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.CHEMOTHERAPY_RECORD, "化疗记录"));
        //        switchList.add(new AttentionInfoSwitchBean(AttentionInfoTimeAdapter.CHEMOTHERAPY_COURSE_RECORD, "化疗疗程记录"));
    }


    private void initView(final View view) {

        tvNoneInfo = (TextView) view.findViewById(R.id.tv_no_info);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        ivAlter = (ImageView) view.findViewById(R.id.iv_alert);
        tvAddAttention = (TextView) view.findViewById(R.id.tv_add_attention);
        tvAddAttention.setOnClickListener(this);
        tvMarker1 = (TextView) view.findViewById(R.id.tv_marker1);
        tvMarker2 = (TextView) view.findViewById(R.id.tv_marker2);
        tvMarker3 = (TextView) view.findViewById(R.id.tv_marker3);
        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        rlAlter = (RelativeLayout) view.findViewById(R.id.rl_alert);
        rlAlter.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvDiagnosis = (TextView) view.findViewById(R.id.tv_diagnosis);
        tvReView = (TextView) view.findViewById(R.id.tv_review);
        if (recentlyCheck > 0) {
            tvReView.setText("最近一次检查" + changeDateToString(recentlyCheck));
        }
        tvPill = (TextView) view.findViewById(R.id.tv_pill);
        tvPill.setText("正在服用" + takingMedicine + "种药物");

        rlNoneAttention = (RelativeLayout) view.findViewById(R.id.rl_none_attention);
        obContent = (ObservableScrollView) view.findViewById(R.id.sv_content);
        obContent.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                Log.i("hcbtest", "firstScroll" + firstScroll);
                Log.i("hcbtest", "dragging" + dragging);
                if (firstScroll && obContent.getChildAt(0).getMeasuredHeight() <=
                    scrollY + view.getHeight()) {//判断是否滑动到底部
                    if (totalPage > 0 && pageIndex < totalPage) {
                        pageIndex++;
                        progressBar.setVisibility(View.VISIBLE);
                        Log.i("hcbtest", "onScrollChanged");
                        Log.i("hcbtest", "pageIndex" + pageIndex);
                        recordListBody.setPageIndex(pageIndex);
                        getHttpRecordList(recordListBody, token);
                    }

                }
            }


            @Override
            public void onDownMotionEvent() {

            }


            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        tvAdd = (TextView) view.findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(token)) {//判断是否登录
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        parentActivity.getWindow().setExitTransition(null);
                        parentActivity.getWindow().setEnterTransition(null);

                    }
                    startActivityForResult(new Intent(parentActivity, ActivityCardLogin.class), 1);
                } else {
                    Intent intent = new Intent(getActivity(), ActivityAddAttention.class);
                    intent.putExtra("ISADD", true);
                    startActivityForResult(intent, 1);
                }

            }
        });

        ivMore = (ImageView) view.findViewById(R.id.iv_more);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), ivMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_attention, popupMenu.getMenu());
                Menu menu = popupMenu.getMenu();
                MenuItem stopUpdateMenuItem = menu.findItem(R.id.stop);

                if (UPDATEPATION) {
                    stopTitle = "暂停更新";
                    stopUpdateMenuItem.setIcon(R.drawable.menu_icon_pause);
                } else {
                    stopTitle = "启用更新";
                    stopUpdateMenuItem.setIcon(R.drawable.menu_icon_start);
                }
                stopUpdateMenuItem.setTitle(stopTitle);

                if (popupMenu.getMenu().getClass().getSimpleName().equals("MenuBuilder")) {
                    try {
                        Method m = popupMenu.getMenu()
                            .getClass()
                            .getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                        m.setAccessible(true);
                        m.invoke(menu, true);
                        popupMenu.setOnMenuItemClickListener(
                            new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.change:
                                            Intent intent = new Intent(parentActivity,
                                                ActivityAddAttention.class);
                                            intent.putExtra("ISADD", false);
                                            intent.putExtra("patientId", patientId);
                                            //                                        intent.putExtra("photo",iconPhoto);
                                            startActivityForResult(intent, 1);
                                            break;
                                        case R.id.remove:
                                            showDeleteDialog(parentActivity,
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(
                                                        @NonNull MaterialDialog dialog,
                                                        @NonNull DialogAction which) {
                                                        deleteHttpPatient(patientId, token);
                                                    }
                                                }, "确定删除关注记录？");
                                            break;
                                        case R.id.stop:
                                            showDeleteDialog(parentActivity,
                                                new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(
                                                        @NonNull MaterialDialog dialog,
                                                        @NonNull DialogAction which) {
                                                        UPDATEPATION = !UPDATEPATION;
                                                        userInfoSP.edit()
                                                            .putBoolean("UPDATEPATION",
                                                                UPDATEPATION)
                                                            .commit();
                                                    }
                                                }, "确定" + stopTitle + "？");
                                            break;
                                    }

                                    return true;
                                }
                            });
                        popupMenu.show();
                    } catch (NoSuchMethodException e) {
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

        gridRecyclerview = (RecyclerView) view.findViewById(R.id.grid_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3) {
            @Override public boolean canScrollVertically() {
                return false;
            }
        };
        gridRecyclerview.setLayoutManager(gridLayoutManager);
        gridRecyclerview.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        fragAttentionGridAdapter = new FragAttentionGridAdapter(getActivity());
        fragAttentionGridAdapter.setItemClickListener(adapterListener);
        gridRecyclerview.setAdapter(fragAttentionGridAdapter);

        chart = (LineChartView) view.findViewById(R.id.chart);
        chart.setScrollEnabled(true);
        chart.setOnClickListener(this);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setZoomType(ZoomType.HORIZONTAL);

        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        lastY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = event.getX() - lastX;
                        offsetY = event.getY() - lastY;

                        Logger.d("x==" + Math.abs(offsetX) + "==Y==" + Math.abs(offsetY));
                        if (Math.abs(offsetY) > Math.abs(offsetX)) {
                            chart.getParent().requestDisallowInterceptTouchEvent(false);
                            return false;
                        } else {
                            chart.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        }

                        //break;
                    case MotionEvent.ACTION_UP:

                        //chart.getParent().requestDisallowInterceptTouchEvent(true);
                        if (Math.abs(offsetX) < 4 && Math.abs(offsetY) < 4) {
                            offsetX = 0;
                            offsetY = 0;
                            return false;
                        } else {
                            offsetY = 0;
                            offsetX = 0;
                            return true;
                        }
                }

                return false;
            }
        });

        tvSwitch = (TextView) view.findViewById(R.id.tv_switch);
        tvSwitch.setOnClickListener(this);

        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.scll_info_time);

    }


    float lastX;
    float lastY;
    float offsetX;
    float offsetY;


    @Override
    public void onResume() {
        super.onResume();
        Log.i("hcb", "AttentionFragment onResume start");
        infoList.clear();
        pageIndex = 1;
        if (TextUtils.isEmpty(token)) {//未登录过
            obContent.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);
            rlNoneAttention.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.VISIBLE);
        } else {
            //登录过,请求关注列表接口
            //            LATER = false;
            recentlyCheck = userInfoSP.getLong("recentlyCheck", 0);
            takingMedicine = userInfoSP.getInt("takingMedicine", 0);
            attentionNoticesServerTime = serverTimeSP.getString("attentionNoticesServerTime", "0");
            tvPill.setText("正在服用" + takingMedicine + "种药物");
            if (recentlyCheck > 0) {
                tvReView.setText("最近一次检查" + changeDateToString(recentlyCheck));
            }

            if (android.os.Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                //5.0以下singleTask onActivityResult BUG
                boolean markerBack = userInfoSP.getBoolean(SPInfo.UserKey_markerListBack,false);
                if (markerBack){
                    pationListServerTime = serverTimeSP.getString("pationListServerTime", "0");
                    pageIndex = 1;
                    LATER = false;
                    userInfoSP.edit().putBoolean(SPInfo.UserKey_markerListBack,false).apply();
                    getHttpPatientList(pationListServerTime, userId, token);
                }
            }

            //            getHttpPatientList(pationListServerTime, userId, token);
        }

    }


    /**
     * Activity 切换时调用
     */
    public void getHttpData(ActHome parentActivity) {
        this.parentActivity = parentActivity;
        infoList.clear();
        pageIndex = 1;
        userInfoSP = parentActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        token = userInfoSP.getString("token", "");
        userId = userInfoSP.getString("userId", "");
        UPDATEPATION = userInfoSP.getBoolean("UPDATEPATION", true);
        recentlyCheck = userInfoSP.getLong("recentlyCheck", 0);
        takingMedicine = userInfoSP.getInt("takingMedicine", 0);
        markerServerTime = serverTimeSP.getString("markerServerTime", "0");
        Log.i("hcbtest", "markerServerTime" + markerServerTime);

        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        pationListServerTime = serverTimeSP.getString("pationListServerTime", "0");
        attentionNoticesServerTime = serverTimeSP.getString("attentionNoticesServerTime", "0");
        if (TextUtils.isEmpty(token)) {//未登录过
            if (!(obContent == null)) {
                obContent.setVisibility(View.GONE);
                ivMore.setVisibility(View.GONE);
                rlNoneAttention.setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.VISIBLE);
                tvName.setText("");
                tvAge.setText("");
                tvDiagnosis.setText("");
                tvReView.setText("");
                tvPill.setText("");
                indexIdList.clear();
                indexNameList.clear();
            }
        } else {
            getHttpPatientList(pationListServerTime, userId, token);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_attention:
                if (TextUtils.isEmpty(token)) {//判断是否登录
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        parentActivity.getWindow().setExitTransition(null);
                        parentActivity.getWindow().setEnterTransition(null);

                    }
                    startActivityForResult(new Intent(parentActivity, ActivityCardLogin.class), 1);
                } else {
                    Intent intent = new Intent(getActivity(), ActivityAddAttention.class);
                    intent.putExtra("ISADD", true);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.tv_switch:
                showMultiChoiceDialog();
                break;
            case R.id.rl_alert://点击查看注意事项
                LATER = true;
                Intent intent = new Intent(parentActivity, ActivityAttentionNoticesList.class);
                intent.putExtra("patientId", patientId);
                startActivityForResult(intent, 1);
                break;
            case R.id.chart:
                Intent intent1 = new Intent(parentActivity, ActivityAttentionChatDetail.class);
                startActivity(intent1);
                break;
        }
    }


    //Recycleradapter 点击事件接口
    AdapterClickInterface adapterListener = new AdapterClickInterface() {

        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ActivityAttentionList.class);
            intent.putExtra("attentionType", position);
            startActivityForResult(intent, 1);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("hcb", "AttentionFragment onActivityResult start");
        if (resultCode == RESULT_OK) {
            token = userInfoSP.getString("token", "");
            userId = userInfoSP.getString("userId", "");
            pationListServerTime = serverTimeSP.getString("pationListServerTime", "0");
            pageIndex = 1;
            getHttpPatientList(pationListServerTime, userId, token);
            //            getHttpData(parentActivity);
        } else if (resultCode == 300) {
            //提示页回调
            LATER = data.getBooleanExtra("later", false);
            Log.i("hcb", "LATER" + LATER);
            //            getHttpData(parentActivity);
        }else if (resultCode == FragmentMarkerList.FragmentMarkerListBack){
            //标志物返回 弹出提示信息
            pationListServerTime = serverTimeSP.getString("pationListServerTime", "0");
            pageIndex = 1;
            LATER = false;
            getHttpPatientList(pationListServerTime, userId, token);
        }
    }


    /**
     * 获取关注人信息
     */
    private void getHttpPatientList(String serverTime, String userId, String token) {
        HttpService.getHttpService().getPatientList(serverTime, userId, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<PatientList>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getHttpPatientList onCompleted()");
                    getDBData();

                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getHttpPatientList onError()");
                    Log.i("hcb", "e is" + e);
                }


                @Override
                public void onNext(PatientList patientList) {
                    Log.i("hcb", "getHttpPatientList onNext()");
                    //                        if (patientList.getData().getSource().size() > 0) {
                    List<PatientList.Source> source = patientList.getData().getSource();

                    String serverTime = patientList.getData().getServerTime();
                    SharedPreferences.Editor serverTimeEditor = serverTimeSP.edit();
                    serverTimeEditor.putString("pationListServerTime", "0");//接口问题 不存 每次传0

                    if (patientList.getData().getSource().size() > 0) {
                        if (patientList.getData().getSource().get(0).getReviewTime() > 0) {
                            userInfoSP.edit()
                                .putLong("recentlyCheck",
                                    patientList.getData().getSource().get(0).getReviewTime())
                                .commit();
                        } else {
                            //删除全部标志物时
                            userInfoSP.edit()
                                .putLong("recentlyCheck", 0)
                                .commit();
                        }

                        userInfoSP.edit()
                            .putInt("takingMedicine",
                                patientList.getData().getSource().get(0).getMedicineNum())
                            .commit();
                    }

                    recentlyCheck = userInfoSP.getLong("recentlyCheck", 0);
                    takingMedicine = userInfoSP.getInt("takingMedicine", 0);
                    tvPill.setText("正在服用" + takingMedicine + "种药物");
                    if (recentlyCheck > 0) {
                        tvReView.setText("最近一次检查" + changeDateToString(recentlyCheck));
                    } else {
                        tvReView.setText("暂无检查");
                    }

                    serverTimeEditor.commit();

                    insertDBData(source);
                    //                        }else {
                    //                            obContent.setVisibility(View.GONE);
                    //                            ivMore.setVisibility(View.GONE);
                    //                            rlNoneAttention.setVisibility(View.VISIBLE);
                    //                            tvAdd.setVisibility(View.VISIBLE);
                    //                        }
                }
            });
    }


    /**
     * 插入数据库数据
     */
    private void insertDBData(List<PatientList.Source> sourceList) {
        PatientDao patientDao = ((MyApplication) parentActivity.getApplication())
            .getAttentionDaoSession().getPatientDao();
        for (int i = 0; i < sourceList.size(); i++) {
            PatientList.Source source = sourceList.get(i);

            String patientId = source.getPatientId();
            long birthday = source.getBirthday();
            long comfirmTime = source.getComfirmTime();
            int relation = source.getRelation();
            int gender = source.getGender();
            long diseaseId = source.getDiseaseId();
            int status = source.getStatus();
            int medicineNum = source.getMedicineNum();
            String diseaseName = source.getDiseaseName();
            String avatar = source.getAvatar();
            boolean isActive = source.getActive();

            Patient patient = new Patient(patientId, comfirmTime, birthday, diseaseId,
                relation, gender, status, medicineNum, diseaseName, userId, avatar, isActive);

            long count = patientDao.queryBuilder()
                .where(PatientDao.Properties.PatientId.eq(patientId))
                .count();
            if (count > 0) {
                patientDao.update(patient);
            } else {
                patientDao.insert(patient);
            }
        }

    }


    /**
     * 获取本地数据库患者信息
     */
    private void getDBData() {
        Observable.create(new Observable.OnSubscribe<Patient>() {
            @Override
            public void call(Subscriber<? super Patient> subscriber) {

                PatientDao patientDao = ((MyApplication) parentActivity.getApplication())
                    .getAttentionDaoSession().getPatientDao();
                Log.i("hcbtest", "userId " + userId);
                List<Patient> patientList = patientDao.queryBuilder()
                    .where(PatientDao.Properties.IsActive.eq(true),
                        PatientDao.Properties.UserId.eq(userId)).list();

                patientId = "0";
                if (patientList.size() > 0) {
                    patient = patientList.get(0);

                    SharedPreferences.Editor editor = userInfoSP.edit();
                    patientId = patient.getPatientId();
                    editor.putString("patientId", patientId);
                    editor.commit();
                }

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Patient>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getDBData onCompleted()");
                    if (patientId.equals("0")) {
                        //无关注人
                        obContent.setVisibility(View.GONE);
                        ivMore.setVisibility(View.GONE);
                        rlNoneAttention.setVisibility(View.VISIBLE);
                        tvAdd.setVisibility(View.VISIBLE);
                    } else {
                        //有关注人
                        obContent.setVisibility(View.VISIBLE);
                        ivMore.setVisibility(View.VISIBLE);
                        rlNoneAttention.setVisibility(View.GONE);
                        tvAdd.setVisibility(View.GONE);

                        int diseaseId = patient.getDiseaseId().intValue();
                        userInfoSP.edit().putInt("patientDiseaseId", diseaseId).commit();
                        Log.i("hcb", "patient.getDiseaseId()" + patient.getDiseaseId());
                        int old = TimeUtils.getAgeByBirthday(patient.getBirthday());
                        tvAge.setText(old + " 岁");

                        if (patient.getAvatar() != null &&
                            !TextUtils.isEmpty(patient.getAvatar())) {
                            String imageUrl = ImageUrl.BaseImageUrl + patient.getAvatar();
                            Log.i("hcbtest", "imageUrl" + imageUrl);
                            //                                Picasso.with(parentActivity).load(imageUrl).error(R.drawable.attention_icon).into(ivIcon);
                            ImageLoaderZMB.getInstance()
                                .loadTransImage(parentActivity, imageUrl, R.drawable.attention_icon,
                                    new CircleTransform(), ivIcon);
                            //                                if (ImageLoaderZMB.getInstance().loadBitMapList(parentActivity,imageUrl).size()>0){
                            //                                    Bitmap mPhoto = (Bitmap) ImageLoaderZMB.getInstance().loadBitMapList(parentActivity,imageUrl).get(0);
                            //                                    iconPhoto = ImageUtil.toRoundBitmap(mPhoto);
                            //                                }

                        } else {
                            ivIcon.setImageResource(R.drawable.attention_icon);
                        }

                        String relationName = AttentionUtils.getRelationName(patient.getGender(),
                            patient.getRelation());
                        tvName.setText(relationName);
                        //
                        //                            int pillNum = patient.getMedicineNum();
                        //                            tvPill.setText("正在服用" + pillNum + "种药品");

                        String comfirmTime = changeDateToString(patient.getComfirmTime());
                        String diseaseName = patient.getDiseaseName();
                        tvDiagnosis.setText(comfirmTime + "确诊" + diseaseName);

                        if (UPDATEPATION) {
                            //判断是否选择停止更新
                             getHttpAttentionNotices(attentionNoticesServerTime, patientId, token);
                        }
                        //                            } else {
                        //                                getDBAttentionNotices(patientId);
                        //                            }
                        getHttpMarkerData(markerServerTime, token);

                    }

                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getDBData onError()");
                }


                @Override
                public void onNext(Patient patient) {
                    Log.i("hcb", "getDBData onNext()");
                }
            });
    }


    /**
     * 删除关注人
     */
    private void deleteHttpPatient(String mPatientId, String token) {

        DeletePatientBody body = new DeletePatientBody(mPatientId);
        HttpService.getHttpService().postDeletePatient(body, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "deleteHttpPatient onCompleted");
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "deleteHttpPatient onError");
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    Log.i("hcb", "deleteHttpPatient onNext");
                    if (createOrDeleteBean.getResult()) {
                        obContent.setVisibility(View.GONE);
                        ivMore.setVisibility(View.GONE);
                        rlNoneAttention.setVisibility(View.VISIBLE);
                        tvAdd.setVisibility(View.VISIBLE);
                        ivIcon.setImageResource(R.drawable.attention_icon);
                        tvName.setText("");
                        tvAge.setText("");
                        tvDiagnosis.setText("");
                        tvReView.setText("");
                        tvPill.setText("");
                        indexIdList.clear();
                        indexNameList.clear();
                        parentActivity.clearPationServerTime();
                        patientId = "";
                        userInfoSP.edit().putString("patientId", patientId).commit();
                        userInfoSP.edit()
                            .putLong(SPInfo.UserKey_recentlyCheck, 0).commit();

                        parentActivity.clearDB();
                    }
                }
            });

    }


    /**
     * 获取所有记录信息，分页
     */
    private void getHttpRecordList(RecordListBody body, String token) {
        HttpService.getHttpService().postGetRecordList(body, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<RecordlistBean>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getHttpRecordList onCompleted");
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getHttpRecordList onError");
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(final RecordlistBean recordlistBean) {
                    Log.i("hcb", "getHttpRecordList onNext");
                    Log.i("hcb", "pageIndex" + pageIndex);
                    if (recordlistBean.getData().getSourceItems().size() > 0) {
                        tvNoneInfo.setVisibility(View.GONE);
                        scrollLinearLayout.setVisibility(View.VISIBLE);
                        totalPage = recordlistBean.getData().getTotalPage();
                        //                        if (pageIndex == 1) {
                        infoList.clear();
                        infoList.addAll(recordlistBean.getData().getSourceItems());
                        //                        } else {
                        //                            infoList.addAll(recordlistBean.getData().getSourceItems());
                        //                        }
                        //                        new Handler().postDelayed(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        //                                progressBar.setVisibility(View.GONE);
                        scrollLinearLayout.setAdapter(
                            new AttentionInfoTimeAdapter(getActivity(), infoList));
                        //                            }
                        //                        }, 1000);
                    } else {
                        infoList.clear();
                        //                        scrollLinearLayout.setAdapter(
                        //                                new AttentionInfoTimeAdapter(getActivity(), infoList));
                        scrollLinearLayout.setVisibility(View.GONE);
                        tvNoneInfo.setVisibility(View.VISIBLE);
                    }

                }
            });
    }


    /**
     * 获取异常消息
     */
    private void getHttpAttentionNotices(String serverTime, final String patientId, String token) {
        HttpService.getHttpService().getAttentionNotices(serverTime, patientId, token)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Subscriber<AttentionNoticesBean>() {
                @Override
                public void onCompleted() {
                    getDBAttentionNotices(patientId);
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getHttpAttentionNotices onError");
                    Log.i("hcb", "getHttpAttentionNotices e" + e);
                }


                @Override
                public void onNext(AttentionNoticesBean attentionNoticesBean) {
                    Log.i("hcb", "getHttpAttentionNotices onNext");
                    if (attentionNoticesBean.getData().getSource().size() > 0) {
                        insertAttentionNoticeDBData(attentionNoticesBean.getData().getSource());
                        //                            userInfoSP.edit().putInt("newNotices", attentionNoticesBean.getData().getSource().size()).commit();
                        serverTimeSP.edit()
                            .putString("attentionNoticesServerTime",
                                attentionNoticesBean.getData().getServerTime()).commit();
                    }
                }
            });
    }


    /**
     * 异常信息入库
     */
    private void insertAttentionNoticeDBData(List<AttentionNoticesBean.Source> sourceList) {
        AttentionNoticesDao attentionNoticesDao = ((MyApplication) parentActivity.getApplication())
            .getAttentionDaoSession().getAttentionNoticesDao();
        for (int i = 0; i < sourceList.size(); i++) {
            String patientId = sourceList.get(i).getPatientId();
            String content = sourceList.get(i).getContent();
            long time = sourceList.get(i).getTime();
            long createTime = sourceList.get(i).getCreateTime();
            long id = sourceList.get(i).getId();
            int status = sourceList.get(i).getStatus();
            boolean isActive = sourceList.get(i).isActive();

            AttentionNotices attentionNotices = new AttentionNotices();
            attentionNotices.setPatientId(patientId);
            attentionNotices.setContent(content);
            attentionNotices.setTime(time);
            attentionNotices.setId(id);
            attentionNotices.setStatus(status);
            attentionNotices.setIsActive(isActive);
            attentionNotices.setCreateTime(createTime);

            long count = attentionNoticesDao.queryBuilder()
                .where(AttentionNoticesDao.Properties.Id.eq(id))
                .count();
            if (count > 0) {
                attentionNoticesDao.update(attentionNotices);
            } else {
                attentionNoticesDao.insert(attentionNotices);
            }
        }
    }


    /**
     * 获取数据库 患者异常信息
     */
    private void getDBAttentionNotices(final String patientId) {
        Observable.create(new Observable.OnSubscribe<AttentionNotices>() {
            @Override
            public void call(Subscriber<? super AttentionNotices> subscriber) {
                AttentionNoticesDao attentionNoticesDao
                    = ((MyApplication) parentActivity.getApplication())
                    .getAttentionDaoSession().getAttentionNoticesDao();
                attentionNoticeDBList.clear();

                attentionNoticeDBList = attentionNoticesDao.queryBuilder()
                    .where(attentionNoticesDao.queryBuilder()
                        .and(AttentionNoticesDao.Properties.PatientId.eq(patientId),
                            AttentionNoticesDao.Properties.IsActive.eq(true),
                            AttentionNoticesDao.Properties.Status.eq(1)))
                    .list();

                //获取新提示
                int newMessage = (int) attentionNoticesDao.queryBuilder()
                    .where(attentionNoticesDao.queryBuilder()
                            .and(AttentionNoticesDao.Properties.PatientId.eq(patientId),
                                AttentionNoticesDao.Properties.IsActive.eq(true),
                                AttentionNoticesDao.Properties.Status.eq(1)),
                        AttentionNoticesDao.Properties.CreateTime.ge(TimeUtils.getDateTime(-2)))
                    .count();
                userInfoSP.edit().putInt("newNotices", newMessage).commit();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<AttentionNotices>() {
                @Override
                public void onCompleted() {
                    Logger.e(LATER+"更新 getDBAttentionNotices"+attentionNoticeDBList.size());
                    if (attentionNoticeDBList.size() > 0) {
                        ivAlter.setVisibility(View.VISIBLE);
                        if (!LATER) {
                            Intent intent = new Intent(parentActivity,
                                ActivityAttentionNoticePop.class);
                            intent.putExtra("patientId", patientId);
                            intent.putExtra("unReadNotices", attentionNoticeDBList.size());
                            startActivityForResult(intent, 1);
                        }

                    } else {
                        ivAlter.setVisibility(View.GONE);
                    }

                    recordListBody.setPatientId(patientId);
                    recordListBody.setPageIndex(pageIndex);
                    recordListBody.setPageSize(100);
                    getHttpRecordList(recordListBody, token);
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getDBAttentionNotices onError");
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(AttentionNotices attentionNotices) {
                    Log.i("hcb", "getDBAttentionNotices onNext");
                }
            });
    }


    /***
     * 获取网络标志物数据
     */
    private void getHttpMarkerData(String serverTime, String token) {

        HttpService.getHttpService().getIndexRecords(serverTime, patientId, token)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Subscriber<IndexRecords>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getHttpData onCompleted");
                    getDBMarkerIndexData(patientId);

                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getHttpData onError");
                    Log.i("hcb", "e is" + e);
                }


                @Override
                public void onNext(IndexRecords indexRecords) {
                    Log.i("hcb", "getHttpData onNext");
                    List<IndexRecords.Source> httpList = indexRecords.getData().getSource();

                    SharedPreferences.Editor editor = serverTimeSP.edit();
                    String markerServerTime = indexRecords.getData().getServerTime();
                    editor.putString("markerServerTime", markerServerTime);
                    editor.commit();

                    insertMarkerDB(httpList);
                }
            });
    }


    /**
     * 插入标志物数据库数据
     */
    private void insertMarkerDB(List<IndexRecords.Source> httpList) {
        if (httpList.size() > 0) {//判断是否有最新数据需要入库
            RecordIndexDao recordIndexDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getRecordIndexDao();
            RecordIndexItemDao recordIndexItemDao
                = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getRecordIndexItemDao();
            for (int i = 0; i < httpList.size(); i++) {
                //indexItem 入库
                IndexRecords.IndexItem indexItem = httpList.get(i).getIndexItem();
                int id = indexItem.getId();
                String patientId = indexItem.getPatientId();
                long hospitalId = indexItem.getHospitalId();
                long time = indexItem.getTime();
                String hospitalName = indexItem.getHospitalName();
                int type = indexItem.getType();
                boolean isActive = indexItem.getActive();

                RecordIndexItem recordIndexItem = new RecordIndexItem();
                recordIndexItem.setId(id);
                recordIndexItem.setPatientId(patientId);
                recordIndexItem.setHospitalId(hospitalId);
                recordIndexItem.setTime(time);
                recordIndexItem.setHospitalName(hospitalName);
                recordIndexItem.setType(type);
                recordIndexItem.setIsActive(isActive);

                long count = recordIndexItemDao.queryBuilder()
                    .where(RecordIndexItemDao.Properties.Id.eq(id))
                    .count();//判断是否更新
                if (count > 0) {
                    Log.i("hcbtest", " recordIndexItemDao.update count" + recordIndexItem.getId());
                    recordIndexItemDao.update(recordIndexItem);
                } else {
                    recordIndexItemDao.insert(recordIndexItem);
                }

                //indexs 入库(标志物)
                List<IndexRecords.Indexs> indexsList = httpList.get(i).getIndexs();

                List<RecordIndex> dbRecordIndexList = recordIndexDao.queryBuilder()
                    .where(RecordIndexDao.Properties.IndexItemRecordId.eq(id)).list();
                for (RecordIndex dbRecordIndex : dbRecordIndexList) {
                    recordIndexDao.delete(dbRecordIndex);
                }
                for (int a = 0; a < indexsList.size(); a++) {
                    IndexRecords.Indexs indexs = indexsList.get(a);
                    int indexItemRecordId = indexs.getIndexItemRecordId();
                    int iId = indexs.getId();
                    //                                    String unitName = indexs.getUnitName();
                    String iPatientId = indexs.getPatientId();
                    String indexName = indexs.getIndexName();
                    //                                    String unitId = indexs.getUnitId();
                    int indexId = indexs.getIndexId();
                    int status = indexs.getStatus();
                    long iTime = indexs.getTime();
                    String value = indexs.getValue();
                    float normalMin = indexs.getNormalMin();
                    float normalMax = indexs.getNormalMax();
                    String unitName = indexs.getUnitName();
                    boolean iIsActive = indexs.getActive();

                    RecordIndex recordIndex = new RecordIndex();
                    recordIndex.setId(iId);
                    recordIndex.setIndexId(indexId);
                    recordIndex.setIndexItemRecordId(indexItemRecordId);
                    recordIndex.setPatientId(iPatientId);
                    recordIndex.setIndexName(indexName);
                    recordIndex.setTime(iTime);
                    recordIndex.setStatus(status);
                    recordIndex.setValue(value);
                    recordIndex.setIsActive(iIsActive);
                    recordIndex.setNormalMin(normalMin);
                    recordIndex.setNormalMax(normalMax);
                    recordIndex.setUnitName(unitName);

                    recordIndexDao.insert(recordIndex);

                    //                    if (recordIndexDao.queryBuilder().where(RecordIndexDao.Properties.Id.eq(iId)).count()>0){
                    //                        Log.i("hcbtest"," recordIndexDao.update count"+recordIndex.getId());
                    //                        recordIndexDao.update(recordIndex);
                    //                    }else {
                    //                        recordIndexDao.insert(recordIndex);
                    //                    }

                }
            }
        }

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
                            RecordIndexDao recordIndexDao
                                = ((MyApplication) (parentActivity.getApplication()))
                                .getAttentionDaoSession().getRecordIndexDao();
                            //去重获取指标Id

                            indexIdList.clear();
                            indexNameList.clear();
                            Cursor c = ((MyApplication) (parentActivity.getApplication()))
                                .getAttentionDaoSession().getDatabase()
                                .rawQuery(
                                    "select INDEX_ID , INDEX_NAME from RECORD_INDEX where IS_ACTIVE = 1 and PATIENT_ID = "
                                        + patientId
                                        +
                                        " group by INDEX_ID , INDEX_NAME order  by count(*) desc, max(TIME) desc limit 3",
                                    null);
                            try {
                                if (c.moveToFirst()) {
                                    do {
                                        //                                            if (indexIdList.size()>0){
                                        //                                                Log.i("hcbtest","indexIdList.size()");
                                        //                                                for (Long id :indexIdList){
                                        //                                                    if (c.getLong(c.getColumnIndex("INDEX_ID"))!=id){
                                        Log.i("hcbtest", "c.getColumnIndex(INDEX_ID)!=id");

                                        if (!indexNameList.contains(
                                            c.getString(c.getColumnIndex("INDEX_NAME")))) {
                                            indexIdList.add(c.getLong(c.getColumnIndex("INDEX_ID")));
                                            indexNameList.add(c.getString(c.getColumnIndex("INDEX_NAME")));
                                        }

                                    } while (c.moveToNext());
                                }
                            } finally {
                                c.close();
                            }
                            Log.i("hcb", "list.size" + indexIdList.size());
                            //                                numberOfPoints = 0;
                            //将取出3个指标，并分组
                            Map<Long, List<RecordIndex>> indexMap = new HashMap<Long, List<RecordIndex>>();
                            List<RecordIndex> indexDose = recordIndexDao.queryBuilder()
                                .where(recordIndexDao.queryBuilder()
                                    .and(RecordIndexDao.Properties.PatientId.eq(patientId),
                                        RecordIndexDao.Properties.IsActive.eq(true),
                                        RecordIndexDao.Properties.IndexId.in(indexIdList)))
                                .orderAsc(RecordIndexDao.Properties.Time)
                                .list();
                            times = new ArrayList<Long>();
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
                            axisX.setTextSize(10);
                            axisX.setTextColor(getResources().getColor(R.color.text_one));

                            Axis axisY = new Axis();
                            List<AxisValue> yList = new ArrayList<AxisValue>();
                            for (int i = 0; i < 11; i++) {
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
                                //                                    numberOfPoints += list.size();
                                Log.i("hcb", "numberOfPoints" + numberOfPoints);
                                for (RecordIndex index : list) {
                                    //生成横坐标
                                    long time = index.getTime();
                                    float x = xMap.get(time);
                                    //生成纵坐标
                                    float value = Float.parseFloat(index.getValue());
                                    float normal = 0;
                                    if (index.getNormalMax() != null && index.getNormalMax() != 0) {
                                        normal = index.getNormalMax();
                                    }
                                    float y = AttentionChatUtil.changeValueToY(value, normal);
                                    Log.i("hcb", "x" + x);
                                    Log.i("hcb", "y" + y);
                                    values.add(new PointValue(x, y));
                                }
                                Line line = new Line(values);
                                line.setColor(ActivityAttentionChatDetail.COLORS[i]);
                                line.setShape(shape);
                                line.setCubic(isCubic);
                                line.setFilled(isFilled);
                                line.setHasLabels(hasLabels);
                                line.setHasLabelsOnlyForSelected(false);
                                line.setHasLines(hasLines);
                                line.setHasPoints(true);//显示点
                                if (pointsHaveDifferentColor) {
                                    line.setPointColor(
                                        ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                                }
                                lines.add(line);

                            }

                            data = new LineChartData(lines);

                            //                                if (hasAxesNames) {
                            //                                    axisX.setName("Axis X");
                            //                                    axisY.setName("Axis Y");
                            //                                }
                            data.setAxisXBottom(axisX);
                            data.setAxisYLeft(axisY);

                            data.setBaseValue(Float.NEGATIVE_INFINITY);
                            chart.setLineChartData(data);
                            chart.setZoomType(ZoomType.HORIZONTAL);
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
                    Log.i("hcbtest", "indexNameList.size()" + indexNameList.size());
                    generateMakerDesc(indexNameList, indexNameList.size());
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
     * 详细记录删选
     */
    private void showMultiChoiceDialog() {
        new MaterialDialog.Builder(parentActivity)
            .title("请选择筛选内容")
            .items(switchList)
            .itemsCallbackMultiChoice(selectedType, new MaterialDialog.ListCallbackMultiChoice() {
                @Override
                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                    return true; // allow selection
                }
            })
            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.clearSelectedIndices();
                }
            })
            .alwaysCallMultiChoiceCallback()
            .autoDismiss(false)
            .positiveText("确认")
            .negativeText("全选")
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.selectAllIndicies();
                }
            })
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.getSelectedIndices().toString();
                    recordTypesList.clear();
                    for (int i = 0; i < dialog.getSelectedIndices().length; i++) {
                        Log.i("hcb", "switchList.get(dialog.getSelectedIndices()[i]).getId()" +
                            switchList.get(dialog.getSelectedIndices()[i]).getId());
                        recordTypesList.add(switchList.get(dialog.getSelectedIndices()[i]).getId());
                    }
                    selectedType = dialog.getSelectedIndices();
                    recordListBody.setRecordTypes(recordTypesList);
                    pageIndex = 1;
                    recordListBody.setPageIndex(pageIndex);
                    getHttpRecordList(recordListBody, token);
                    dialog.dismiss();
                }
            })
            .neutralText("清空")
            .show();

    }


    /**
     * 生成底部指标描述
     */
    private void generateMakerDesc(List<String> indexList, int size) {
        switch (size) {
            case 1:
                tvMarker1.setText(indexList.get(0));
                tvMarker2.setText("");
                tvMarker3.setText("");
                break;
            case 2:
                tvMarker1.setText(indexList.get(0));
                tvMarker2.setText(indexList.get(1));
                tvMarker3.setText("");
                break;
            case 3:
                tvMarker1.setText(indexList.get(0));
                tvMarker2.setText(indexList.get(1));
                tvMarker3.setText(indexList.get(2));
                break;
            case 0:
                tvMarker1.setText("");
                tvMarker2.setText("");
                tvMarker3.setText("");
                break;
            default:
                tvMarker1.setText(indexList.get(0));
                tvMarker2.setText(indexList.get(1));
                tvMarker3.setText(indexList.get(2));
                break;
        }
    }


    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 12;
        v.left = 0;
        //横屏最佳点数15个
        if (numberOfPoints > 8) {
            v.right = numberOfPoints + 1;
        } else {
            v.right = 8;
        }

        chart.setMaximumViewport(v);
        final Viewport vCurrent = new Viewport(chart.getMaximumViewport());
        vCurrent.bottom = 0;
        vCurrent.top = 12;
        vCurrent.left = 0;
        vCurrent.right = 8;
        chart.setCurrentViewport(vCurrent);

        chart.setZoomEnabled(false);
    }


    public void clearPation() {
        indexIdList.clear();
        indexNameList.clear();
    }

}
