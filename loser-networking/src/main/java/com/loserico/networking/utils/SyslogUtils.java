package com.loserico.networking.utils;

import com.loserico.networking.builder.SyslogBuilder;

import java.util.Objects;

/**
 * <p>
 * Copyright: (C), 2021-09-30 15:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SyslogUtils {
	
	public static SyslogBuilder of(String host, Integer port) {
		Objects.requireNonNull(host, "host cannot be null!");
		Objects.requireNonNull(port, "port cannbot be null!");
		return new SyslogBuilder(host, port);
	}
}
