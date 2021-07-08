package com.loserico.networking.wrapper;

import lombok.Builder;
import lombok.Getter;

/**
 * {@link RequestTimeout}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/7/8
 */
@Getter
@Builder
public class RequestTimeout {

	/**
	 * Connection timeout, unit millisecond.
	 */
	private Integer connectTimeout;

	/**
	 * Timeout for getting connection from connectManager, unit millisecond.
	 */
	private Integer connectRequestTimeout;

	/**
	 * Timeout for request to fetch data, unit millisecond.
	 */
	private Integer socketTimeout;

}
