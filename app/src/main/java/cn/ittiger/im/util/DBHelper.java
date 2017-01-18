package cn.ittiger.im.util;

import cn.ittiger.app.AppContext;
import cn.ittiger.database.SQLiteDB;
import cn.ittiger.database.SQLiteDBConfig;
import cn.ittiger.database.SQLiteDBFactory;
import cn.ittiger.im.app.App;
import cn.ittiger.im.app.IDbApplication;

/**
 * 本地数据库管理类
 * @author laohu
 */
public class DBHelper {
    /**
     * 管理器单例
     */
    private static DBHelper sDBInstance;
    /**
     * 数据库配置上下文
     */
    private IDbApplication mDbApplication;
    /**
     * 全局数据库
     */
    private SQLiteDB mDB;


    private DBHelper() {

        mDbApplication = ((App) AppContext.getInstance().getApplicationContext());
    }

    public static DBHelper getInstance() {

        if(sDBInstance == null) {
            synchronized (DBHelper.class) {
                if(sDBInstance == null) {
                    sDBInstance = new DBHelper();
                }
            }
        }
        return  sDBInstance;
    }

    /**
     * 获取全局数据库操作对象
     * @return
     */
    public SQLiteDB getSQLiteDB() {

        if(mDB == null) {
            synchronized (this) {
                if(mDB == null) {
                    SQLiteDBConfig config = mDbApplication.getGlobalDbConfig();
                    mDB = SQLiteDBFactory.createSQLiteDB(config);
                }
            }
        }
        return mDB;
    }

    /**
     * 关闭数据库
     */
    public void closeSQLiteDB() {
        if(this.mDB != null) {
            this.mDB.close();
        }
        this.mDB = null;
    }
}
