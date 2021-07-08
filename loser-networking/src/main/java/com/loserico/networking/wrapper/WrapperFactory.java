package com.loserico.networking.wrapper;

import com.loserico.common.lang.utils.CollectionUtils;
import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.wrapper.RequestConfigWrapper.RequestTimeoutWrapperEnum;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * {@link WrapperFactory}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/7/8
 */
public class WrapperFactory {

	private List<Wrapper> wrappers;

	private WrapperFactory() {
		wrappers = new ArrayList<>();
	}

	public enum WrapperFactoryEnum {
		INSTANCE;

		WrapperFactory instance;

		public WrapperFactory getInstance() {
			if (instance == null) {
				instance = new WrapperFactory();
			}
			return instance;
		}
	}

	public void initTimeout(RequestTimeout timeout) {
		RequestConfigWrapper requestTimeoutWrapper = RequestTimeoutWrapperEnum.INSTANCE.getInstance();
		requestTimeoutWrapper.setConnectRequestTimeout(timeout.getConnectRequestTimeout());
		requestTimeoutWrapper.setConnectTimeout(timeout.getConnectTimeout());
		requestTimeoutWrapper.setSocketTimeout(timeout.getSocketTimeout());
		wrappers.add(requestTimeoutWrapper);
	}

	public HttpRequestBase startWrap(HttpMethod method) {
		HttpRequestBase httpRequestBase = HttpMethod.getMethod(method);

		if (CollectionUtils.isEmpty(wrappers)) {
			return httpRequestBase;
		}
		wrappers.stream().forEach(wrapper -> wrapper.wrap(httpRequestBase));

		return httpRequestBase;
	}
}
