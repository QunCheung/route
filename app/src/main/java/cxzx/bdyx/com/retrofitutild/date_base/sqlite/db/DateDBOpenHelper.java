package cxzx.bdyx.com.retrofitutild.date_base.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateDBOpenHelper extends SQLiteOpenHelper {


    /**
     * 数据库创建的构造方法 数据库名称 .db ，版本号为1
     * <p>
     * 不同的渠道  采用不同的 .db  获取 json 串 采用 相同的 json
     *
     * db 的数据命名
     *
     * @param context
     */
    public DateDBOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    /**
     * 初始化数据库的表结构
     * <p>
     * json 表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists json (_id integer primary key autoincrement , id integer ,tag char(50) not null,userId  char(50) , date long,json text not null,url text)");

    }

    /**
     * 数据库版本升级时调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
