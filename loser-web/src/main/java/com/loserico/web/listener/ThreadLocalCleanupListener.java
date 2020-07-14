package com.loserico.web.listener;

import com.loserico.common.lang.context.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * 在Request开始和结束的时候清理一遍ThreadLocal
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-08-21 15:34
 * @version 1.0
 *
 */
public class ThreadLocalCleanupListener implements ServletRequestListener {

	private static final Logger logger = LoggerFactory.getLogger(ThreadLocalCleanupListener.class);

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		try {
			ThreadContext.remove();
			logger.debug("Cleanup threadlocals...");
		} catch (Throwable e) {
			logger.error("Threadlocals clean up failed!", e);
		}
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		try {
			ThreadContext.remove();
			logger.debug("Cleanup threadlocals...");
		} catch (Throwable e) {
			logger.error("Threadlocals clean up failed!", e);
		}
		logger.debug("ThreadLocalCleanupListener initialized!");
	}

}
