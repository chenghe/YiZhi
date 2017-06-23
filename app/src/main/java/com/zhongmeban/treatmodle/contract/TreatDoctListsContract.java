package com.zhongmeban.treatmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.bean.DropDownBean;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.TreatDoctor;

import java.util.List;

/**
 * 医生列表contract
 * Created by Chengbin He on 2016/12/2.
 */

public interface TreatDoctListsContract {

    interface View extends BaseMvpView<Presenter>{

        void startRefresh();
        void stopRefresh();
        /**
         *有网络数据
         */
        void exitData();
        /**
         *无数据
         */
        void noData();
        /**
         *无网络
         */
        void noNet();

        /**
         * 上拉加载
         */
        void onPostLoadMore();

        /**
         * 显示网络数据
         */
        void showData(List<TreatDoctor> httpList);

        void showProvince(List<DropDownBean> provinceList);
        void showCity(List<DropDownBean> cityList);
        void setCityTitle(String title);
        void setHospLevelTitle(String title);
        void setDoctLevelTitle(String title);
    }

    interface Presenter extends BaseMvpPresenter {

        void getHttpData();
        List<DropDownBean> getHospLevelList();
        List<DropDownBean> getDoctLevelList();

        void setSelectAllCity(CityListBean bean);
        /**
         * 设置医院等级
         * @param position
         */
        void setHospLevel(int position);

        /**
         * 设置医生等级
         * @param position
         */
        void setDoctTitle(int position);

        /**
         * 设置城市
         * @param position
         */
        void setHospCityCode(int position);

        /**
         * 重置选择城市
         */
        void resetHospCityCode();


        /**
         * 获取全部省列表
         */
        void getHttpProvinceList();

        /**
         * 获取全部市列表
         */
        void getHttpCityList(int position);

        void destory();
        void nextIndex();//下一页
        void resetIndex();

        /**
         * 进入医生详情
         * @param intent
         * @return
         */
        Intent startDoctDetail(Intent intent, int position);

        /**
         * 获取加载完成网络List
         */
        List<TreatDoctor> getHttpList();

        /**
         * 加入加载更多progressBar
         */
        void addLoadMore();
        /**
         * 移除加载更多progressBar
         */
        void removeLoadMore();

        /**
         * 判断是否有下一页
         * @return
         */
        boolean exitNextPage();
    }
}
