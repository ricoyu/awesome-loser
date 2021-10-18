package com.loserico.networking;

import com.loserico.networking.constants.Proto;
import com.loserico.networking.utils.SyslogUtils;
import lombok.SneakyThrows;
import org.graylog2.syslog4j.SyslogIF;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2021-09-30 13:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SyslogClientTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		syslogUtilsApi();
		return;
	}
	
	private static void syslogUtilsApi() {
		SyslogIF syslog = SyslogUtils.of("10.10.26.14", 31514)
				.charset(UTF_8)
				.proto(Proto.TCP)
				.maxMessageLength(500000)
				.build();
		while (true) {
			try {
				syslog.info("hello guys");
				//syslog.flush();
				System.out.println("发送成功");
				SECONDS.sleep(1L);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void rawApi() {
		SyslogIF syslog = org.graylog2.syslog4j.Syslog.getInstance("tcp");
		syslog.getConfig().setHost("10.10.26.14");
		syslog.getConfig().setPort(31514);
		syslog.getConfig().setMaxMessageLength(50000);
		while (true) {
			try {
				syslog.info("hello guys");
				//syslog.flush();
				System.out.println("发送成功");
				SECONDS.sleep(1L);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
