package com.loserico.networking.wrapper;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * {@link Wrapper}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/7/8
 */
public interface Wrapper {

	HttpRequestBase wrap(HttpRequestBase base);
}
