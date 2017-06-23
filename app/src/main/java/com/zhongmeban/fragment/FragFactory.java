package com.zhongmeban.fragment;

import android.support.v4.app.Fragment;
import com.zhongmeban.mymodle.fragment.FragmentMy;
import com.zhongmeban.shopmodle.fragment.ShopFragment;

public class FragFactory {
	
	// 主页的底部导航菜单栏的选项
	public static final String ATTENTION_TAG = AttentionFragment.class.getSimpleName();
	public static final String TREATMENT_TAG = FragHome.class.getSimpleName();
	public static final String SHOP_TAG = ShopFragment.class.getSimpleName();
	//	public static final String FOUR_TAG = "me";
//	public static final String FIVE_TAG = "more";
	public static final String SETTING_TAG = FragmentMy.class.getSimpleName();
	
	// 根据tag值去创建不同的fragment对象
	public static Fragment getInstanceByTag(String tag) {  
        Fragment fragment = null;
		if (ATTENTION_TAG.equals(tag)) {//关注
			fragment = new AttentionFragment();
		} else if (TREATMENT_TAG.equals(tag)) {//治疗首页
			fragment = new FragHome();
		} else if (SHOP_TAG.equals(tag)) {//购物
			fragment = new ShopFragment();
		} else if (SETTING_TAG.equals(tag)) {//设置
			fragment = new FragmentMy();
		}
        return fragment;  
    }  
	
}
