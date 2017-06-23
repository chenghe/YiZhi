package com.zhongmeban.treatmodle.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.zhongmeban.bean.treatbean.TreatHospList;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.contract.TreatSearchHospListContract;
import com.zhongmeban.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 医院搜索Presenter
 * Created by Chengbin He on 2016/12/9.
 */

public class TreatSearchHospListPresenter implements TreatSearchHospListContract.Presenter{

    private Context mContext;
    private TreatSearchHospListContract.View view;

    private TreatHospitalScource load = new TreatHospitalScource();
    private int pageIndex = 1;
    private int pageSize = 20;
    private int level;//医院等级
    private String cityCode = "";
    private String keywords = "";
    private List<TreatHospitalScource> httpList = new ArrayList<TreatHospitalScource>();
    private Subscription mSubscription;
    private boolean exitNext = true;//判断是否有下一页


    public TreatSearchHospListPresenter(Context mContext, TreatSearchHospListContract.View view) {
        this.mContext = mContext;
        this.view = view;
        this.view.setPresenter(this);
        load.setViewType(0);
    }

    @Override
    public void start() {

    }

    @Override
    public void getHttpData() {
        mSubscription = HttpService.getHttpService().getHospitalList(cityCode,level,keywords,pageIndex,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreatHospList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()" + e);
                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                        view.noNet();
                    }

                    @Override
                    public void onNext(TreatHospList treatHospList) {
                        Log.i("hcb", "http onNext()");
                        exitNext = true;
                        if (treatHospList.getData().size()>0){
                            view.exitData();
                            if (pageIndex == 1){
                                httpList.clear();
                                httpList = treatHospList.getData();
                            }else {
                                view.onPostLoadMore();
                                httpList.addAll(treatHospList.getData());
                            }
                            view.showData(httpList);
                        } else if (pageIndex>1&&treatHospList.getData().size() ==0){
                            exitNext = false;
                            ToastUtil.showText(mContext,"无更多内容");
                            view.onPostLoadMore();
                            view.showData(httpList);
                        }else {
                            view.noData();
                        }

                    }
                });
    }

    @Override
    public void setSearchKeyword(String keyword) {
        this.keywords = keyword;
        pageIndex = 1;
        getHttpData();
    }

    @Override
    public void destory() {
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void nextIndex() {
        pageIndex++;
    }

    @Override
    public void resetIndex() {
        pageIndex = 1;
    }

    @Override
    public Intent startHospDetail(Intent intent, int position) {
        String hospName = httpList.get(position).getName();
        String hospAddress = httpList.get(position).getAddress();
        String hospitalId = httpList.get(position).getId() + "";

        intent.putExtra("hospName", hospName);
        intent.putExtra("hospAddress", hospAddress);
        intent.putExtra("hospitalId", hospitalId);
        return intent;
    }

    @Override
    public List<TreatHospitalScource> getHttpList() {
        return httpList;
    }

    @Override
    public void addLoadMore() {
        httpList.add(load);
    }

    @Override
    public void removeLoadMore() {
        httpList.remove(load);
    }

    @Override
    public boolean exitNextPage() {
        return exitNext;
    }


}
