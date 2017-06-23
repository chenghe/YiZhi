package com.zhongmeban.utils;

import android.view.View;
import android.widget.TextView;

import com.zhongmeban.R;


/**
 * @author sf layouttitle 满足不同title
 */
public class LayoutActivityTitle {
	private View titleLayout;
//	protected AgentApp app;
	private String rightTitle="";

	public LayoutActivityTitle(View v) {
		this.titleLayout = v;
	}

	/**
	 * 设置 title 文字
	 * 
	 * @param resid
	 */
	public void setTitleText(int resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slideTitleName);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	/**
	 * 设置 title 文字
	 * 
	 * @param resid
	 */
	public void setTitleText(String resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slideTitleName);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	/**
	 * 显示返回按钮
	 * 
	 *
	 */
	public void backSlid(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.ivTitleBtnback);
		View left = titleLayout.findViewById(R.id.left_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(listener);
		left.setOnClickListener(listener);

	}

	/**
	 * 回到主slid
	 * 
	 *
	 */
	public void backSlidMain(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.ivTitleBtnback);
		View left = titleLayout.findViewById(R.id.left_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(listener);
		left.setOnClickListener(listener);
	}

	/**
	 * 进入im
	 * 
	 *
	 */
	public void backIm(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}

		View back = titleLayout.findViewById(R.id.ivTitleBtnIm);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		right_button.setOnClickListener(listener);

	}
	
	/**
	 * 
	 */
	public void HiddenIsRead(Boolean Isr) {
		if (titleLayout == null) {
			return;
		}

		View isRead = titleLayout.findViewById(R.id.is_notice);
		if (isRead == null) {
			return;
		}
		if(Isr)
		{
			isRead.setVisibility(View.VISIBLE);
		}
		else {
			isRead.setVisibility(View.GONE);
		}
		
	}

	/**
	 * 隐藏IM入口
	 */
	public void HiddenbackIm() {
		if (titleLayout == null) {
			return;
		}

		View back = titleLayout.findViewById(R.id.ivTitleBtnIm);
		View right_button = titleLayout.findViewById(R. id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.GONE);

	}

	/**
	 * 新建项目
	 * 
	 *
	 */
	public void slidPlus(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.slid_plus);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		right_button.setOnClickListener(listener);

	}

	/**
	 * 设置置顶
	 *
	 */
	public void header(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.header);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		right_button.setOnClickListener(listener);
	}

	/**
	 * 项目状态
	 * 
	 *
	 */
	public void slidestate(int resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_state);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
		// title_txt.setTextColor(0xff51c4d4);
	}

	public void slidestate(String resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_state);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	public String getSlidestate() {
		if (titleLayout == null) {
			return null;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_state);
		return title_txt.getText().toString();
	}

	/**
	 * 项目选择状态
	 * 
	 *
	 */

	public void slidstateic(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.change_state);
		View middle = titleLayout.findViewById(R.id.middle_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		middle.setOnClickListener(listener);

	}

	/**
	 * 中间title文字
	 * 
	 * @param
	 */
	public void slideCentertext(int resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_center_title);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	public void slideCentertext(String resid) {
		if (titleLayout == null) {
			return;
		}
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_center_title);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	/**
	 * 右侧title文字
	 * 
	 * @param
	 */

	public void slideRighttext(String resid, View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		rightTitle = resid;
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_right_text);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		right_button.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
		right_button.setOnClickListener(listener);
	}

	public void setRightGone(){
		View right_button = titleLayout.findViewById(R.id.right_button);
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_right_text);
		title_txt.setText("");
		right_button.setVisibility(View.GONE);
	}

	public void slideRighttext(String resid) {
		if (titleLayout == null) {
			return;
		}
		rightTitle = resid;
		TextView title_txt = (TextView) titleLayout
				.findViewById(R.id.slid_right_text);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (title_txt == null) {
			return;
		}
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText(resid);
	}

	/**
	 * 隐藏标题头下侧的线
	 * 
	 *
	 */

	public void enableTitleline() {
		if (titleLayout == null) {
			return;
		}
		View line = titleLayout.findViewById(R.id.title_line);
		if (line == null) {
			return;
		}
		line.setVisibility(View.INVISIBLE);

	}

	/**
	 * 我的钱 触发菜单项
	 * 
	 *
	 */
	public void slidMoneyMenu(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.slid_money_menu);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		right_button.setOnClickListener(listener);

	}

	/**
	 * 我的设置 出发菜单
	 * 
	 *
	 */
	public void slidSetMenu(View.OnClickListener listener) {

		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.slid_person_menu);
		View left = titleLayout.findViewById(R.id.left_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(listener);
		left.setOnClickListener(listener);
	}

	/**
	 * 我的钱 触发菜单项
	 * 
	 *
	 */
	public void setAtten(View.OnClickListener listener) {
		if (titleLayout == null) {
			return;
		}
		View back = titleLayout.findViewById(R.id.maintitle_set);
		View right_button = titleLayout.findViewById(R.id.right_button);
		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		right_button.setOnClickListener(listener);

	}

	public void setRightTitle(String rightTitle){
			this.rightTitle = rightTitle;
	}

	public String getRightTitle(){
		return rightTitle;
	}


}
