package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.GridViewItem;

import java.util.List;

public class GridAdapter extends BaseAdapter {
//	private static final String TAG = "MyAdapter";
	private List<GridViewItem> myData;
	private int mCount;
	private Context mContext;

	public GridAdapter(int count, Context context, List<GridViewItem> myData) {
		this.mCount = count;
		this.mContext = context;
		this.myData = myData;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	public List<GridViewItem> getMyData() {
		return myData;
	}

	public void setMyData(List<GridViewItem> myData) {
		this.myData = myData;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_grid, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextName = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mTextName.setText(myData.get(position).getName());
		viewHolder.mImageView.setImageResource(myData.get(position).getImageId());
		return convertView;
	}

	static class ViewHolder {
		TextView mTextName;
		ImageView mImageView;
	}

}
