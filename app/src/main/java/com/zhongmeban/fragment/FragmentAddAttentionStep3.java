package com.zhongmeban.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAddAttention;
import com.zhongmeban.base.BaseFragment;

/**
 * 新增患者Fragment第三步
 * Created by Chengbin He on 2016/6/21.
 */
public class FragmentAddAttentionStep3 extends BaseFragment{
    private ActivityAddAttention parentActivity;
    private Button button;

    public static FragmentAddAttentionStep3 newInstance() {
        Bundle args = new Bundle();
        
        FragmentAddAttentionStep3 fragment = new FragmentAddAttentionStep3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAddAttention) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_attention_step3,container,false);
        return view;
    }

}
