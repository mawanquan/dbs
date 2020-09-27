package com.dbs.service;

import com.dbs.util.ParamConfig;
import com.dbs.util.Result;
import java.util.concurrent.ExecutionException;

public interface ThreadService {

	/**
	 * 多线程更新
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	Result start( ParamConfig paramConfig)throws InterruptedException, ExecutionException;

}
