package com.dbs.util;


import com.dbs.dao.DbsDao;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by df on 2018/9/20.
 */
public class ThredQuery implements Callable<Integer> {

	private int start;//当前页数

	private int limit;//每页查询多少条

	private String selectSql;//要查询的表名，也可以写死，也可以从前面传
	private String insertSql;//要查询的表名，也可以写死，也可以从前面传
	private List<String> insertCol;//要查询的表名，也可以写死，也可以从前面传
	private List<Map<String, Object>> dbsDaolis ;//要查询的表名，也可以写死，也可以从前面传


	private DbsDao dbsDao = SpringUtil.getBean(DbsDao.class);

	public ThredQuery(int start, int limit, String selectSql, String insertSql,
		List<String> insertCol) {
		this.start = start;
		this.limit = limit;
		this.selectSql = selectSql;
		this.insertSql = insertSql;
		this.insertCol = insertCol;
		//分页查询数据库数据
		 dbsDaolis = dbsDao.selectData(start, limit, selectSql);
		 dbsDao.insertData(dbsDaolis, insertSql,insertCol);
	}

	@Override
	public Integer call() throws Exception {
		//返回数据给Future
		return dbsDaolis.size();
	}
}
