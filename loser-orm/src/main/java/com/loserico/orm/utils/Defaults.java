package com.loserico.orm.utils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides default values for all Java types, as defined by the JLS.
 *
 * @author Ben Yu
 * @since 1.0
 */
public final class Defaults {
  private Defaults() {}

  private static final Map<Class<?>, Object> DEFAULTS;

  static {
    // Only add to this map via put(Map, Class<T>, T)
    Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    put(map, boolean.class, false);
    put(map, char.class, '\0');
    put(map, byte.class, (byte) 0);
    put(map, short.class, (short) 0);
    put(map, int.class, 0);
    put(map, long.class, 0L);
    put(map, float.class, 0f);
    put(map, double.class, 0d);
    DEFAULTS = Collections.unmodifiableMap(map);
  }

  private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
    map.put(type, value);
  }

  /**
   * Returns the default value of {@code type} as defined by JLS --- {@code 0} for numbers, {@code
   * false} for {@code boolean} and {@code '\0'} for {@code char}. For non-primitive types and
   * {@code void}, {@code null} is returned.
   */
  public static <T> T defaultValue(Class<T> type) {
    // Primitives.wrap(type).cast(...) would avoid the warning, but we can't use that from here
    @SuppressWarnings("unchecked") // the put method enforces this key-value relationship
    T t = (T) DEFAULTS.get(type);
    return t;
  }
}