package com.zhongmeban.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON转换类
 */
public class JsonUtils {

	/**
	 * JSON转对象
	 * 
	 * @param json
	 *            JSON字符串
	 * @param clazz
	 *            对象类型
	 * @return 对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			Gson gson = new Gson();
			T obj = gson.fromJson(json, clazz);
			return obj;
		} catch (Throwable e) {
			MyLog.e(e.getMessage());
		}

		return null;

	}

	/**
	 * JSON转对象
	 * 
	 * Type type = new TypeToken<List<类>>() {}.getType();
	 * 
	 * @param json
	 *            JSON字符串
	 * @param Type
	 *            对象类型
	 * @return 对象
	 */
	public static <T> List<T> fromJson(String json, Type typeOfT) {
		try {

			Gson gson = new Gson();
			List<T> obj = gson.fromJson(json, typeOfT);
			return obj;

		} catch (Throwable e) {
			MyLog.e(e.getMessage());
		}
		return null;
	}

	//将已知的json数组转换成列表
	public static <T> ArrayList<T> stringToList(String jsArrStr, Type typeOfT) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsArrStr).getAsJsonArray();
		ArrayList<T> lcs = new ArrayList<>();
		for (JsonElement obj : array) {
			T cse = gson.fromJson(obj, typeOfT);
			lcs.add(cse);
		}
		return lcs;
	}

	/**
	 * 对象对JSON
	 * 
	 * @param obj
	 *            对象
	 * @return json字符串
	 */
	public static <T> String toJson(T obj) {

		try {

			Gson gson = new Gson();
			String json = gson.toJson(obj);

			return json;

		} catch (Throwable e) {
			MyLog.e(e.getMessage());
		}

		return null;
	}

	/**
	 * Json 转成 Map<>
	 *
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, String> getMapForJson(String jsonStr) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonStr);

			Iterator<String> keyIter = jsonObject.keys();
			String key;
			String value;
			Map<String, String> valueMap = new HashMap<>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = (String) jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
