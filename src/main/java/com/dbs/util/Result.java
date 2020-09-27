package com.dbs.util;

import lombok.Data;

@Data
public class Result {

	private String message;
	private String code;
	private boolean success;

	public static Result badResult(String message) {
		Result result = new Result();
		result.setMessage(message);
		result.setCode("200");
		result.setSuccess(false);
		return result;
	}

	public static Result goodResult(String message) {
		Result result = new Result();
		result.setMessage(message);
		result.setCode("200");
		result.setSuccess(true);
		return result;
	}
}
