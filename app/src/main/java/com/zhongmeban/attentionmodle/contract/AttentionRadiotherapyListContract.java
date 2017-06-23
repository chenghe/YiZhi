package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

import java.util.List;

import de.greenrobot.dao.attention.Radiotherapy;
import de.greenrobot.dao.attention.SurgerySource;

/**
 * 放疗列表Contract
 * Created by Chengbin He on 2016/12/27.
 */

public interface AttentionRadiotherapyListContract {

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
        void showData(List<Radiotherapy> dbList);
    }


    interface Presenter extends BaseMvpPresenter {

        void getData();


        /**
         * 进入放疗详情
         * @param intent
         * @return
         */
        Intent startRadiotherapyDetail(Intent intent, int position);

    }
}
