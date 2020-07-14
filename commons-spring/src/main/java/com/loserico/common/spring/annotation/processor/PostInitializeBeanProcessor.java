package com.loserico.common.spring.annotation.processor;

import com.loserico.common.spring.annotation.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostInitializeBeanProcessor implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * Internal logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PostInitializeBeanProcessor.class);

	/**
	 * Indicate how many applicationContext there are, default 1
	 */
	private int contextCount = 1;

	private int currentRunTimes = 1;

	/**
	 * The number of parallels threads
	 */
	private int maxThread = 32;

	/**
	 * Filter used to get only methods annotated with {@link PostInitialize}
	 */
	private ReflectionUtils.MethodFilter methodFilter = new ReflectionUtils.MethodFilter() {
		@Override
		public boolean matches(Method method) {
			return AnnotationUtils.findAnnotation(method, PostInitialize.class) != null;
		}
	};

	/**
	 * A map used to cache all methods before execution
	 */
	private SortedMap<Integer, List<PostInitializingMethod>> postInitializingMethods = new TreeMap<Integer, List<PostInitializingMethod>>();

	/**
	 * Sets the maximum number of threads in parallel
	 *
	 * @param maxThread The number of parallels threads
	 * @since 1.0.3
	 */
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}

	/**
	 * Event processor when context is refreshed
	 * If there are several contexts, only run on the last context refresh
	 *
	 * @param event The context event
	 * @since 1.0.0
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (currentRunTimes < contextCount) {
			currentRunTimes++;
			return;
		}
		this.initializeAnnotations(event.getApplicationContext());
		this.processInitialization();
	}

	/**
	 * Bean processor for methods annotated with {@link PostInitialize}
	 *
	 * @param context The applicationContext
	 * @since 1.0.0
	 */
	private void initializeAnnotations(ApplicationContext context) {
		for (final Map.Entry<String, Object> bean : context.getBeansOfType(null, false, false).entrySet()) {
			ReflectionUtils.doWithMethods(bean.getValue().getClass(), new ReflectionUtils.MethodCallback() {
				@Override
				public void doWith(Method method) throws IllegalAccessException {
					int order = AnnotationUtils.findAnnotation(method, PostInitialize.class).order();
					if (!postInitializingMethods.containsKey(order)) {
						postInitializingMethods.put(order, new ArrayList<PostInitializingMethod>());
					}

					List<PostInitializingMethod> queue = postInitializingMethods.get(order);
					PostInitializingMethod postInitializingMethod = new PostInitializingMethod(method, bean.getValue(),
							bean.getKey());

					if (!queue.contains(postInitializingMethod)) {
						queue.add(postInitializingMethod);
					}
				}
			}, this.methodFilter);
		}

		if (context.getParent() != null) {
			initializeAnnotations(context.getParent());
		}
	}

	/**
	 * Launch the initialization of eligible beans
	 *
	 * @since 1.0.2
	 */
	private void processInitialization() {
		ExecutorService threadPool = Executors.newFixedThreadPool(maxThread);
		for (Map.Entry<Integer, List<PostInitializingMethod>> initializingMethods : this.postInitializingMethods.entrySet()) {
			List<PostInitializingMethod> postInitializingMethods = initializingMethods.getValue();
			for (PostInitializingMethod postInitializingMethod : postInitializingMethods) {
				threadPool.submit(postInitializingMethod);
			}
		}
	}

	public int getContextCount() {
		return contextCount;
	}

	public void setContextCount(int contextCount) {
		this.contextCount = contextCount;
	}

	/**
	 * Inner class used for method arrangement
	 */
	private static class PostInitializingMethod implements Callable<Void> {
		/**
		 * The method to execute
		 */
		private Method method;

		/**
		 * The instance of the bean
		 */
		private Object beanInstance;

		/**
		 * The name of the bean
		 */
		private String beanName;

		/**
		 * Complete constructor of the PostInitializingMethod
		 *
		 * @param method       The method to invoke
		 * @param beanInstance The beanInstance on which to execute the method
		 * @param beanName     The name of the bean
		 * @since 1.0.0
		 */
		private PostInitializingMethod(Method method, Object beanInstance, String beanName) {
			this.method = method;
			this.beanInstance = beanInstance;
			this.beanName = beanName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			PostInitializingMethod that = (PostInitializingMethod) o;
			if (!beanInstance.equals(that.beanInstance)) {
				return false;
			}

			if (!method.getName().equals(that.method.getName())) {
				return false;
			}

			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			int result = method.getName().hashCode();
			result = 31 * result + beanInstance.hashCode();
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Void call() throws Exception {
			log.debug(MessageFormat.format("Initializing bean named {0} with method {1}", this.beanName, this.method.getName()));

			try {
				ReflectionUtils.invokeMethod(this.method, this.beanInstance);
			} catch (IllegalArgumentException e) {
				throw new BeanInitializationException(
						MessageFormat.format("Post Initialization of bean {0} failed.", this.beanName), e);
			}

			return null;
		}
	}
}
