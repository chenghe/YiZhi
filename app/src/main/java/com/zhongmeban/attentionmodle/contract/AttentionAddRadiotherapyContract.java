package com.zhongmeban.attentionmodle.contract;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

/**
 * 新增放疗记录Contract
 * Created by Chengbin He on 2017/1/9.
 */

public interface AttentionAddRadiotherapyContract {

    interface View extends BaseMvpView<Presenter> {
        void dismissProgressDialog();

        void setStartTime(String time);

        void setResultType(int resultType);

        void setDose(String dose);

        void setWeekCount(String weekCount);

        void setTimeCount(String daysCount);

        void setNotes(String notes);

        void showToast(String name);
    }

    interface Presenter extends BaseMvpPresenter {
        /**
         * 提交网络数据
         */
        void commitData();

        void setStartTime(long time);

        void setResultType(int resultType);

        void setDose(String dose);

        void setWeekCount(String weekCount);

        void setTimeCount(String daysCount);

        void setNotes(String notes);

        boolean canCommit();
    }
}
