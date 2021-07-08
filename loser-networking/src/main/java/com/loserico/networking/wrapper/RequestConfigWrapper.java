package com.loserico.networking.wrapper;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * {@link RequestConfigWrapper}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/7/8
 */
public class RequestConfigWrapper implements Wrapper {

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

	public void setConnectTimeout(Integer connectTimeout) {
		if (connectTimeout == null) {
			this.connectTimeout = 5000;
			return;
		}
		this.connectTimeout = connectTimeout;
	}

	public void setConnectRequestTimeout(Integer connectRequestTimeout) {
		if (connectRequestTimeout == null) {
			this.connectRequestTimeout = 1000;
			return;
		}
		this.connectRequestTimeout = connectRequestTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		if (socketTimeout == null) {
			this.socketTimeout = 2000;
			return;
		}
		this.socketTimeout = socketTimeout;
	}

	private RequestConfigWrapper() {
	}

	public enum RequestTimeoutWrapperEnum {
		INSTANCE;

		private RequestConfigWrapper instance;

		public RequestConfigWrapper getInstance() {
			if (instance == null) {
				instance = new RequestConfigWrapper();
			}
			return instance;
		}
	}

	@Override
	public HttpRequestBase wrap(HttpRequestBase httpRequestBase) {
		httpRequestBase.setConfig(
				RequestConfig.custom()
						.setConnectTimeout(this.connectTimeout)
						.setConnectionRequestTimeout(this.connectRequestTimeout)
						.setSocketTimeout(this.socketTimeout)
						.build());

		return httpRequestBase;
	}
}
