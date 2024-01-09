package com.loserico.okhttp3;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-12-25 17:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class OKHttpTest {
	
	@Test
	public void testTimeout() {
		Request request = new Request.Builder().url("http://127.0.0.1:9603/depalletizer/handleReachCheckPosition").build();
		OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
		long begin = System.currentTimeMillis();
		/*okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				log.info("请求失败");
				long now = System.currentTimeMillis();
				System.out.println("花费时间: " + (now - begin));
			}
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				log.info("请求成功");
				long now = System.currentTimeMillis();
				System.out.println("花费时间: " + (now - begin));
			}
		});*/
		try {
		okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			log.error("", e);
		}
		long end = System.currentTimeMillis();
		System.out.println("花费时间: " + (end - begin));
		System.out.println("是异步的?");
	}
}
