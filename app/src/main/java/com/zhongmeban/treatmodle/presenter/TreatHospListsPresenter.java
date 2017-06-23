package com.zhongmeban.treatmodle.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.bean.DoctorList;
import com.zhongmeban.bean.DropDownBean;
import com.zhongmeban.bean.HospitalLists;
import com.zhongmeban.bean.HospitalScource;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.ChooseHospBean;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.ProvinceListBean;
import com.zhongmeban.bean.treatbean.TreatHospList;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.contract.TreatDoctListsContract;
import com.zhongmeban.treatmodle.contract.TreatHospListsContract;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.disk.CacheUtil;
import com.zhongmeban.utils.genericity.TreatDocHospLevelGen;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 医生列表presenter
 * Created by Chengbin He on 2016/12/2.
 */

public class TreatHospListsPresenter implements TreatHospListsContract.Presenter {

    public static final String CHOOSE_HOSP_BEAN = "ChooseHospBean";

    private Context mContext;
    private TreatHospListsContract.View view;

    private List<TreatHospitalScource> httpList = new ArrayList<TreatHospitalScource>();
    private Subscription mSubscription;
    private int level;//医院等级
    private String cityCode = "";
    private String keywords = "";
    private TreatHospitalScource load = new TreatHospitalScource();
    private int pageIndex = 1;
    private int pageSize = 20;
    private int totalPage = 100;//总页数
    private List<DropDownBean> provinceList = new ArrayList<>();//省List
    private List<DropDownBean> cityList = new ArrayList<>();//城市List
    private List<ProvinceListBean>httpProvinceList = new ArrayList<>();//网络省列表
    private List<CityListBean> httpCityList = new ArrayList<>();//网络城市列表
    private List<DropDownBean> hospLevelList = new ArrayList<>();//医院等级列表
    private boolean exitNext = true;//判断是否有下一页
    private ChooseHospBean chooseHospBean;

    public TreatHospListsPresenter( TreatHospListsContract.View view,Context mContext) {
        this.mContext = mContext;
        this.view = view;
        this.view.setPresenter(this);
        load.setViewType(0);
        initHospLevelList();
        initMenu();
    }

    private void initMenu(){
        chooseHospBean = CacheUtil.getInstance().getCacheHelper().getAsSerializable(CHOOSE_HOSP_BEAN);
        if (chooseHospBean == null){
            chooseHospBean = new ChooseHospBean(new CityListBean("","筛选城市"),new DropDownBean(0,"医院等级"));
        }

        cityCode = chooseHospBean.getCityChoose().getCityCode();
        level = chooseHospBean.getHospLevelChoose().getId();

        view.setCityTitle(chooseHospBean.getCityChoose().getName());
        view.setHospLevelTitle(chooseHospBean.getHospLevelChoose().getName());
    }

    private void initHospLevelList(){
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.ALL,"全部"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.ThreeOne,"三级甲等"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.ThreeTwo,"三级乙等"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.Three,"三级"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.TwoOne,"二级甲等"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.TwoTwo,"二级乙等"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.Two,"二级"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.OneOne,"一级甲等"));
        hospLevelList.add(new DropDownBean(TreatDocHospLevelGen.One,"一级"));
    }

    @Override
    public void start() {
        exitNext = true;
        getHttpData();
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.stopRefresh();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()" + e);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.stopRefresh();
                            }
                        }, 2000);
                       view.noNet();
                    }

                    @Override
                    public void onNext(TreatHospList treatHospList) {
                        Log.i("hcb", "http onNext()");

                        if (treatHospList.getData().size()>0){
                            view.exitData();
                            if (pageIndex == 1){
                                httpList.clear();
                                httpList = treatHospList.getData();
                            }else{
                                view.onPostLoadMore();
                                httpList.addAll(treatHospList.getData());
                            }
                            Log.i("hcbtest", "http size"+ httpList.size());
                            view.showData(httpList);
                        }else if (pageIndex > 1&&treatHospList.getData().size() ==0){
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
    public void setSelectAllCity(CityListBean bean) {
        chooseHospBean.setCityChoose(bean);
        CacheUtil.getInstance().getCacheHelper().put(CHOOSE_HOSP_BEAN, chooseHospBean);
    }


    @Override
    public List<DropDownBean> getHospLevelList() {
        return hospLevelList;
    }

    @Override
    public void setHospLevel(int position) {
        if (hospLevelList.size() >= position){
            exitNext = true;
            level = hospLevelList.get(position).getId();
            view.startRefresh();
            resetIndex();
            getHttpData();

            chooseHospBean.setHospLevelChoose(hospLevelList.get(position));
            CacheUtil.getInstance().getCacheHelper().put(CHOOSE_HOSP_BEAN, chooseHospBean);
        }
    }

    @Override
    public void setHospCityCode(int position) {
        if (httpCityList.size() >= position){
            exitNext = true;
            cityCode = httpCityList.get(position).getCityCode();
            view.startRefresh();
            resetIndex();
            getHttpData();

            chooseHospBean.setCityChoose(httpCityList.get(position));
            CacheUtil.getInstance().getCacheHelper().put(CHOOSE_HOSP_BEAN, chooseHospBean);
        }
    }

    @Override
    public void resetHospCityCode() {
        cityCode = "";
        exitNext = true;
        view.startRefresh();
        resetIndex();
        getHttpData();
    }

    @Override
    public void getHttpProvinceList() {
        HttpService.getHttpService().getProvinceList()
                .compose(RxUtil.<HttpResult<List<ProvinceListBean>>>normalSchedulers())
                .subscribe(new Subscriber<HttpResult<List<ProvinceListBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getHttpProvinceList onCompleted");
                        view.showProvince(provinceList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getHttpProvinceList onError " +e);
                        view.showProvince(provinceList);
                    }

                    @Override
                    public void onNext(HttpResult<List<ProvinceListBean>> listHttpResult) {
                        Log.i("hcb", "getHttpProvinceList onNext");
                        if (listHttpResult!=null && listHttpResult.errorCode == 0){
                            httpProvinceList.clear();
                            httpProvinceList.addAll(listHttpResult.getData());
                            provinceList.clear();
                            for ( ProvinceListBean provinceListBean:httpProvinceList){
                                DropDownBean dropDownBean = new DropDownBean();
                                dropDownBean.setId(1);
                                dropDownBean.setName(provinceListBean.getName());
                                provinceList.add(dropDownBean);
                            }
                        }
                    }
                });
    }

    @Override
    public void getHttpCityList(int position) {
        if (httpProvinceList.size()>=position){
            HttpService.getHttpService().getProvinceCityList(httpProvinceList.get(position).getProvinceCode())
                    .compose(RxUtil.<HttpResult<List<CityListBean>>>normalSchedulers())
                    .subscribe(new Subscriber<HttpResult<List<CityListBean>>>() {
                        @Override
                        public void onCompleted() {
                            Log.i("hcb", "getHttpCityList onCompleted ");
                            view.showCity(cityList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("hcb", "getHttpCityList onError " +e);
                            view.showCity(cityList);
                        }

                        @Override
                        public void onNext(HttpResult<List<CityListBean>> listHttpResult) {
                            Log.i("hcb", "getHttpCityList onNext ");
                            if (listHttpResult!=null && listHttpResult.errorCode == 0){
                                httpCityList.clear();
                                httpCityList.addAll(listHttpResult.getData());
                                cityList.clear();
                                for ( CityListBean cityListBean:httpCityList){
                                    DropDownBean dropDownBean = new DropDownBean();
                                    dropDownBean.setId(2);
                                    dropDownBean.setName(cityListBean.getName());
                                    cityList.add(dropDownBean);
                                }
                            }
                        }
                    });
        }
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
