package com.zhongmeban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class IBFragPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;

	public IBFragPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}


	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}


	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
}