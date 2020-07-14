package com.loserico.functional;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.time.format.DateTimeFormatter.ofPattern;

public class FunctionalTest {
/*
	public static void main(String[] args) {
		FunctionalInterfaceExample a = () -> System.out.println("a");
		a.apply();

		B b = () -> System.out.println("B");
		b.apply();
	}*/

	@Test
	public void testFunc() {
		Stream.of("hello", "world")
				.filter(e -> e.startsWith("h"))
				.forEach(System.out::println);
	}

	@Test
	public void testNullAndEmpty() {
		Predicate<String> nullCheck = e -> e != null;
		Predicate<String> emptyCheck = e -> e.trim().length() > 0;
		Predicate<String> nullAndEmtpyCheck = nullCheck.and(emptyCheck);
		System.out.println(nullAndEmtpyCheck.test("hello"));
		System.out.println(nullAndEmtpyCheck.test(null));
		System.out.println(nullAndEmtpyCheck.test(" "));
	}

	@Test
	public void testRemoveIf() {
		List<String> greeting = new ArrayList<String>();
		greeting.add("hello");
		greeting.add("world");
		greeting.removeIf(((Predicate<String>) e -> e.startsWith("h")).negate());
		greeting.forEach(System.out::println);
	}

	@Test
	public void testConsumer() {
		Stream<String> strings = Stream.of("hello", "world");
		Consumer<String> printStr = System.out::println;
		strings.forEach(printStr);
	}

	@Test
	public void testFunction() {
		Arrays.stream("4, -9, 16".split(", "))
				.map(Integer::parseInt)
				.map(i -> (i < 0) ? -i : i)
				.forEach(System.out::println);
	}

	@Test
	public void testFunctionCombine() {
		Function<String, Integer> parseInt = Integer::parseInt;
		Function<Integer, Integer> absInt = Math::abs;
		Function<String, Integer> parseAndAbs = parseInt.andThen(absInt);
		Arrays.stream("4, -9, 16".split(", "))
				.map(parseAndAbs)
				.forEach(System.out::println);
	}

	@Test
	public void testSupplier() {
		Random random = new Random();
		Stream.generate(random::nextBoolean)
				.limit(2)
				.forEach(System.out::println);
	}

	@Test
	public void testSupplierWithLocalDateTime() {
		Supplier<String> dateTimeStr = () -> LocalDateTime.now().format(ofPattern("yyyy-MM-dd HH:mm:ss"));
		System.out.println(dateTimeStr.get());
	}

	/*
	 * Integer(String)
	 */
	@Test
	public void testIntegerNew() {
		Function<String, Integer> anotherInt = Integer::new;
		System.out.println(anotherInt.apply("100"));
	}

	@Test
	public void testPrimativeFunctions() {
		IntPredicate intPredicate = i -> i % 2 == 0;
		IntStream.range(1, 10)
				// .filter(i -> i % 2 == 0)
				.filter(intPredicate)
				.forEach(System.out::println);
	}

	/*
	 * This code calls the int incrementAndGet() method defined in the class
	 * java.util.concurrent. atomic.AtomicInteger. Note that this method returns an
	 * int and not an Integer. Still, we can use it with Stream because of implicit
	 * autoboxing and unboxing to and from int’s wrapper type Integer. This boxing
	 * and unboxing is simply unnecessary. Instead you can use the IntStream
	 * interface; its generator() method takes an IntSupplier as an argument.
	 */
	@Test
	public void testPrimativeFunction() {
		AtomicInteger ints = new AtomicInteger(0);
		// Stream.generate(ints::incrementAndGet)
		// .limit(10)
		// .forEach(System.out::println);
		IntStream.generate(ints::incrementAndGet).limit(10).forEach(System.out::println);
	}

	@Test
	public void testBiFunction() {
		BiFunction<String, String, String> concat = (x, y) -> x.concat(y);
		System.out.println(concat.apply("rico ", "yu"));
	}

	/*
	 * This code shows how to use BiPredicate. The contains() method in List takes
	 * an element as an argument and checks if the underlying list contains the
	 * element. Because it takes an argument and returns an Integer, we can use a
	 * BiPredicate. Why not use BiFunction<T, U, Boolean>? Yes, the code will work,
	 * but a better choice is the equivalent BiPredicate<T, U> because the
	 * BiPredicate returns a boolean value.
	 */
	@Test
	public void testBiPredicate() {
		BiPredicate<List<Integer>, Integer> listContains = List::contains;
		List<Integer> list = Arrays.asList(10, 20, 30);
		System.out.println(listContains.test(list, 20));
	}

	/*
	 * This code segment shows how to use BiConsumer. Similar to using
	 * List::contains method reference in the previous example for BiPredicate, this
	 * example shows how to use BiConsumer to call add() method in List using this
	 * interface.
	 */
	@Test
	public void testBiConsumer() {
		BiConsumer<List<Integer>, Integer> listAddElement = List::add;
		List<Integer> list = new ArrayList<Integer>();
		listAddElement.accept(list, 10);
		System.out.println(join(", ", list.stream()
				.map(e -> e.toString())
				.collect(Collectors.toList())));
	}

	/*
	 * This code uses replaceAll() method introduced in Java 8 that replaces the
	 * elements in the given List. The replaceAll() method takes a UnaryOperator as
	 * the sole argument:
	 * 
	 * The replaceAll() method is passed with Math::abs method to it. Math has four
	 * overloaded methods for abs() method: 
	 * 	abs(int) 
	 * 	abs(long) 
	 * 	abs(double)
	 * 	abs(float)
	 * 
	 * Because the type is Integer, the overloaded method abs(int) is selected
	 * through type inference. UnaryOperator is a functional interface and it
	 * extends Function interface, and you can use the apply() method declared in
	 * the Function interface; further, it inherits the default functions compose()
	 * and andThen() from the Function interface. Similar to UnaryOperator that
	 * extends Function interface, there is a BinaryOperator that extends BiFunction
	 * interface.
	 */
	@Test
	public void testUnaryOperator() {
		List<Integer> ell = Arrays.asList(-11, 22, 33, -44, 55);
		System.out.println("Before: " + ell);
		ell.replaceAll(Math::abs);
		System.out.println("After: " + ell);
	}
	
}
