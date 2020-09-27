package com.dbs.service;

import com.dbs.entity.OrderInfo;
import com.dbs.util.ParamConfig;
import com.dbs.util.Result;
import java.io.FileInputStream;

public interface DbsService {

	/**
	 * 单线程更新
	 * @return
	 */
	Result start( ParamConfig paramConfig);

	/**
	 * 验证文件是否传输成功
	 * @return
	 */
	OrderInfo testFileTwo();

	/**
	 * 插入测试数据
	 * @param fi
	 */
	void testFile(FileInputStream fi);

	Integer testSql(String sql);
}
