package cxzx.bdyx.com.retrofitutild.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by houyongliang on 2018/3/27 14:34.
 * Function(功能): 时间的工具类
 */

public class TimeUtils {

    /**
     * 获取当前的时间
     *
     * @return long 类型时间
     */
    public static long getCurrentTime() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 将 yyyy-MM-dd 格式时间转为 long 类型时间
     *
     * @param time
     * @return
     */
    public static long getLongTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }
    /**
     * 将 yyyy年MM月dd日 格式时间转为 long 类型时间
     *
     * @param time
     * @return
     */
    public static long getLongTime2(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }


}
