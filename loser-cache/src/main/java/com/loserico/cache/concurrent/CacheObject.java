package com.loserico.cache.concurrent;

public interface CacheObject {
	
	/**
	 * Update the last access time of an object.
	 *
	 * @return <code>true</code> if object was touched else <code>false</code>
	 */
	default boolean touch() {
		return false;
	}
	
	/**
	 * Copy object from source Redis instance to destination Redis instance
	 *
	 * @param host     - destination host
	 * @param port     - destination port
	 * @param database - destination database
	 * @param timeout  - maximum idle time in any moment of the communication with the destination instance in milliseconds
	 */
	default void migrate(String host, int port, int database, long timeout) {
	}
	
	/**
	 * Copy object from source Redis instance to destination Redis instance
	 *
	 * @param host     - destination host
	 * @param port     - destination port
	 * @param database - destination database
	 * @param timeout  - maximum idle time in any moment of the communication with the destination instance in milliseconds
	 */
	default void copy(String host, int port, int database, long timeout) {
	}
	
	/**
	 * Move object to another database
	 *
	 * @param database - Redis database number
	 * @return <code>true</code> if key was moved else <code>false</code>
	 */
	default boolean move(int database) {
		return false;
	}
	
	/**
	 * Returns name of object
	 *
	 * @return name - name of object
	 */
	default String getName() {
		return "无名氏";
	}
	
	/**
	 * Deletes the object
	 *
	 * @return <code>true</code> if it was exist and deleted else <code>false</code>
	 */
	boolean delete();
	
	/**
	 * Delete the objects.
	 * Actual removal will happen later asynchronously.
	 * <p>
	 * Requires Redis 4.0+
	 *
	 * @return <code>true</code> if it was exist and deleted else <code>false</code>
	 */
	default boolean unlink() {
		return false;
	}
	
	/**
	 * Rename current object key to <code>newName</code>
	 *
	 * @param newName - new name of object
	 */
	default void rename(String newName) {
	}
	
	/**
	 * Rename current object key to <code>newName</code>
	 * only if new key is not exists
	 *
	 * @param newName - new name of object
	 * @return <code>true</code> if object has been renamed successfully and <code>false</code> otherwise
	 */
	default boolean renamenx(String newName) {
		return false;
	}
	
	/**
	 * Check object existence
	 *
	 * @return <code>true</code> if object exists and <code>false</code> otherwise
	 */
	boolean isExists();
}