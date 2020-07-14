package com.loserico.common.spring.annotation.processor;

import com.loserico.common.spring.annotation.PostInitialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ReflectionUtils.doWithMethods;
import static org.springframework.util.ReflectionUtils.invokeMethod;

/**
 * Processor that handles {@link PostInitialize} annotation
 *
 * @author Bastien Cecchinato
 * @since 1.0.0
 */
@Slf4j
public class PostInitializeProcessor implements SmartInitializingSingleton {
	
	/**
	 * The maximum number of threads in parallel
	 */
	private int maxThread = 32;
	
	/**
	 * The application context that contains all beans
	 */
	private ApplicationContext applicationContext;
	
	/**
	 * Filter used to get only methods annotated with {@link PostInitialize}
	 */
	private ReflectionUtils.MethodFilter methodFilter = method -> findAnnotation(method, PostInitialize.class) != null;
	
	/**
	 * A map used to cache all methods before execution
	 */
	private MultiValueMap<Integer, PostInitializingMethod> postInitializingMethods = new LinkedMultiValueMap<>();
	
	/**
	 * Sets the maximum number of threads in parallel
	 *
	 * @param maxThread The maximum number of threads in parallel
	 * @since 1.1.0
	 */
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	
	/**
	 * Sets the application context that contains all beans
	 *
	 * @param applicationContext The application context to set
	 * @since 1.1.0
	 */
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Bean processor for methods annotated with {@link PostInitialize}
	 *
	 * @since 1.0.0
	 */
	private void initializeAnnotations() {
		this.applicationContext.getBeansOfType(null, false, false).values()
				.stream()
				.filter(Objects::nonNull)
				.forEach(bean -> doWithMethods(bean.getClass(), method -> {
					int order = findAnnotation(method, PostInitialize.class).order();
					postInitializingMethods.add(order, new PostInitializingMethod(method, bean));
				}, this.methodFilter));
	}
	
	/**
	 * Launch the initialization of eligible beans
	 *
	 * @since 1.0.2
	 */
	private void processInitialization() {
		ExecutorService executorService = Executors.newFixedThreadPool(this.maxThread);
		this.postInitializingMethods.keySet().stream().sorted().forEach(key -> {
			List<PostInitializingMethod> value = this.postInitializingMethods.get(key);
			log.debug(
					format("Found {0} beans with max threads {1} and order {2}", value.size(), this.maxThread, key));
			try {
				executorService.invokeAll(value);
			} catch (InterruptedException e) {
				log.warn(
						format("Failed to invoke {0} method on executorService {1}", value.size(), executorService));
			}
		});
		executorService.shutdown();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterSingletonsInstantiated() {
		final long startTimestamp = System.currentTimeMillis();
		
		this.initializeAnnotations();
		this.processInitialization();
		
		int methodCount = this.postInitializingMethods.values().stream().collect(Collectors.summingInt(List::size));
		log.info(format("Launched {0} method(s) in {1,number,#}ms", methodCount,
				System.currentTimeMillis() - startTimestamp));
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
		 * Complete constructor of the PostInitializingMethod
		 *
		 * @param method       The method to invoke
		 * @param beanInstance The beanInstance on which to execute the method
		 * @since 1.0.0
		 */
		private PostInitializingMethod(Method method, Object beanInstance) {
			this.method = method;
			this.beanInstance = beanInstance;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Void call() throws Exception {
			log.debug(format("Initializing method {0} in class {1}", this.method.getName(),
					this.beanInstance.getClass()));
			
			try {
				invokeMethod(this.method, this.beanInstance);
			} catch (IllegalArgumentException e) {
				throw new BeanInitializationException(
						format("Post Initialization of method {0} in class {1} failed.", method.getName(),
								beanInstance.getClass()),
						e);
			}
			
			return null;
		}
	}
	
	/**
	 * Method called after bean initialization
	 *
	 * @since 1.1.0
	 */
	@PostConstruct
	public void postConstruct() {
		log.info(format("Registered {0} with configuration : maxThread = [{1}]",
				PostInitializeProcessor.class.getName(), this.maxThread));
	}
}
