package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;

/**
 * 新增手术Contract
 * Created by Chengbin He on 2017/1/4.
 */

public interface AttentionAddOperationContract {

    interface View extends BaseMvpView<Presenter> {
        void dismissProgressDialog();

        void setTime(String time);

        void setDoctName(String name);

        void setNotes(String notes);

        void setHospName(String name);

        void setTitleName(String titleName);

        void setTherapeuticName(String name);

        void showToast(String name);

        void setSurgeryMethords(List<SurgeryMethods> mChooseMethods);
    }

    interface Presenter extends BaseMvpPresenter {
        /**
         * 提交网络数据
         */
        void commitData();

        String getHospName();

        void setHospName(String name);

        int getHospId();

        void setHospId(int id);

        String getTherapeuticName();

        void setTherapeuticName(String name);

        int getTherapeuticId();

        void setTherapeuticId(int id);

        void setTime(long time);

        void setDoctName(String name);

        void setNotes(String notes);

        void setChooseMethods(List<SurgeryMethods> chooseMethods);

        void removeChooseMethodsPosition(int position );

        Intent chooseMethods(Intent intent);

        boolean canCommit();
    }
}
