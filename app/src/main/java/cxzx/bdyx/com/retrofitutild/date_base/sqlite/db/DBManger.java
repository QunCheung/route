package cxzx.bdyx.com.retrofitutild.date_base.sqlite.db;

import android.content.Context;

/**
 * Created by houyongliang on 2018/3/27 11:20.
 * Function(功能):
 */

public class DBManger {
    /*单例模式*/
    private static DBManger mDBManger = null;
    private DatabaseDao dao;
    private String dbName;

    private DBManger() {
    }

    public static DBManger getInstance() {
        if (mDBManger == null) {
            synchronized (DBManger.class) {
                if (mDBManger == null) {
                    mDBManger = new DBManger();
                }
            }
        }
        return mDBManger;
    }


    /**
     * 数据库操作对象
     *
     * @param context
     * @param name    数据库库名 样式 json_01000368_4_1_1   channel='4' 渠道; staffNumber='01042547' 工号; orgCode='1' 分公司; oa ="1" （外勤）;oa="0"（内勤）
     * @return
     */

    public DatabaseDao getDataBaseDao(Context context, String name) {
        if (dao == null) {
            synchronized (DBManger.class) {
                if (dao == null) {
                    dbName = name;
                    dao = new DatabaseDao(context, name);
                }
            }
        }
        return dao;
    }

    /**
     *
     * @param staffNumber staffNumber='01042547' 工号
     * @param channel  channel='4' 渠道;
     * @param orgCode orgCode='1' 分公司
     * @param oa  外勤为1  内勤 为0
     * @return
     */
    public String getDBName(String staffNumber,String channel,String orgCode,String oa) {
        return "json_"+staffNumber+"_"+channel+"_"+orgCode+"_"+oa;
    }

}
