package com.dbs.service.impl;

import com.dbs.util.ParamConfig;
import com.dbs.dao.DbsDao;
import com.dbs.entity.OrderInfo;
import com.dbs.service.DbsService;
import com.dbs.util.Result;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DbsServiceImpl implements DbsService {

	@Autowired
	private DbsDao dbsDao;



	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result start( ParamConfig paramConfig) {
		Result result = new Result();
		Integer startNumber = paramConfig.getStartNumber();
		Integer limit = paramConfig.getLimit();
		String insertSql = paramConfig.getInsertSql();
		String selectSql = paramConfig.getSelectSql();
		long stateTime = System.currentTimeMillis();
		Integer sum=0;
		log.info("数据开始更新——————————————————————————————————————————————————————————————");
		while (true) {
			List<Map<String, Object>> dataSize = dbsDao
				.selectData(startNumber * limit, limit, selectSql);
			dbsDao.insertData(dataSize, insertSql, getFormStr(insertSql));
			startNumber += 1;
			sum+=dataSize.size();
			log.info("更新" + startNumber + "次，更新" + dataSize.size() + "条");
			if (dataSize.size() < limit) {
				break;
			}
		}
		long haoshi=(System.currentTimeMillis() - stateTime);
		log.info("数据更新结束耗时:" +haoshi+ "——————————————————————————————————————————————————————————————");
		result.setCode("200");
		result.setMessage("数据更新结束耗时:" +haoshi+ ",更新了"+sum+"条数据");
		result.setSuccess(true);
		return result;
	}

	private List<String> getFormStr(String insertSql) {
		insertSql = insertSql.trim();
		String[] arr = insertSql.substring(insertSql.indexOf("(") + 1, insertSql.indexOf(")"))
			.split(",");
		return Arrays.asList(arr);
	}

	@Override
	public OrderInfo testFileTwo() {
		return dbsDao.testFileTwo();
	}

	@Override
	public void testFile(FileInputStream fi) {
		dbsDao.testFile(fi);
	}

	@Override
	public Integer testSql(String sql) {
		Map<String, Object> mapCount = dbsDao.getCoiunt(sql);
		Integer sumPage = Integer.parseInt(mapCount.get("sum").toString());
		return sumPage;
	}
}
