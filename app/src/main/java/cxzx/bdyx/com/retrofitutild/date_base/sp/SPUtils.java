package cxzx.bdyx.com.retrofitutild.date_base.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import cxzx.bdyx.com.retrofitutild.utils.Base64;

/**
 * Created by houyongliang on 2018/3/28 09:59.
 * Function(功能):
 * 采用单例模式
 *
 * 提供 保存bean （json base64加密）的方法 saveBean2SP
 * 提供 获取bean （json base64 解密）的方法 saveBean2SP
 * 提供 移除 bean 的方法  remove
 * 上面三个方法 已 bean 的名字 （bean.getClass().getSimpleName()） 为key 来处理逻辑
 */

public class SPUtils {

    private SPUtils() {
    }

    private static class Bulid {
        private static final SPUtils single = new SPUtils();
    }

    public static SPUtils getInstance() {
        return SPUtils.Bulid.single;
    }

    /**
     * 保存在手机里面的文件名
     */
    private static String FILE_NAME = "default_data";

    /**
     * 提供设置 fileName 的方法
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public void remove(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public void clear(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public boolean contains(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    /**
     * 将对象转为 json ,json 转为 base64 ,保存
     *
     * @param context
     * @param t       为 tClass 的对象
     * @param <T>
     */
    public <T> void saveBean2SP(Context context, T t) {
        /*非空判断*/
        if (t == null) {
            return;
        }
        //创建sp对象,如果有key为"SP_CARDINFO"的sp就取出，否则就创建一个此key的sp对象
        Gson gson = new Gson();
        String jsonStr = gson.toJson(t);//将对象转换成Json
        put(context, t.getClass().getSimpleName(), Base64.encode(jsonStr.getBytes()));
    }

    /**
     * 提供直接获取 Bean 的方法
     * 获取的json 串 解密后，转为 T 类型
     *
     * @param context
     * @param tClass  默认值 为""
     * @param <T>
     * @return
     */
    public <T> T getBeanFromSP(Context context, Class<T> tClass) {
        /*获取 json 串*/
        String json = (String) get(context, tClass.getSimpleName(), "");
        if (!TextUtils.isEmpty(json))//非空判断
        {
            Gson gson = new Gson();
            T t = gson.fromJson(new String(Base64.decode(json)), tClass);
            return t;
        }
        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param tClass  默认存入的 class name字段
     */
    public <T> void remove(Context context, Class<T> tClass) {
        remove(context, tClass.getSimpleName());
    }

}
