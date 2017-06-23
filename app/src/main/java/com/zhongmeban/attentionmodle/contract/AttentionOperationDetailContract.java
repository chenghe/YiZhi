package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

/**
 * 手术详情Contract
 * Created by Chengbin He on 2017/1/5.
 */

public interface AttentionOperationDetailContract {

    interface View extends BaseMvpView<Presenter> {
        void dismissProgressDialog();

        void setOperationName(String name);

        void setDoctName(String name);

        void setHospName(String name);

        void setTime(String time);

        void setOther(String name);

        void setNotes(String notes);

        /**
         * 无同期手术
         */
        void otherGone();

        /**
         * 存在同期手术
         */
        void otherVisable();
    }

    interface Presenter extends BaseMvpPresenter {
        /**
         * 删除手术记录
         */
        void deleteOperation();

        /**
         * 编辑手术信息
         * @param intent
         * @return
         */
        Intent editOperation(Intent intent);

        /**
         * 是否与住院关联
         * @return
         */
        boolean exitHospitalize();
    }
}
