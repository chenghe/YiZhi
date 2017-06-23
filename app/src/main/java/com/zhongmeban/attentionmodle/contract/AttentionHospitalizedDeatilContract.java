package com.zhongmeban.attentionmodle.contract;

import android.content.Intent;

import com.zhongmeban.base.BaseMvpPresenter;
import com.zhongmeban.base.BaseMvpView;

/**
 * Created by Chengbin He on 2016/12/27.
 */

public interface AttentionHospitalizedDeatilContract {

    interface View extends BaseMvpView<Presenter>{

        /**
         * 设置入院时间
         * @param inTime
         */
        void setInTime(String inTime);

        /**
         * 设置入院目的
         * @param typeName
         */
        void setHospType(String typeName);

        /**
         * 入院备注
         * @param notes
         */
        void setNotes(String notes);

        /**
         * 医院名称
         * @param hospName
         */
        void setHospName (String hospName);

        void dismissProgressDialog();

        void surgeryVisable();

        void surgeryGone();

        /**
         * 设置手术信息
         * @param name
         */
        void setSurgery(String name);

        /**
         * 设置主刀医生
         */
        void setSurgeryDoct(String name);

        void setDays(String days);
        void setDoctName(String name);

    }

    interface Presenter extends BaseMvpPresenter {

        /**
         * 删除住院记录
         */
        void deleteHospitalized();

        /**
         * 是否存在手术
         * @return
         */
        boolean exitSurgery();


        /**
         * 编辑住院信息
         * @param intent
         * @return
         */
        Intent editHospitalized(Intent intent);
    }
}
