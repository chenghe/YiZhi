package com.zhongmeban.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pgyersdk.update.PgyUpdateManager;
import com.umeng.socialize.UMShareAPI;
import com.zhongmeban.BuildConfig;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.fragment.AttentionFragment;
import com.zhongmeban.fragment.FragFactory;
import com.zhongmeban.fragment.FragHome;
import com.zhongmeban.mymodle.fragment.FragmentMy;
import com.zhongmeban.utils.ToastUtil;
import de.greenrobot.dao.attention.AttentionNoticesDao;
import de.greenrobot.dao.attention.ChemotherapyDao;
import de.greenrobot.dao.attention.ChemotherapyMedicineDao;
import de.greenrobot.dao.attention.HospitalizedDao;
import de.greenrobot.dao.attention.MedicineRecordDao;
import de.greenrobot.dao.attention.PatientDao;
import de.greenrobot.dao.attention.RadiotherapyDao;
import de.greenrobot.dao.attention.RadiotherapySuspendedRecordsDao;
import de.greenrobot.dao.attention.RecordIndexDao;
import de.greenrobot.dao.attention.RecordIndexItemDao;
import de.greenrobot.dao.attention.SurgeryMethodsDao;
import de.greenrobot.dao.attention.SurgeryRecordDao;

/**
 * @author Administrator
 * @ClassName:
 * @Description: 主页
 * @date 2015/12/25 11:50
 */
public class ActHome extends BaseActivity {

    // 用于查找回退栈中的fragment，findFragmentByTag
    public static final String TAB_ATTENTION_TAG = AttentionFragment.class.getSimpleName();
    public static final String TAB_TREATMENT_TAG = FragHome.class.getSimpleName();
    //    public static final String TAB_SHOP_TAG = ShopFragment.class.getSimpleName();
    public static final String TAB_SETTING_TAG = FragmentMy.class.getSimpleName();

    private FrameLayout fragContainer;

    //    // 底部菜单
    //    RadioGroup mTabMenu;

    // 保留当前的显示的fragment的标签
    String mLastFragmentTag;
    private ImageView heart;
    private ImageView cure;
    private ImageView store;
    private ImageView ivMy;

    private LinearLayout mLayoutHeart;
    private LinearLayout mLayoutCure;
    private LinearLayout mLayoutStore;
    private LinearLayout llMy;

    private TextView tvHeart;
    private TextView tvCure;
    private TextView tvStore;
    private TextView tvMy;
    public boolean changeUser;//更改用户

    //    private FrameLayout flLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initView();

        if (MyApplication.isTest){
            if (!BuildConfig.IS_DEBUG) {
                PgyUpdateManager.register(this);
            }
        }

    }


    private void initView() {

        //        flLogin = (FrameLayout) findViewById(R.id.card_login);

        // 初始化底部菜单
        heart = (ImageView) findViewById(R.id.heart);
        cure = (ImageView) findViewById(R.id.cure);
        store = (ImageView) findViewById(R.id.bag);
        ivMy = (ImageView) findViewById(R.id.iv_my);

        llMy = (LinearLayout) findViewById(R.id.ll_my);
        mLayoutHeart = (LinearLayout) findViewById(R.id.id_tab_ll_attention);
        mLayoutCure = (LinearLayout) findViewById(R.id.id_tab_ll_cure);
        mLayoutStore = (LinearLayout) findViewById(R.id.id_tab_ll_store);
        llMy.setOnClickListener(onClickListener);
        mLayoutHeart.setOnClickListener(onClickListener);
        mLayoutCure.setOnClickListener(onClickListener);
        mLayoutStore.setOnClickListener(onClickListener);

        tvMy = (TextView) findViewById(R.id.tv_my);
        tvHeart = (TextView) findViewById(R.id.id_tab_tv_attention);
        tvCure = (TextView) findViewById(R.id.id_tab_tv_cure);
        tvStore = (TextView) findViewById(R.id.id_tab_tv_store);

        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);

        // 后退栈为空，添加第一个fragment，即默认显示的fragment
        if (fragContainer != null) {
            FragHome fragHome = new FragHome();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragHome, TAB_TREATMENT_TAG)
                .addToBackStack(null).commit();

            mLastFragmentTag = TAB_TREATMENT_TAG;//初始化的时候将首页作为上一页的标志
            changeTabSelect(1);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_tab_ll_attention://关注
                    changeTabSelect(0);
                    change(TAB_ATTENTION_TAG);
                    AttentionFragment.LATER = false;
                    break;
                case R.id.id_tab_ll_cure://治疗
                    changeTabSelect(1);
                    change(TAB_TREATMENT_TAG);
                    break;
                //                case R.id.id_tab_ll_store://购物
                //                    changeTabSelect(2);
                //                    change(TAB_SHOP_TAG);
                //                    //                    Intent intent = new Intent(ActHome.this,ActivityDetail.class);
                //                    //                    startActivity(intent);
                //                    //测试界面
                //                    break;
                case R.id.ll_my://我
                    changeTabSelect(3);
                    change(TAB_SETTING_TAG);
                    break;

            }
        }
    };


    public void changeTabSelect(int index) {

        int textColorNormal = getResources().getColor(R.color.text_one);

        heart.setImageResource(R.drawable.heart_normal);
        cure.setImageResource(R.drawable.cure_normal);
        store.setImageResource(R.drawable.shopping_normal);
        ivMy.setImageResource(R.drawable.mine_normal);
        tvMy.setTextColor(textColorNormal);
        tvHeart.setTextColor(textColorNormal);
        tvCure.setTextColor(textColorNormal);
        tvStore.setTextColor(textColorNormal);

        switch (index) {
            case 0:
                heart.setImageResource(R.drawable.heart_selected);
                tvHeart.setTextColor(getResources().getColor(R.color.app_green));
                break;
            case 1:
                cure.setImageResource(R.drawable.cure_selected);
                tvCure.setTextColor(getResources().getColor(R.color.app_green));
                break;
            //            case 2:
            //                store.setImageResource(R.drawable.shopping_selected);
            //                tvStore.setTextColor(getResources().getColor(R.color.app_green));
            //                break;
            case 3:
                ivMy.setImageResource(R.drawable.mine_selected);
                tvMy.setTextColor(getResources().getColor(R.color.app_green));
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //        super.onSaveInstanceState(outState);
    }


    // 切换fragment
    private void change(String Tag) {
        if (Tag != mLastFragmentTag) {
            //UmengUtils.TabEvent.clickTabIndex(this,Tag);

            Fragment lastFragment = getSupportFragmentManager()
                .findFragmentByTag(mLastFragmentTag);

            Fragment fragmentNow;
            if (getSupportFragmentManager().findFragmentByTag(Tag) != null) {
                fragmentNow = getSupportFragmentManager()
                    .findFragmentByTag(Tag);
                getSupportFragmentManager().beginTransaction()
                    .show(fragmentNow).hide(lastFragment).commit();
            } else {
                //根据当前tag创建出来显得fragment
                fragmentNow = FragFactory.getInstanceByTag(Tag);
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragmentNow, Tag)
                    .addToBackStack(null).hide(lastFragment).commit();
            }

            if (Tag.equals(TAB_ATTENTION_TAG)) {
                if (fragmentNow instanceof AttentionFragment) {
                    AttentionFragment fragment = (AttentionFragment) fragmentNow;
                    if (changeUser){
                        fragment.clearPation();
                        changeUser = false;
                    }

                    fragment.getHttpData(ActHome.this);
                }
            }
            mLastFragmentTag = Tag;
        }
    }


    long waitTime = 1000;
    long touchTime = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                //让Toast的显示时间和等待时间相同
                ToastUtil.showText(getApplicationContext(), "再按一次退出程序");
                touchTime = currentTime;
                return true;
            } else {
                finish();
            }

            boolean used = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment curFragment = fm.findFragmentByTag(mLastFragmentTag);
            if (curFragment instanceof Fragment) {
                Fragment currentBaseFm = curFragment;
                curFragment.getActivity().finish();
                return true;
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void initTitle() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void clearPationServerTime(){
        SharedPreferences serverTimeSP = getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor serverTimeEditor = serverTimeSP.edit();
        serverTimeEditor.putString("pationListServerTime", "0");//关注人
        serverTimeEditor.putString("markerServerTime", "0");
        serverTimeEditor.putString("medicineRecordServerTime", "0");
        serverTimeEditor.putString("surgeryServerTime", "0");
        serverTimeEditor.putString("hospitalizedServerTime", "0");
        serverTimeEditor.putString("radiotherapyServerTime", "0");
        serverTimeEditor.putString("chemotherapyServerTime", "0");
        serverTimeEditor.putString("chemotherapyCourseServerTime", "0");
        serverTimeEditor.putString("pationListServerTime", "0");
        serverTimeEditor.putString("attentionNoticesServerTime", "0");
        serverTimeEditor.commit();
    }

    public void clearDB() {
        //标志物名称 表
        RecordIndexDao recordIndexDao = ((MyApplication) getApplication()).getAttentionDaoSession()
                .getRecordIndexDao();
        recordIndexDao.deleteAll();
        //标志物详情 表
        RecordIndexItemDao recordIndexItemDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getRecordIndexItemDao();
        recordIndexItemDao.deleteAll();
        //      //用药 表
        MedicineRecordDao medicineRecordDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getMedicineRecordDao();
        medicineRecordDao.deleteAll();
        //手术记录 表
        SurgeryRecordDao surgeryRecordDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getSurgeryRecordDao();
        surgeryRecordDao.deleteAll();
        //手术其他项目 表
        SurgeryMethodsDao surgeryMethodsDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getSurgeryMethodsDao();
        surgeryMethodsDao.deleteAll();
        //住院 表
        HospitalizedDao hospitalizedDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getHospitalizedDao();
        hospitalizedDao.deleteAll();
        //      //放疗 表
        RadiotherapyDao radiotherapyDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getRadiotherapyDao();
        radiotherapyDao.deleteAll();
//        //放疗 暂停记录表
//        RadiotherapySuspendedRecordsDao radiotherapySuspendedRecordsDao
//                = ((MyApplication) getApplication()).getAttentionDaoSession()
//                .getRadiotherapySuspendedRecordsDao();
//        radiotherapySuspendedRecordsDao.deleteAll();
        //化疗 表
        ChemotherapyDao chemotherapyDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getChemotherapyDao();
        chemotherapyDao.deleteAll();
        //化疗 用药 关系表
        ChemotherapyMedicineDao chemotherapyMedicineDao
                = ((MyApplication) getApplication()).getAttentionDaoSession()
                .getChemotherapyMedicineDao();
        chemotherapyMedicineDao.deleteAll();
 /*       //      //化疗疗程 用药 关系表
        ChemotherapyCourseMedicineDao chemotherapyCourseMedicineDao
                = ((MyApplication) getApplication()).getAttentionDaoSession()
                .getChemotherapyCourseMedicineDao();
        chemotherapyCourseMedicineDao.deleteAll();
        //      //化疗 疗程 表
        ChemotherapyCourseDao chemotherapyCourseDao
                = ((MyApplication) getApplication()).getAttentionDaoSession()
                .getChemotherapyCourseDao();
        chemotherapyCourseDao.deleteAll();*/
        //患者表
        PatientDao patientDao = ((MyApplication) getApplication()).getAttentionDaoSession()
                .getPatientDao();
        patientDao.deleteAll();
        //关注提示表
        AttentionNoticesDao attentionNoticesDao
                = ((MyApplication) getApplication()).getAttentionDaoSession().getAttentionNoticesDao();
        attentionNoticesDao.deleteAll();
    }
}
