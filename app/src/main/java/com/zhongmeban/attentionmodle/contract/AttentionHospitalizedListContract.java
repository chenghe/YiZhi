package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.treatmodle.contract.TreatHospListsContract;

import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;

/**
 * 住院列表Contract
 * Created by Chengbin He on 2016/12/27.
 */

public interface AttentionHospitalizedListContract {

    interface View extends BaseMvpView<Presenter>{

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
         * 显示数据库数据
         */
        void showData(List<Hospitalized> dbList);
    }


    interface Presenter extends BaseMvpPresenter {

        void getData();


        /**
         * 进入医院详情
         * @param intent
         * @return
         */
        Intent startHospitalizedDetail(Intent intent, int position);

    }
}
