package cn.ittiger.im.app;

import cn.ittiger.database.SQLiteDBConfig;

/**
 * 本地数据库接口
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public interface IDbApplication {
	/**
	 * 系统全局数据库配置
	 * @return
	 */
	SQLiteDBConfig getGlobalDbConfig();
}
