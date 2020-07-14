package com.loserico.common.lang.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A ThreadContext provides a means of binding and unbinding objects to the
 * current thread based on key/value pairs.
 * <p/>
 * <p>An internal {@link HashMap} is used to maintain the key/value pairs
 * for each thread.</p>
 * <p/>
 * <p>If the desired behavior is to ensure that bound data is not shared across
 * threads in a pooled or reusable threaded environment, the application (or more likely a framework) must
 * bind and remove any necessary values at the beginning and end of stack
 * execution, respectively (i.e. individually explicitly or all via the <tt>clear</tt> method).</p>
 *
 * @on
 * @see #remove()
 * @since 0.1
 */
public final class ThreadContext {

	/**
	 * Private internal log instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ThreadContext.class);

	private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<>();

	/**
	 * Default no-argument constructor.
	 */
	private ThreadContext() {
	}

	/**
	 * Returns the ThreadLocal Map. This Map is used internally to bind objects to the
	 * current thread by storing each object under a unique key.
	 *
	 * @return the map of bound resources
	 */
	public static Map<Object, Object> getResources() {
		if (resources.get() == null) {
			return Collections.emptyMap();
		} else {
			return new HashMap<Object, Object>(resources.get());
		}
	}

	/**
	 * Allows a caller to explicitly set the entire resource map. This operation
	 * overwrites everything that existed previously in the ThreadContext - if you
	 * need to retain what was on the thread prior to calling this method, call the
	 * {@link #getResources()} method, which will give you the existing state.
	 *
	 * @param newResources the resources to replace the existing
	 *            {@link #getResources() resources}.
	 * @since 1.0
	 */
	public static void setResources(Map<Object, Object> newResources) {
		if (newResources == null || newResources.isEmpty()) {
			return;
		}
		ensureResourcesInitialized();
		resources.get().clear();
		resources.get().putAll(newResources);
	}

	/**
	 * Returns the value bound in the {@code ThreadContext} under the specified
	 * {@code key}, or {@code null} if there is no value for that {@code key}.
	 *
	 * @param key the map key to use to lookup the value
	 * @return the value bound in the {@code ThreadContext} under the specified
	 *         {@code key}, or {@code null} if there is no value for that {@code key}.
	 * @since 1.0
	 */
	private static Object getValue(Object key) {
		Map<Object, Object> perThreadResources = resources.get();
		return perThreadResources != null ? perThreadResources.get(key) : null;
	}

	private static void ensureResourcesInitialized() {
		if (resources.get() == null) {
			resources.set(new HashMap<Object, Object>());
		}
	}

	/**
	 * Returns the object for the specified <code>key</code> that is bound to the
	 * current thread.
	 *
	 * @param key the key that identifies the value to return
	 * @return the object keyed by <code>key</code> or <code>null</code> if no value
	 *         exists for the specified <code>key</code>
	 */
	public static <T> T get(Object key) {
		if (logger.isTraceEnabled()) {
			String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
			logger.trace(msg);
		}

		Object value = getValue(key);
		if ((value != null) && logger.isTraceEnabled()) {
			String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" +
					key + "] " + "bound to thread [" + Thread.currentThread().getName() + "]";
			logger.trace(msg);
		}
		return (T)value;
	}

	/**
     * Binds <tt>value</tt> for the given <code>key</code> to the current thread.
     * <p/>
     * <p>A <tt>null</tt> <tt>value</tt> has the same effect as if <tt>remove</tt> was called for the given
     * <tt>key</tt>, i.e.:
     * <p/>
     * <pre>
     * if ( value == null ) {
     *     remove( key );
     * }</pre>
     *
     * @on
     * @param key   The key with which to identify the <code>value</code>.
     * @param value The value to bind to the thread.
     * @throws IllegalArgumentException if the <code>key</code> argument is <tt>null</tt>.
     */
	public static void put(Object key, Object value) {
		if (key == null) {
			throw new IllegalArgumentException("key cannot be null");
		}

		if (value == null) {
			remove(key);
			return;
		}

		ensureResourcesInitialized();
		resources.get().put(key, value);

		if (logger.isTraceEnabled()) {
			String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
					key + "] to thread " + "[" + Thread.currentThread().getName() + "]";
			logger.trace(msg);
		}
	}

	/**
	 * Unbinds the value for the given <code>key</code> from the current thread.
	 *
	 * @param key The key identifying the value bound to the current thread.
	 * @return the object unbound or <tt>null</tt> if there was nothing bound under
	 *         the specified <tt>key</tt> name.
	 */
	public static Object remove(Object key) {
		Map<Object, Object> perThreadResources = resources.get();
		Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

		if ((value != null) && logger.isTraceEnabled()) {
			String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" +
					key + "]" + "from thread [" + Thread.currentThread().getName() + "]";
			logger.trace(msg);
		}

		return value;
	}

	/**
	 * {@link ThreadLocal#remove Remove}s the underlying {@link ThreadLocal
	 * ThreadLocal} from the thread. <p/> This method is meant to be the final 'clean
	 * up' operation that is called at the end of thread execution to prevent thread
	 * corruption in pooled thread environments.
	 *
	 * @since 1.0
	 */
	public static void remove() {
		resources.remove();
	}

	private static final class InheritableThreadLocalMap<T extends Map<Object, Object>>
			extends InheritableThreadLocal<Map<Object, Object>> {

		/**
		 * This implementation was added to address a <a
		 * href="http://jsecurity.markmail.org/search/?q=#query:+page:1+mid:xqi2yxurwmrpqrvj+state:results">
		 * user-reported issue</a>.
		 * 
		 * @param parentValue the parent value, a HashMap as defined in the
		 *            {@link #initialValue()} method.
		 * @return the HashMap to be used by any parent-spawned child threads (a clone
		 *         of the parent HashMap).
		 */
		@SuppressWarnings({ "unchecked" })
		protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
			if (parentValue != null) {
				return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
			} else {
				return null;
			}
		}
	}
}