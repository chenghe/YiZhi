package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;

/**
 * 住院新增手术Contract
 * Created by Chengbin He on 2017/1/4.
 */

public interface AttentionAddHospOperationContract {

    interface View extends BaseMvpView<Presenter> {

        void setTime(String time);

        void setDoctName(String name);

        void setNotes(String notes);

        void setHospName(String name);

        void setTitleName(String titleName);

        void setTherapeuticName(String name);

        void setSurgeryMethords(List<SurgeryMethods> mChooseMethods);

        void showToast(String name);
    }

    interface Presenter extends BaseMvpPresenter {

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

        void removeChooseMethodsPosition(int position);

        Intent chooseMethods(Intent intent);

        /**
         * 回到住院界面
         * @param intent
         * @return
         */
        Intent backHospitalized(Intent intent);
        boolean canCommit();

    }
}
