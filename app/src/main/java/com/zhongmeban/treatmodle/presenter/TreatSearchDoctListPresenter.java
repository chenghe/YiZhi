package com.zhongmeban.treatmodle.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.zhongmeban.bean.treatbean.TreatDoctList;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.bean.treatbean.TreatHospList;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.contract.TreatSearchDoctListContract;
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

public class TreatSearchDoctListPresenter implements TreatSearchDoctListContract.Presenter{

    private Context mContext;
    private TreatSearchDoctListContract.View view;

    private TreatDoctor load = new TreatDoctor();
    private int pageIndex = 1;
    private int pageSize = 20;
    private int level;//医院等级
    private int hospLevel;
    private String cityCode = "";
    private int doctTitle;
    private String keywords = "";
    private List<TreatDoctor> httpList = new ArrayList<TreatDoctor>();
    private Subscription mSubscription;
    private boolean exitNext = true;//判断是否有下一页


    public TreatSearchDoctListPresenter(Context mContext, TreatSearchDoctListContract.View view) {
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
        mSubscription = HttpService.getHttpService().getDoctorList(keywords, cityCode, "",
                hospLevel, doctTitle,pageIndex, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreatDoctList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getHttpData onNext");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getHttpData onError" +e);;

                        view.noNet();
                    }

                    @Override
                    public void onNext(TreatDoctList treatDoctList) {
                        Log.i("hcb", "getHttpData onNext");
                        if (treatDoctList.getData().size() > 0) {
                            view.exitData();
                            if (pageIndex == 1) {
                                httpList.clear();
                                httpList = treatDoctList.getData();
                                Log.i("hcb", "httpList.size()" + httpList.size());
                            } else {
                                view.onPostLoadMore();
                                httpList.addAll(treatDoctList.getData());
                            }
                            view.showData(httpList);
                        } else if (pageIndex>1&&treatDoctList.getData().size() ==0){
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
    public Intent startDoctDetail(Intent intent, int position) {
        String doctName = httpList.get(position).getDoctorName();
        String hospName = httpList.get(position).getHospitalName();
        String doctorId = httpList.get(position).getDoctorId()+"";

        intent.putExtra("doctName",doctName);
        intent.putExtra("hospName",hospName);
        intent.putExtra("doctorId",doctorId);
        return intent;
    }

    @Override
    public List<TreatDoctor> getHttpList() {
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
