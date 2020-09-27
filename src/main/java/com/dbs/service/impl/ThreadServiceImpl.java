package com.dbs.service.impl;

import com.dbs.dao.DbsDao;
import com.dbs.service.ThreadService;
import com.dbs.util.ParamConfig;
import com.dbs.util.Result;
import com.dbs.util.ThredQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ThreadServiceImpl implements ThreadService {

	@Autowired
	private DbsDao dbsDao;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result start( ParamConfig paramConfig) throws InterruptedException, ExecutionException {
		Result result = new Result();
		Integer startNumber = paramConfig.getStartNumber();
		Integer limit = paramConfig.getThreadLimit();
		String insertSql = paramConfig.getInsertSql();
		String selectSql = paramConfig.getSelectSql();
		Integer fixThread = paramConfig.getFixThread();
		long stateTime = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(fixThread);
		List<String> insertCol = getFormStr(insertSql);
		Integer sumSizePage = 0;
		log.info("数据开始更新——————————————————————————————————————————————————————————————");
		Map<String, Object> mapCount = dbsDao.getCoiunt(selectSql);
		Integer sumPage = Integer.parseInt(mapCount.get("sum").toString());
		Integer allTimePage = sumPage / limit;
		if (sumPage % limit != 0) {
			allTimePage = allTimePage + 1;
		}
		for (int i = 0; i < allTimePage; i++) {
			int num = paramConfig.getThreadnum();//一次查询多少条
			//需要查询的次数
			int times = limit / num;
			if (limit % num != 0) {
				times = times + 1;
			}
			int bindex = 0;
			int sumSize = 0;
			//Callable用于产生结果
			List<Callable<Integer>> tasks = new ArrayList<>();
			for (int j = 0; j < times; j++) {
				Callable<Integer> qfe = new ThredQuery(startNumber * limit + bindex * num, num,
					selectSql, insertSql, insertCol);
				tasks.add(qfe);
				bindex += 1;
			}
			List<Future<Integer>> futures = executorService.invokeAll(tasks);
			//处理线程返回结果
			if (futures != null && futures.size() > 0) {
				for (Future<Integer> future : futures) {
					sumSize += future.get();
				}
				sumSizePage += sumSize;
			}
			startNumber += 1;
			log.info("更新" + startNumber + "次，更新" + sumSize + "条");
		}
		executorService.shutdown();//关闭线程池
		long haoshi = (System.currentTimeMillis() - stateTime);
		log.info("数据更新结束耗时:" + haoshi
			+ "——————————————————————————————————————————————————————————————");
		result.setCode("200");
		result.setMessage("数据更新结束耗时:" + haoshi + ",更新了" + sumSizePage + "条数据");
		result.setSuccess(true);
		return result;
	}

	private List<String> getFormStr(String insertSql) {
		insertSql = insertSql.trim();
		String[] arr = insertSql.substring(insertSql.indexOf("(") + 1, insertSql.indexOf(")"))
			.split(",");
		return Arrays.asList(arr);
	}
}
