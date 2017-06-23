package com.zhongmeban.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;

/**
 * @author Administrator
 * @ClassName:
 * @Description:
 * @date 2015/12/25 19:23
 * 邮箱：cchen@ideabinder.com
 */
public abstract class BaseFrag extends Fragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        TextView textView = (TextView) view.findViewById(R.id.txt_content);
        textView.setText(initContent());
        return view;
    }

    public abstract String initContent();

    public abstract void initWidgetAndLisenter();

}
