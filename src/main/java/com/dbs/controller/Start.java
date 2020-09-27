package com.dbs.controller;

import com.dbs.entity.OrderInfo;
import com.dbs.service.DbsService;
import com.dbs.service.ThreadService;
import com.dbs.util.ByteToFileUtil;
import com.dbs.util.ParamConfig;
import com.dbs.util.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dbs")
public class Start {

	@Autowired
	private DbsService dbsService;
	@Autowired
	private ThreadService threadService;

	@RequestMapping("/test")
	public String test(){
		return "index";
	}

	@RequestMapping("/start")
	@ResponseBody
	public Result start(@Valid ParamConfig paramConfig) {

		return dbsService.start(paramConfig);
	}
	@RequestMapping("/testform")
	@ResponseBody
	public Result testForm(@Valid ParamConfig paramConfig) {
		paramConfig.getInsertSql();
		return new Result();
	}
	@RequestMapping("/testsql")
	@ResponseBody
	public Result testSql(String selectSql) {
		if(null==selectSql||"".equals(null==selectSql)){
			return Result.badResult("sql不能为空");
		}
		Integer num=dbsService.testSql(selectSql);
		return Result.goodResult("sql查询条数"+num);
	}

	@RequestMapping("/startThead")
	@ResponseBody
	public Result startThead(@Valid ParamConfig paramConfig) throws ExecutionException, InterruptedException {
		return threadService.start(paramConfig);
	}

	/**
	 * 测试
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@RequestMapping(path = "/testFile")
	@ResponseBody
	public void testFile() throws FileNotFoundException, ClassNotFoundException, SQLException {
		File file = new File("D:\\密码.txt");
		FileInputStream fi = null;
		fi = new FileInputStream(file);
		dbsService.testFile(fi);
	}

	@RequestMapping(path = "/testFileTwo")
	@ResponseBody
	public String testFileTwo() throws FileNotFoundException, ClassNotFoundException, SQLException {
		OrderInfo orderInfo = dbsService.testFileTwo();
		byte[] filesname = orderInfo.getByteName();
		ByteToFileUtil.getFile(filesname, "D:\\", "321.txt");
		return "true";
	}

}
