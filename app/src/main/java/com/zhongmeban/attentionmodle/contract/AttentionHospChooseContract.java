package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;
import com.zhongmeban.bean.DropDownBean;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;

import java.util.List;

/**
 * 医院查询contract
 * Created by Chengbin He on 2016/12/2.
 */

public interface AttentionHospChooseContract {

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
        void showData(List<TreatHospitalScource> httpList);
        void showProvince(List<DropDownBean> provinceList);
        void showCity(List<DropDownBean> cityList);
        void setCityTitle(String title);
        void setHospLevelTitle(String title);

    }

    interface Presenter extends BaseMvpPresenter {

        void getHttpData();
        void destory();
        void nextIndex();//下一页
        void resetIndex();

        void setSelectAllCity(CityListBean bean);
        List<DropDownBean> getHospLevelList();

        /**
         * 设置医院等级
         * @param position
         */
        void setHospLevel(int position);

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

        /**
         * 进入医院详情
         * @param intent
         * @return
         */
        Intent startHospDetail(Intent intent, int position);

        /**
         * 完成筛选返回
         * @param intent
         * @return
         */
        Intent finishChoose(Intent intent, int position);

        /**
         * 完成筛选返回
         * @param intent
         * @return
         */
        Intent finishChoose(Intent intent);

        /**
         * 获取加载完成网络List
         */
        List<TreatHospitalScource> getHttpList();

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
