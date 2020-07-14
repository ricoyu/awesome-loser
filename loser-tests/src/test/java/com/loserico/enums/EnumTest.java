package com.loserico.enums;

import com.loserico.common.lang.utils.EnumUtils;
import org.junit.Test;

import java.util.EnumSet;
import java.util.Iterator;

import static com.loserico.common.lang.utils.StringUtils.equalsIgCase;

public class EnumTest {

	private enum Status {
		HAPPY,
		SAD,
		CARM;
	}

	@Test
	public void testEnumCreate() {
		Status status = Enum.valueOf(Status.class, "HAPPY");
		System.out.println(status);
		Status sad = lookupEnum(Status.class, 1);
		System.out.println(sad);
		System.out.println(sad.name());
		System.out.println(Status.HAPPY.ordinal());
	}
	
	@Test
	public void testEnumTest() {
		System.out.println(EnumSet.allOf(Status.class));;
		EnumSet.allOf(Status.class).forEach((e) -> System.out.println(equalsIgCase(e.name(), "happy")));
	}
	
	@Test
	public void testIsEnum() {
		System.out.println(Status.CARM instanceof Enum);
		System.out.println(Status.class.isEnum());
	}
	
	@Test
	public void testEnumClass() {
		Class<?> clazz = getClassObject();
		Object transformed = EnumUtils.lookupEnum((Class<Enum>)clazz, getValue());
		setEnum(transformed);
	}

	public static <E extends Enum<E>> E lookupEnum(Class<E> clazz, int ordinal) {
		Class<? extends Enum> clazz2 = Status.class;
		EnumSet<E> enumSet = EnumSet.allOf(clazz);
		if (ordinal < enumSet.size()) {
			Iterator<E> iterator = enumSet.iterator();
			for (int i = 0; i < ordinal; i++) {
				iterator.next();
			}
			E rval = iterator.next();
			assert (rval.ordinal() == ordinal);
			return rval;
		}
		throw new IllegalArgumentException(
				"Invalid value " + ordinal + " for " + clazz.getName() + ", must be < " + enumSet.size());
	}
	
	@Test
	public void testOrdinal() {
		System.out.println(Status.HAPPY.ordinal());
	}
	
	public static Object getValue() {
		return Status.HAPPY.ordinal();
	}
	
	public static Class<?> getClassObject() {
		return Status.class;
	}
	
	public static void setEnum(Object status) {
		System.out.println((Status)status);
	}
}
