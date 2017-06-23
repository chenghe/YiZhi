package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

/**
 * 手术详情Contract
 * Created by Chengbin He on 2017/1/5.
 */

public interface AttentionRadiotherapyDetailContract {

    interface View extends BaseMvpView<Presenter> {
        void dismissProgressDialog();


        void setDose(String name);

        void setTimesCount(String name);

        void setStartTime(String time);

        void setWeeksCount(String name);

        void setNotes(String notes);

    }

    interface Presenter extends BaseMvpPresenter {
        /**
         * 删除放疗
         */
        void deleteRadiotherapy();

        /**
         * 编辑放疗
         * @param intent
         * @return
         */
        Intent editRadiotherapy(Intent intent);
    }
}
