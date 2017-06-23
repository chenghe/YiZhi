package com.zhongmeban.attentionmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TabViewFragmentPagerAdapter;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionTakeAllMedicine;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionTakedMedicine;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionTakingMedicine;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.MedicineRecordBean;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SlidingTabLayout;
import de.greenrobot.dao.attention.MedicineRecordDao;
import java.util.ArrayList;
import java.util.List;

/**
 * 用药记录列表Activity
 * Created by Chengbin He on 2016/7/4.
 */
public class ActivityAttentionMedicineList extends BaseActivity implements View.OnClickListener {

    private static final String[] tabTitle = {"正在服用", "历史用药", "全部用药"};
    private List<MedicineRecordBean.Source> httpList;//网络数据List
    private Context mContext = ActivityAttentionMedicineList.this;
    private TabViewFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentAttentionTakingMedicine fragmentAttentionTakingMedicine;//增在服用药物Fragment
    private FragmentAttentionTakedMedicine fragmentAttentionTakedMedicine;//历史用药Fragment
    private FragmentAttentionTakeAllMedicine fragmentAttentionTakeAllMedicine;//全部用药Fragment
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton floatingButtonAddNew;
    private FloatingActionButton floatingButtonAddExist;
    private SharedPreferences serverTimeSP;
    private ViewPager vp;
    private SlidingTabLayout tabLayout;
    private String serverTime;
    private MedicineRecordDao medicineRecordDao;
    public String token;
    public String patientId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_medicine_list);

        SharedPreferences userInfoSP = getSharedPreferences("userInfo", MODE_PRIVATE);
        token = userInfoSP.getString("token", "");
        patientId = userInfoSP.getString("patientId", "");

        serverTimeSP = getSharedPreferences("serverTime", MODE_PRIVATE);
        serverTime = serverTimeSP.getString("medicineRecordServerTime", "0");
        initView();
        initTitle();
        getHttpData(serverTime, patientId, token,false);

    }

    private void initView() {

        fragmentAttentionTakingMedicine = FragmentAttentionTakingMedicine.newInstance();
        fragmentAttentionTakedMedicine = FragmentAttentionTakedMedicine.newInstance();
        fragmentAttentionTakeAllMedicine = FragmentAttentionTakeAllMedicine.newInstance();
        mFragments.add(fragmentAttentionTakingMedicine);
        mFragments.add(fragmentAttentionTakedMedicine);
        mFragments.add(fragmentAttentionTakeAllMedicine);

        vp = (ViewPager) findViewById(R.id.vp);
        mPagerAdapter = new TabViewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, tabTitle);
        vp.setAdapter(mPagerAdapter);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tl);
        tabLayout.setViewPager(vp);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu_green);
        floatingActionMenu.showMenuButton(true);
        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingButtonAddNew = (FloatingActionButton) findViewById(R.id.float_button_new);
        floatingButtonAddNew.setLabelTextColor(Color.BLACK);
        floatingButtonAddNew.setLabelText("添加当前用药");
        floatingButtonAddNew.setOnClickListener(this);
        floatingButtonAddNew.setLabelColors(getResources().getColor(R.color.white),
                getResources().getColor(R.color.floating_gray_press),
                getResources().getColor(R.color.floating_gray_ripple));


        floatingButtonAddExist = (FloatingActionButton) findViewById(R.id.float_button_exist);
        floatingButtonAddExist.setLabelTextColor(Color.BLACK);
        floatingButtonAddExist.setLabelText("记录历史用药");
        floatingButtonAddExist.setOnClickListener(this);
        floatingButtonAddExist.setLabelColors(getResources().getColor(R.color.white),
                getResources().getColor(R.color.floating_gray_press),
                getResources().getColor(R.color.floating_gray_ripple));
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("辅助用药");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.left_button:
                onBackPressed();
                break;
            case R.id.float_button_new://添加当前用药
                intent = new Intent(mContext, ActivityAttentionAddMedicine.class);
                intent.putExtra("ISNOW", true);
                startActivityForResult(intent, 1);
                floatingActionMenu.close(true);
                break;
            case R.id.float_button_exist://记录历史用药
                intent = new Intent(mContext, ActivityAttentionAddMedicine.class);
                intent.putExtra("ISNOW", false);
                startActivityForResult(intent, 1);
                floatingActionMenu.close(true);
                break;
        }
    }

    /**
     * 获取网络数据
     *
     * @param mServerTime
     * @param patientId
     * @param token
     */
    private void getHttpData(String mServerTime, final String patientId, String token, final boolean getDB) {
        //HttpService.getHttpService().getMedicineRecord(mServerTime, patientId)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Subscriber<MedicineRecordBean>() {
        //            @Override
        //            public void onCompleted() {
        //                Log.i("hcb", "ActivityAttentionMedicineList getHttpData onCompleted");
        //                if (httpList.size() > 0) {//判断是否有最新数据需要入库
        //                    medicineRecordDao = ((MyApplication) getApplication())
        //                            .getAttentionDaoSession().getMedicineRecordDao();
        //                    for (int i = 0; i < httpList.size(); i++) {
        //                        int id = httpList.get(i).getId();
        //                        int type = httpList.get(i).getType();
        //                        int status = httpList.get(i).getStatus();
        //                        int medicineId = httpList.get(i).getMedicineId();
        //                        long createTime = httpList.get(i).getCreateTime();
        //                        long startTime = httpList.get(i).getStartTime();
        //                        long endTime = httpList.get(i).getEndTime();
        //                        long updateTime = httpList.get(i).getUpdateTime();
        //                        String medicineName = httpList.get(i).getMedicineName();
        //                        boolean isActive = httpList.get(i).isActive();
        //                        MedicineRecord medicineRecord = new MedicineRecord(id, type, status,
        //                                medicineId, patientId, createTime, startTime, endTime,
        //                                updateTime, medicineName, isActive);
        //                        long cont = medicineRecordDao.queryBuilder()
        //                                .where(MedicineRecordDao.Properties.Id.eq(id))
        //                                .count();
        //                        if (cont > 0) {
        //                            medicineRecordDao.update(medicineRecord);
        //
        //                        } else {
        //                            medicineRecordDao.insert(medicineRecord);
        //
        //                        }
        //                    }
        //                }
        //
        //                if (getDB){
        //                    fragmentAttentionTakingMedicine.getDBData();
        //                    fragmentAttentionTakeAllMedicine.getDBData();
        //                    fragmentAttentionTakedMedicine.getDBData();
        //                }
        //
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                Log.i("hcb", "ActivityAttentionMedicineList getHttpData onError");
        //                Log.i("hcb", "ActivityAttentionMedicineList e" + e);
        //                ToastUtil.showText(mContext,"请检查网络");
        //            }
        //
        //            @Override
        //            public void onNext(MedicineRecordBean medicineRecordBean) {
        //                Log.i("hcb", "ActivityAttentionMedicineList getHttpData onNext");
        //                serverTime = medicineRecordBean.getDate().getServerTime();
        //                SharedPreferences.Editor editor = serverTimeSP.edit();
        //                editor.putString("medicineRecordServerTime", serverTime);
        //                editor.commit();
        //
        //                httpList = medicineRecordBean.getDate().getSource();
        //            }
        //        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            Log.i("hcb", "onActivityResult");
            Log.i("hcbtest", "fragmentPosition" + data.getIntExtra("fragmentPosition", 0));
            vp.setCurrentItem(data.getIntExtra("fragmentPosition", 0));
            tabLayout.setCurrentTab(data.getIntExtra("fragmentPosition", 0));
            serverTime = serverTimeSP.getString("medicineRecordServerTime", "0");
            getHttpData(serverTime, patientId, token,true);
        }
    }

    /**
     * Fragment 调用Activity 更新网络数据
     */
    public void updateHttpData() {
        serverTime = serverTimeSP.getString("medicineRecordServerTime", "0");
        getHttpData(serverTime, patientId, token,true);
    }
}
