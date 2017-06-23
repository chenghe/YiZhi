package com.zhongmeban.treatmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;

import java.util.List;

/**
 * 医生搜索Contract
 * Created by Chengbin He on 2016/12/9.
 */

public interface TreatSearchDoctListContract {

    interface View extends BaseMvpView<Presenter> {
        /**
         * 有网络数据
         */
        void exitData();

        /**
         * 无数据
         */
        void noData();
        /**
         * 无网络
         */
        void noNet();

        /**
         * 显示progress
         */
        void showProgress();

        /**
         * 上拉加载
         */
        void onPostLoadMore();

        /**
         * 显示网络数据
         */
        void showData(List<TreatDoctor> httpList);
    }

    interface Presenter extends BaseMvpPresenter {
        void getHttpData();

        void setSearchKeyword(String keyword);

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
         *
         * @return
         */
        boolean exitNextPage();
    }
}
