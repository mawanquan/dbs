package com.dbs.config;

import java.io.IOException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class IndexConfig {

	@EventListener({ApplicationReadyEvent.class})
	void applicationReadyEvent() {
		System.out.println("应用已经准备就绪 ... 启动浏览器");
		String url = "http://127.0.0.1:8080/dbs/test";
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}