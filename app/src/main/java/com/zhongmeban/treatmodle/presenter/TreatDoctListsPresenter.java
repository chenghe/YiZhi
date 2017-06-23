package com.zhongmeban.treatmodle.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.bean.DoctorList;
import com.zhongmeban.bean.DropDownBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.ChooseDoctBean;
import com.zhongmeban.bean.treatbean.ChooseHospBean;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.ProvinceListBean;
import com.zhongmeban.bean.treatbean.TreatDoctList;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.contract.TreatDoctListsContract;
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

public class TreatDoctListsPresenter implements TreatDoctListsContract.Presenter {

    public static final String CHOOSE_DOCT_BEAN = "ChooseDoctBean";

    private TreatDoctListsContract.View view;
    private Context mContext;
    private int hospLevel;
    private String cityCode = "";
    private int doctTitle;
    private String keywords = "";
    private Subscription mSubscription;
    private int pageIndex = 1;
    private int pageSize = 20;
    private int totalPage = 100;//总页数
    private TreatDoctor load = new TreatDoctor();
    private List<TreatDoctor> httpList = new ArrayList<TreatDoctor>();
    private List<DropDownBean> provinceList = new ArrayList<>();//省List
    private List<DropDownBean> cityList = new ArrayList<>();//城市List
    private List<ProvinceListBean>httpProvinceList = new ArrayList<>();//网络省列表
    private List<CityListBean> httpCityList = new ArrayList<>();//网络城市列表
    private List<DropDownBean> hospLevelList = new ArrayList<>();
    private List<DropDownBean> docLevelList = new ArrayList<>();
    private boolean exitNext = true;//判断是否有下一页
    private ChooseDoctBean chooseDoctBean;

    public TreatDoctListsPresenter(TreatDoctListsContract.View view, Context context) {
        this.view = view;
        mContext = context;
        this.view.setPresenter(this);
        load.setViewType(0);
        initHospLevelList();
        initDoctLevelList();
        initMenu();
    }

    private void initMenu(){
        chooseDoctBean = CacheUtil.getInstance().getCacheHelper().getAsSerializable(CHOOSE_DOCT_BEAN);
        if (chooseDoctBean == null){
            chooseDoctBean = new ChooseDoctBean(new CityListBean("","筛选城市"),
                    new DropDownBean(0,"医院等级"),new DropDownBean(0,"医生等级"));
        }

        cityCode = chooseDoctBean.getCityChoose().getCityCode();
        hospLevel = chooseDoctBean.getHospLevelChoose().getId();
        doctTitle = chooseDoctBean.getDoctLevelChoose().getId();

        view.setCityTitle(chooseDoctBean.getCityChoose().getName());
        view.setHospLevelTitle(chooseDoctBean.getHospLevelChoose().getName());
        view.setDoctLevelTitle(chooseDoctBean.getDoctLevelChoose().getName());
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

    private void initDoctLevelList(){
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.ALL,"全部"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL1,"主任医生"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL2,"副主任医生"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL3,"主治医生"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL4,"副主治医生"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL5,"住院医生"));
        docLevelList.add(new DropDownBean(TreatDocHospLevelGen.DOCLEVEL6,"其他医生"));
    }
    @Override
    public void start() {
        exitNext = true;
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
    public void setHospLevel(int position) {
        if (hospLevelList.size() >= position){
            exitNext = true;
            hospLevel = hospLevelList.get(position).getId();
            view.startRefresh();
            resetIndex();
            getHttpData();

            chooseDoctBean.setHospLevelChoose(hospLevelList.get(position));
            CacheUtil.getInstance().getCacheHelper().put(CHOOSE_DOCT_BEAN, chooseDoctBean);
        }
    }

    @Override
    public void setDoctTitle(int position) {
        if (docLevelList.size() >= position){
            exitNext = true;
            doctTitle = docLevelList.get(position).getId();
            view.startRefresh();
            resetIndex();
            getHttpData();

            chooseDoctBean.setDoctLevelChoose(docLevelList.get(position));
            CacheUtil.getInstance().getCacheHelper().put(CHOOSE_DOCT_BEAN, chooseDoctBean);
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

            chooseDoctBean.setCityChoose(httpCityList.get(position));
            CacheUtil.getInstance().getCacheHelper().put(CHOOSE_DOCT_BEAN, chooseDoctBean);
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
    public boolean exitNextPage() {
        return exitNext;
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.stopRefresh();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getHttpData onError" +e);;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.stopRefresh();
                            }
                        }, 2000);
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
    public List<DropDownBean> getHospLevelList() {
        return hospLevelList;
    }

    @Override
    public List<DropDownBean> getDoctLevelList() {
        return docLevelList;
    }

    @Override
    public void setSelectAllCity(CityListBean bean) {
        chooseDoctBean.setCityChoose(bean);
        CacheUtil.getInstance().getCacheHelper().put(CHOOSE_DOCT_BEAN, chooseDoctBean);
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

}
