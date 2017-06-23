package com.zhongmeban.utils;

import android.text.TextUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 */
public class StringUtils {

	private static String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
	/**
     * 匹配短信中间的4个数字（验证码等）
     * 
     * @param patternContent 待校验的内容
     * @return
     */
    public static String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     *
     * @Title: getListString
     * @Description: 分割字符串为一个数组
     * @param  str		待分割的字符串
     * @param  c	分割字符
     * @return List<String>    返回类型
     * @throws
     */
    public static List<String> getListString(String str,String c){
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str,c);
        while(st.hasMoreTokens() ){
            list.add(st.nextToken());
        }
        return list;
    }

    /**
     * @Title: stringFilter
     * @Description: 清除掉所有特殊字符 ,过滤特殊字符 ,只允许字母和数字  "[^a-zA-Z0-9]"
     * @param  str	待处理字符串
     * @return String  过滤特殊字符串后的新字符串
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str)throws PatternSyntaxException {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("").trim();
    }

    /**
     * @Title: timerFormatConversion
     * @Description: 已既定的格式获取当前时间
     * @return String    当前时间新格式
     */
    public static String timerFormatConversion(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return timeFormat.format(new Date());
    }

    /**
     * 参数拼接
     * @param url
     * @param params
     * @return
     */
    public static String paramsSplicling(String url,Map<String, String> params){
        String param = "?";
        if(params!=null){
            for (String key : params.keySet()) {
                param += key + "=";
                param += URLEncoder.encode(params.get(key) + "")+ "&";
            }
            param = param.substring(0, param.length()-1);
        }
        if(param.length()>1){
            url += param;
        }
        return url;
    }

    //将list中的String类型拼接成字符串,间隔为逗号
    public static String getListContent(List<String> lists) {
        String str = ",";
        return getStrListContent(lists,str);
    }

    //将list中的String类型拼接成字符串,后面的str为间隔方式
    public static String getStrListContent(List<String> lists, String str){
        StringBuffer sb = new StringBuffer();
        if (0 == lists.size()) {
            return "";
        } else {
            for (int i = 0; i < lists.size(); i++) {
                if (i == 0) {
                    sb.append(lists.get(i));
                } else {
                    sb.append(str);
                    sb.append(lists.get(i));
                }
            }
            return sb.toString();
        }
    }


    /**
     * 格式化时间工具
     *
     * @param date
     * @return
     */
    public static String formateDate(long date) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  可以//yyyy年MM月dd日 HH点mm分ss秒
        String res = sdformat.format(new Date(date + 0));
        return res;
    }


    /**
     * 将字符串数组转换成list集合
     *
     * @param strs
     * @return
     */
    public static List<String> transforStrToList(String[] strs) {
        List<String> lists = Arrays.asList(strs);
        return lists;
    }

    /**
     * 手机验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[7])|(18[0,0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 名称是否正确
     * @param name
     * @return
     */
    public static boolean checkName(String name) {
        Pattern p = Pattern
                .compile("^[\\u4E00-\\u9FA5A-Za-z]+$");
        Matcher m = p.matcher(name);

        return m.matches();
    }

    //判断是否仅仅是中文和英文，否则返回flase
    public static boolean matchChart(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        }
        char[] chars = content.toCharArray();
        for (char c : chars) {
            if (isChinese(c) || Character.isLetter(c)) {
            } else {
                return false;
            }
        }

        return true;
    }

    //判断是不是中文
    private static boolean isChinese(char c) {
        boolean result = false;
        if ((c >= 0x4e00) && (c <= 0x9fbb)) {
            result = true;
        }
        return result;
    }




}
