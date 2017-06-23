package com.zhongmeban.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.AttenDoctorAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.BaseBean;
import com.zhongmeban.utils.LayoutActivityTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 关注医生
 * Created by Chengbin He on 2016/3/21.
 */
public class ActivityAttenDoctor extends BaseActivity {
    private LayoutActivityTitle title;
    private RecyclerView recyclerView;
    private AttenDoctorAdapter mAdapter;
    /*全选，反选部分。
     */
//    private LinearLayout checkInfo;
//    private Button bt_cancel;
//    private Button bt_check;
    private Boolean ISSELECTALL = true;
    private Boolean ISEDIT = false;
    private List <BaseBean> beanList = new ArrayList<BaseBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_article);
        initView();
        initTitle();
    }

    private void initView() {
//        checkInfo = (LinearLayout) findViewById(R.id.checkinfo);
//        bt_cancel = (Button) findViewById(R.id.bt_cancel);
//        bt_cancel.setOnClickListener(onClickListener);
//        bt_check = (Button) findViewById(R.id.bt_check);
//        bt_check.setOnClickListener(onClickListener);

        BaseBean bean1 = new BaseBean();
        BaseBean bean2 = new BaseBean();
        BaseBean bean3 = new BaseBean();
        BaseBean bean4 = new BaseBean();
        BaseBean bean5 = new BaseBean();
        BaseBean bean6 = new BaseBean();
        BaseBean bean7 = new BaseBean();
        BaseBean bean8 = new BaseBean();
        BaseBean bean9 = new BaseBean();
        beanList.add(bean1);
        beanList.add(bean2);
        beanList.add(bean3);
        beanList.add(bean4);
        beanList.add(bean5);
        beanList.add(bean6); 
        beanList.add(bean7);
        beanList.add(bean8);
        beanList.add(bean9);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityAttenDoctor.this));
        mAdapter = new AttenDoctorAdapter(ActivityAttenDoctor.this ,beanList );
        mAdapter.setItemClickListener(adapterClick);
        recyclerView.setAdapter(mAdapter);

    }


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("关注医生");
        title.slideRighttext("编辑",onClickListener);
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
//                case R.id.bt_cancel: //取消选择
//                    mAdapter.setCheckGone();
//                    checkInfo.setVisibility(View.GONE);
//                    ISSELECTALL = false;
//                    bt_check.setText("全选");
//                    title.slideRighttext("编辑");
//                break;
//
//                case R.id.bt_check: //全选删除
//                    title.slideRighttext("删除");
//                    if (ISSELECTALL){
//                        ISSELECTALL = false;
//                        mAdapter.setSelectAll();
//                        bt_check.setText("清空");
//                    }else{
//                        ISSELECTALL = true;
//                        mAdapter.setUnSelectAll();
//                        bt_check.setText("全选");
//                    }
//
//                    break;

                case R.id.right_button: //title右侧Button
                    if ( title.getRightTitle().equals("编辑")){
                        title.slideRighttext("删除");
                    }else{
                        mAdapter.removeItem();
                        mAdapter.setCheckGone();
                        title.slideRighttext("编辑");
                    }
                break;
            }
        }
    };

    AdapterClickInterface adapterClick = new AdapterClickInterface() {

        @Override
        public void onItemClick(View v, int position) {

        }

        @Override
        public void onItemLongClick(View v, int position) {
            ISEDIT = true;
            title.slideRighttext("删除");
        }

    };

}
