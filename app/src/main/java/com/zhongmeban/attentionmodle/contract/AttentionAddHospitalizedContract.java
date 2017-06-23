package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;
import com.zhongmeban.bean.postbody.SurgeryRecordItem;

import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;

/**
 * Created by Chengbin He on 2017/1/3.
 */

public interface AttentionAddHospitalizedContract {

     interface View extends BaseMvpView<Presenter>{

         void dismissProgressDialog();

         void setIntime(String time);

         void setDays(String days);

         void setDoctName(String name);

         void setPurposeType(int type);

         void setNotes(String notes);

         void setHospName(String name);

         void setTitleName(String titleName);

         void setSurgeryName(String name);

         void showToast(String name);

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

        void setIntime(long time);

        void setDays(String days);

        void setDoctName(String name);

        void setPurposeType(int type);

        void setNotes(String notes);

        /**
         * 创建手术
         * @param intent
         * @return
         */
        Intent addSurgery(Intent intent);


        void getAddHospOperationResultData(Intent data);
        boolean canCommit();
        boolean changePurposeType();
        int getBeforePurposeType();
        String getTherapeuticName();
    }
}
