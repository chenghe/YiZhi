package com.zhongmeban.utils;

import android.content.Context;
import com.umeng.analytics.MobclickAgent;

public class UmengUtils {


	public static class JelweBox {
		private static final String EVENT_NAME = "JelweBoxClick";

		private static void onEvent(Context ctx, String label) {
			MobclickAgent.onEvent(ctx, EVENT_NAME, label);
		}

		public static void clickJelweBox(Context context,String name) {
			onEvent(context, name);
		}

	}
	public static class TabEvent {
		private static final String EVENT_NAME = "TabClick";

		private static void onEvent(Context ctx, String label) {
			MobclickAgent.onEvent(ctx, EVENT_NAME, label);
		}

		public static void clickTabIndex(Context context,String name) {
			onEvent(context, name);
		}

	}

	public static class ShareEvent{
		private static final String EVENT_NAME = "ShareEvent";

		private static void onEvent(Context ctx, String label) {
			MobclickAgent.onEvent(ctx, EVENT_NAME, label);
		}

		public static void clickTabIndex(Context context,String name) {
			onEvent(context, name);
		}
	}

	public static void pipEventClick(Context context, String label) {
		MobclickAgent.onEvent(context, "pipEventClick", label);
	}

	public static void pipEventDown(Context context, String label) {
		MobclickAgent.onEvent(context, "pipEventDown", label);
	}



}
