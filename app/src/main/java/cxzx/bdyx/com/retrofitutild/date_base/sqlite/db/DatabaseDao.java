package cxzx.bdyx.com.retrofitutild.date_base.sqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cxzx.bdyx.com.retrofitutild.date_base.sqlite.bean.JsonBean;
import cxzx.bdyx.com.retrofitutild.utils.Base64;
import cxzx.bdyx.com.retrofitutild.utils.TimeUtils;

public class DatabaseDao {
    private DateDBOpenHelper helper;
    private SQLiteDatabase db;
    private String db_name;//样式 json_01000368_4_1_1   channel='4' 渠道; staffNumber='01042547' 工号; orgCode='1' 分公司; oa ="1" （外勤）;oa="0"（内勤）
    private String table = "json";

    public DatabaseDao(Context context, String name) {
        db_name = name + ".db";
        helper = new DateDBOpenHelper(context.getApplicationContext(), db_name);
    }


    /**
     * 向数据库中添加数据
     *
     * @param json
     */
    public void addJson(String tag, String json) {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put("tag", tag);
        values.put("json", Base64.encode(json.getBytes()));/*对 json 加密 */
        values.put("date", TimeUtils.getCurrentTime());
        db.insert("json", null, values);
        db.close();
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    public ArrayList<JsonBean> findAllJson() {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
        ArrayList<JsonBean> list = new ArrayList<JsonBean>();
        Cursor cursor = db.rawQuery("select * from json order by _id desc", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            JsonBean hn = new JsonBean();
            hn.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            hn.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            hn.setId(cursor.getInt(cursor.getColumnIndex("id")));
            hn.setJson(new String(Base64.decode(cursor.getString(cursor.getColumnIndex("json")))));/*解密*/
            hn.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            hn.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            hn.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            list.add(hn);
        }
        cursor.close();
        db.close();
        return list;
    }


    /**
     * 通过 tag查询数据
     *
     * @return
     */
    public ArrayList<JsonBean> findJsonByTag(String tag) {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
        ArrayList<JsonBean> list = new ArrayList<JsonBean>();

        Cursor cursor = db.query("json", null, "tag=?", new String[]{tag}, null, null, "_id");
        //3.遍历结果集
        while (cursor.moveToNext()) {
            JsonBean hn = new JsonBean();
            hn.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            hn.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            hn.setId(cursor.getInt(cursor.getColumnIndex("id")));
            hn.setJson(new String(Base64.decode(cursor.getString(cursor.getColumnIndex("json")))));
            hn.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            hn.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            hn.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            list.add(hn);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 通过 id 查询数据
     *
     * @return
     */
    public ArrayList<JsonBean> findJsonById(String id) {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
        ArrayList<JsonBean> list = new ArrayList<JsonBean>();

        Cursor cursor = db.query("json", null, "id=?", new String[]{id}, null, null, "_id");
        //3.遍历结果集
        while (cursor.moveToNext()) {
            cursor.moveToNext();
            JsonBean hn = new JsonBean();
            hn.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            hn.setDate(cursor.getLong(cursor.getColumnIndex("date")));
            hn.setId(cursor.getInt(cursor.getColumnIndex("id")));
            hn.setJson(new String(Base64.decode(cursor.getString(cursor.getColumnIndex("json")))));
            hn.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            hn.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            hn.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            list.add(hn);
        }
        cursor.close();
        db.close();
        return list;
    }


    /**
     * 删除 json 表数据通过 tag
     */
    public void deleteJsonByTag(String tag) {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
//        db.execSQL("delete from json where tag =? ;",new String[]{tag});
        db.delete("json", "tag=?", new String[]{tag});
        db.close();
    }

    /**
     * 删除 json 表数据通过 id
     */
    public void deleteJsonById(String id) {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
//        db.execSQL("delete from json where id = ?;",new String[]{id});
        db.delete("json", "id=?", new String[]{id});

        db.close();
    }

    /**
     * 删除json表所有数据
     */
    public void deleteAllJson() {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
//        db.execSQL("delete from json;");
        db.delete("json", null, null);
        db.close();
    }


}
