package com.loserico.cache.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-17 13:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CollectionUtils {

	public static <E> HashSet<E> newHashSet(E... elements) {
		Objects.requireNonNull(elements, "elements cannot be null");
		HashSet<E> set = new HashSet<>(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

}
