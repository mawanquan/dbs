package com.dbs.util;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
public class ParamConfig {

	@NotBlank(message = "查询语句不能为空")
	private String selectSql;

	@NotBlank(message = "插入语句不能为空")
	private String insertSql;

	@Value("${dbsConfig.limit}")
	private Integer limit;
	@Value("${dbsConfig.startNumber}")
	private Integer startNumber;
	@Value("${dbsConfig.threadnum}")
	private Integer threadnum;
	@Value("${dbsConfig.threadLimit}")
	private Integer threadLimit;
	@Value("${dbsConfig.fixThread}")
	private Integer fixThread;
}
