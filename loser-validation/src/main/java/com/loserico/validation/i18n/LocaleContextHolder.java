package com.loserico.validation.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 这个类搭配:<p/>
 * <code>com.loserico.web.filter.LocaleConfigurerFilter<code>
 * 可以获得MessageSource实例
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:53
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class LocaleContextHolder {

	private static final ThreadLocal<LocaleContext> localeContextHolder =
			new NamedThreadLocal<LocaleContext>("LocaleContext");

	private static final ThreadLocal<LocaleContext> inheritableLocaleContextHolder =
			new NamedInheritableThreadLocal<LocaleContext>("LocaleContext");

	private static Locale defaultLocale;

	private static TimeZone defaultTimeZone;

	private static MessageSource messageSource;


	/**
	 * Reset the LocaleContext for the current thread.
	 */
	public static void resetLocaleContext() {
		localeContextHolder.remove();
		inheritableLocaleContextHolder.remove();
	}

	/**
	 * Associate the given LocaleContext with the current thread,
	 * <i>not</i> exposing it as inheritable for child threads.
	 * <p>The given LocaleContext may be a {@link TimeZoneAwareLocaleContext},
	 * containing a locale with associated time zone information.
	 *
	 * @param localeContext the current LocaleContext,
	 *                      or {@code null} to reset the thread-bound context
	 * @see SimpleLocaleContext
	 * @see SimpleTimeZoneAwareLocaleContext
	 */
	public static void setLocaleContext(LocaleContext localeContext) {
		setLocaleContext(localeContext, false);
	}

	/**
	 * Associate the given LocaleContext with the current thread.
	 * <p>The given LocaleContext may be a {@link TimeZoneAwareLocaleContext},
	 * containing a locale with associated time zone information.
	 *
	 * @param localeContext the current LocaleContext,
	 *                      or {@code null} to reset the thread-bound context
	 * @param inheritable   whether to expose the LocaleContext as inheritable
	 *                      for child threads (using an {@link InheritableThreadLocal})
	 * @see SimpleLocaleContext
	 * @see SimpleTimeZoneAwareLocaleContext
	 */
	public static void setLocaleContext(LocaleContext localeContext, boolean inheritable) {
		if (localeContext == null) {
			resetLocaleContext();
		} else {
			if (inheritable) {
				inheritableLocaleContextHolder.set(localeContext);
				localeContextHolder.remove();
			} else {
				localeContextHolder.set(localeContext);
				inheritableLocaleContextHolder.remove();
			}
		}
	}

	/**
	 * Return the LocaleContext associated with the current thread, if any.
	 *
	 * @return the current LocaleContext, or {@code null} if none
	 */
	public static LocaleContext getLocaleContext() {
		LocaleContext localeContext = localeContextHolder.get();
		if (localeContext == null) {
			localeContext = inheritableLocaleContextHolder.get();
		}
		return localeContext;
	}

	/**
	 * Associate the given Locale with the current thread,
	 * preserving any TimeZone that may have been set already.
	 * <p>Will implicitly create a LocaleContext for the given Locale,
	 * <i>not</i> exposing it as inheritable for child threads.
	 *
	 * @param locale the current Locale, or {@code null} to reset
	 *               the locale part of thread-bound context
	 * @see #setTimeZone(TimeZone)
	 * @see SimpleLocaleContext#SimpleLocaleContext(Locale)
	 */
	public static void setLocale(Locale locale) {
		setLocale(locale, false);
	}

	/**
	 * Associate the given Locale with the current thread,
	 * preserving any TimeZone that may have been set already.
	 * <p>Will implicitly create a LocaleContext for the given Locale.
	 *
	 * @param locale      the current Locale, or {@code null} to reset
	 *                    the locale part of thread-bound context
	 * @param inheritable whether to expose the LocaleContext as inheritable
	 *                    for child threads (using an {@link InheritableThreadLocal})
	 * @see #setTimeZone(TimeZone, boolean)
	 * @see SimpleLocaleContext#SimpleLocaleContext(Locale)
	 */
	public static void setLocale(Locale locale, boolean inheritable) {
		LocaleContext localeContext = getLocaleContext();
		TimeZone timeZone = (localeContext instanceof TimeZoneAwareLocaleContext ?
				((TimeZoneAwareLocaleContext) localeContext).getTimeZone() : null);
		if (timeZone != null) {
			localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
		} else if (locale != null) {
			localeContext = new SimpleLocaleContext(locale);
		} else {
			localeContext = null;
		}
		setLocaleContext(localeContext, inheritable);
	}

	/**
	 * Set a shared default locale at the framework level,
	 * as an alternative to the JVM-wide default locale.
	 * <p><b>NOTE:</b> This can be useful to set an application-level
	 * default locale which differs from the JVM-wide default locale.
	 * However, this requires each such application to operate against
	 * locally deployed Spring Framework jars. Do not deploy Spring
	 * as a shared library at the server level in such a scenario!
	 *
	 * @param locale the default locale (or {@code null} for none,
	 *               letting lookups fall back to {@link Locale#getDefault()})
	 * @see #getLocale()
	 * @see Locale#getDefault()
	 * @since 4.3.5
	 */
	public static void setDefaultLocale(Locale locale) {
		LocaleContextHolder.defaultLocale = locale;
	}

	/**
	 * Return the Locale associated with the current thread, if any,
	 * or the system default Locale otherwise. This is effectively a
	 * replacement for {@link Locale#getDefault()},
	 * able to optionally respect a user-level Locale setting.
	 * <p>Note: This method has a fallback to the shared default Locale,
	 * either at the framework level or at the JVM-wide system level.
	 * If you'd like to check for the raw LocaleContext content
	 * (which may indicate no specific locale through {@code null}, use
	 * {@link #getLocaleContext()} and call {@link LocaleContext#getLocale()}
	 *
	 * @return the current Locale, or the system default Locale if no
	 * specific Locale has been associated with the current thread
	 * @see LocaleContext#getLocale()
	 * @see #setDefaultLocale(Locale)
	 * @see Locale#getDefault()
	 */
	public static Locale getLocale() {
		LocaleContext localeContext = getLocaleContext();
		if (localeContext != null) {
			Locale locale = localeContext.getLocale();
			if (locale != null) {
				return locale;
			}
		}
		return (defaultLocale != null ? defaultLocale : Locale.getDefault());
	}

	/**
	 * Associate the given TimeZone with the current thread,
	 * preserving any Locale that may have been set already.
	 * <p>Will implicitly create a LocaleContext for the given Locale,
	 * <i>not</i> exposing it as inheritable for child threads.
	 *
	 * @param timeZone the current TimeZone, or {@code null} to reset
	 *                 the time zone part of the thread-bound context
	 * @see #setLocale(Locale)
	 * @see SimpleTimeZoneAwareLocaleContext#SimpleTimeZoneAwareLocaleContext(Locale, TimeZone)
	 */
	public static void setTimeZone(TimeZone timeZone) {
		setTimeZone(timeZone, false);
	}

	/**
	 * Associate the given TimeZone with the current thread,
	 * preserving any Locale that may have been set already.
	 * <p>Will implicitly create a LocaleContext for the given Locale.
	 *
	 * @param timeZone    the current TimeZone, or {@code null} to reset
	 *                    the time zone part of the thread-bound context
	 * @param inheritable whether to expose the LocaleContext as inheritable
	 *                    for child threads (using an {@link InheritableThreadLocal})
	 * @see #setLocale(Locale, boolean)
	 * @see SimpleTimeZoneAwareLocaleContext#SimpleTimeZoneAwareLocaleContext(Locale, TimeZone)
	 */
	public static void setTimeZone(TimeZone timeZone, boolean inheritable) {
		LocaleContext localeContext = getLocaleContext();
		Locale locale = (localeContext != null ? localeContext.getLocale() : null);
		if (timeZone != null) {
			localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
		} else if (locale != null) {
			localeContext = new SimpleLocaleContext(locale);
		} else {
			localeContext = null;
		}
		setLocaleContext(localeContext, inheritable);
	}

	/**
	 * Set a shared default time zone at the framework level,
	 * as an alternative to the JVM-wide default time zone.
	 * <p><b>NOTE:</b> This can be useful to set an application-level
	 * default time zone which differs from the JVM-wide default time zone.
	 * However, this requires each such application to operate against
	 * locally deployed Spring Framework jars. Do not deploy Spring
	 * as a shared library at the server level in such a scenario!
	 *
	 * @param timeZone the default time zone (or {@code null} for none,
	 *                 letting lookups fall back to {@link TimeZone#getDefault()})
	 * @see #getTimeZone()
	 * @see TimeZone#getDefault()
	 * @since 4.3.5
	 */
	public static void setDefaultTimeZone(TimeZone timeZone) {
		defaultTimeZone = timeZone;
	}

	/**
	 * Return the TimeZone associated with the current thread, if any,
	 * or the system default TimeZone otherwise. This is effectively a
	 * replacement for {@link TimeZone#getDefault()},
	 * able to optionally respect a user-level TimeZone setting.
	 * <p>Note: This method has a fallback to the shared default TimeZone,
	 * either at the framework level or at the JVM-wide system level.
	 * If you'd like to check for the raw LocaleContext content
	 * (which may indicate no specific time zone through {@code null}, use
	 * {@link #getLocaleContext()} and call {@link TimeZoneAwareLocaleContext#getTimeZone()}
	 * after downcasting to {@link TimeZoneAwareLocaleContext}.
	 *
	 * @return the current TimeZone, or the system default TimeZone if no
	 * specific TimeZone has been associated with the current thread
	 * @see TimeZoneAwareLocaleContext#getTimeZone()
	 * @see #setDefaultTimeZone(TimeZone)
	 * @see TimeZone#getDefault()
	 */
	public static TimeZone getTimeZone() {
		LocaleContext localeContext = getLocaleContext();
		if (localeContext instanceof TimeZoneAwareLocaleContext) {
			TimeZone timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
			if (timeZone != null) {
				return timeZone;
			}
		}
		return (defaultTimeZone != null ? defaultTimeZone : TimeZone.getDefault());
	}

	public static MessageSource getMessageSource() {
		return messageSource;
	}

	public static void setMessageSource(MessageSource messageSource) {
		LocaleContextHolder.messageSource = messageSource;
	}

}