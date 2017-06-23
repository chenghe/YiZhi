package com.zhongmeban.mymodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.mymodle.adapter.FavoriteMedicineListAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.FavoriteMedicineList;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我关注的药品
 * Created by HeCheng on 2016/11/6.
 */

public class ActivityMyMedicine extends BaseActivity  {

    private RecyclerView recyclerView;
    private FavoriteMedicineListAdapter mAdapter;
    private Context mContext = ActivityMyMedicine.this;
    private String token;
    private String userId;
    private SharedPreferences userSp;
    private Button btCancle;
    private LinearLayout llEmptyView;
    private boolean ISEDIT = true;//判断是否为编辑状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_common);
        initTitle();
        initData();
        llEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        btCancle = (Button) findViewById(R.id.bt_cancel);
        btCancle.setOnClickListener(onClickListener);
        getFavoriteMedicine(userId,token);
    }

    private void initData(){
        userSp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("收藏药品");
//        title.slideRighttext("编辑", onClickListener);
        title.backSlid(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.right_button: //title右侧Button
                    if (ISEDIT) {
                        ISEDIT = false;
                        title.slideRighttext("删除");
                        mAdapter.setChoose(true);
                        btCancle.setVisibility(View.VISIBLE);
                    } else {
                        if (mAdapter.getChooseList().size() > 0) {
                            ISEDIT = true;
                            title.slideRighttext("编辑");
                            btCancle.setVisibility(View.GONE);
                            Log.i("hcbtest", "mAdapter.getChooseList().size()" + mAdapter.getChooseList().size());
                            DeleteFavoriteBody body = new DeleteFavoriteBody(TreatMentShare.MEDICINE, mAdapter.getChooseList());
                            deleteFavorite(body, token);
                            mAdapter.setChoose(false);
                        }else {
                            ToastUtil.showText(mContext,"请选择删除内容");
                        }

                    }
                    break;

                case R.id.ivTitleBtnback://back 图标
                    finish();
                    break;
                case R.id.bt_cancel:
                    ISEDIT = true;
                    title.slideRighttext("编辑");
                    mAdapter.setChoose(false);
                    btCancle.setVisibility(View.GONE);
                    break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //内容页返回
            boolean isFavorite  = data.getBooleanExtra("isFavorite",true);
            if (!isFavorite){
                getFavoriteMedicine(userId,token);
            }
        }
    }
    /**
     * 获取关注医生
     * @param userId
     * @param token
     */
    private void getFavoriteMedicine(String userId, String token) {
        HttpService.getHttpService().getFavoriteMedicine(1, 100, userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FavoriteMedicineList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","getFavoriteMedicine onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","getFavoriteMedicine onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(final FavoriteMedicineList favoriteMedicineList) {
                        Log.i("hcb","getFavoriteMedicine onNext");
                        if (favoriteMedicineList.getData().getSourceItems().size()>0){
                            llEmptyView.setVisibility(View.GONE);
                            title.slideRighttext("编辑", onClickListener);

                            final List<FavoriteMedicineList.DrugScourceItem> list = favoriteMedicineList.getData().getSourceItems();
                            mAdapter = new FavoriteMedicineListAdapter(list ,mContext);
                            mAdapter.setItemClickListener(new AdapterClickInterface() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Intent intent = new Intent(mContext,ActPharmaceutiaclDetail.class);
//                                    Medicine medicine = new Medicine();

                                    Long medicineId = list.get(position).getMedicineId();
//                                    medicine.setChemicalId(medicineId);
//                                    medicine.setMedicineName(list.get(position).getMedicineName());
//                                    medicine.setChemicalName(list.get(position).getChemicalName());
//                                    medicine.setPrescription(list.get(position).getPrescription());
//                                    medicine.setClassify(list.get(position).getClassify());
//                                    medicine.setPriceMax(list.get(position).getPriceMax());

                                    intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID,medicineId+"");
                                    startActivityForResult(intent,1);
                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });

                        }else {
                            List<FavoriteMedicineList.DrugScourceItem> list = new ArrayList<FavoriteMedicineList.DrugScourceItem>();
                            mAdapter = new FavoriteMedicineListAdapter(list,mContext);
                            title.setRightGone();
                            llEmptyView.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void deleteFavorite(DeleteFavoriteBody body , final String token){
        HttpService.getHttpService().postDeleteFavorite(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","deleteFavorite onCompleted");
                        getFavoriteMedicine(userId,token);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","deleteFavorite onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","deleteFavorite onNext");
                    }
                });
    }
}
