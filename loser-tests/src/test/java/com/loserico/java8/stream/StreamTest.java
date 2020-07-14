package com.loserico.java8.stream;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertSame;

/**
 * http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html
 * https://www.javacodegeeks.com/2015/11/java-8-streams-api-grouping-partitioning-stream.html
 * http://www.mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/
 * <p>
 * Among many other things, filtering, mapping, and finding elements in
 * collections has become so much easier and more concise with lambdas and
 * the streams API.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @since 2017-03-18 22:07
 */
public class StreamTest {

    @Test
    public void testFileStream() throws IOException {
        final Path path = new File("data.txt").toPath();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.onClose(() -> System.out.println("Done!")) //注册stream关闭时的监听器
                    .forEach(System.out::println);
        }
    }

    @Test
    public void testPeek() {
        /*
         * Filtered value: three Mapped value: THREE Filtered value: four
         * Mapped value: FOUR
         */
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e)) //中间状态，对stream的每个元素执行一定操作然后返回stream的所有元素
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }

    @Test
    public void testList2Array() {
        List<String> peoples = Arrays.asList("rico", "vivi", "zaizai");
        String[] peopleArray = peoples.stream().toArray(String[]::new); //List转Array
        System.out.println(peopleArray.length);
        for (int i = 0; i < peopleArray.length; i++) {
            String people = peopleArray[i];
            System.out.println(people);
        }
    }

    @Test
    public void testStreamCreate() {
        Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        });
        //无限循环下去
        //		Stream<Double> resultStream = Stream.generate(() -> Math.random());
        Stream<Double> resultStream = Stream.generate(() -> Math.random()).limit(10);
        resultStream.forEach(System.out::println);
        Stream.generate(Math::random);

    }

    @Test
    public void testStreamCreateByIterate() {
        Stream.iterate(1, t -> 2 + t).limit(9).forEach(System.out::println);
    }

    /**
     * The below example will print an empty result, because filter() has no
     * idea how to filter a stream of String[]
     */
    @Test
    public void testFlatMap1() {
        String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
        // Stream<String[]>
        Stream<String[]> temp = Arrays.stream(data);
        // filter a stream of string[], and return a string[]?
        Stream<String[]> stream = temp.filter(x -> "a".equals(x.toString()));
        // 不打印
        stream.forEach(System.out::println);
    }

    @Test
    public void testFlatMap2() {
        String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
        // Stream<String[]>
        Stream<String[]> temp = Arrays.stream(data);
        // Stream<String>, GOOD!
        Stream<String> stringStream = temp.flatMap(x -> Arrays.stream(x));
        Stream<String> stream = stringStream.filter(x -> "a".equals(x));
        // a
        stream.forEach(System.out::println);
    }

    @Test
    public void testFlatMap() {
        Map<String, List<LocalDateTime>> dates = new HashMap<String, List<LocalDateTime>>();
        dates.put("today", asList(LocalDateTime.now(), LocalDateTime.of(2017, 03, 29, 11, 9)));
        dates.put("yestoday", asList(LocalDateTime.of(2017, 03, 28, 11, 9), LocalDateTime.of(2017, 03, 28, 11, 9)));
        dates.values().stream()
                .flatMap(dateList -> dateList.stream())
                .forEach(obj -> System.out.println(obj.getClass()));
    }

    @Test
    public void testListToMap() {
        List<Hosting> list = new ArrayList<>();
        list.add(new Hosting(1, "liquidweb.com", 80000));
        list.add(new Hosting(2, "linode.com", 90000));
        list.add(new Hosting(3, "digitalocean.com", 120000));
        list.add(new Hosting(4, "aws.amazon.com", 200000));
        list.add(new Hosting(5, "mkyong.com", 1));

        // key = id, value - websites
        Map<Integer, String> result1 = list.stream().collect(
                toMap(Hosting::getId, Hosting::getName));
        System.out.println("Result 1 : " + result1);

        // key = name, value - websites
        Map<String, Long> result2 = list.stream().collect(toMap(Hosting::getName, Hosting::getWebsites));
        System.out.println("Result 2 : " + result2);

        // Same with result1, just different syntax
        // key = id, value = name
        Map<Integer, String> result3 = list.stream().collect(toMap(x -> x.getId(), x -> x.getName()));
        System.out.println("Result 3 : " + result3);

        Map<String, Hosting> result4 = list.stream().collect(toMap(Hosting::getName, identity()));
        System.out.println("Result 4: " + toJson(result4));
    }

    @Test
    public void testList2MapWithDuplicateKey() {
        List<Hosting> list = new ArrayList<>();
        list.add(new Hosting(1, "liquidweb.com", 80000));
        list.add(new Hosting(2, "linode.com", 90000));
        list.add(new Hosting(3, "digitalocean.com", 120000));
        list.add(new Hosting(4, "aws.amazon.com", 200000));
        list.add(new Hosting(5, "mkyong.com", 1));

        list.add(new Hosting(6, "linode.com", 100000)); // new line

        // key = name, value - websites , but the key 'linode' is duplicated!?
        //Map<String, Long> result1 = list.stream().collect(toMap(Hosting::getName, Hosting::getWebsites));//java.lang.IllegalStateException: Duplicate key 90000
        //System.out.println("Result 1 : " + result1);

        //To solve the duplicated key issue above, pass in the third mergeFunction argument like this
        Map<String, Long> result1 = list.stream()
                //(oldValue, newValue) -> oldValue ==> If the key is duplicated, do you prefer oldKey or newKey?
                //.collect(toMap(Hosting::getName, Hosting::getWebsites, (oldValue, newValue) -> oldValue));
                .collect(toMap(Hosting::getName, Hosting::getWebsites, (oldValue, newValue) -> newValue));
        System.out.println("Result 1 : " + result1);
    }

    @Test
    public void testList2MapWithSort() {
        List<Hosting> list = new ArrayList<>();
        list.add(new Hosting(1, "liquidweb.com", 80000));
        list.add(new Hosting(2, "linode.com", 90000));
        list.add(new Hosting(3, "digitalocean.com", 120000));
        list.add(new Hosting(4, "aws.amazon.com", 200000));
        list.add(new Hosting(5, "mkyong.com", 1));
        list.add(new Hosting(6, "linode.com", 100000));

        Map<String, Long> result1 = list.stream()
                .sorted(Comparator.comparingLong(Hosting::getWebsites).reversed())
                .collect(toMap(
                        Hosting::getName,
                        Hosting::getWebsites, // key = name, value = websites
                        (oldValue, newValue) -> oldValue, // if same key, take the old key
                        LinkedHashMap::new)); //returns a LinkedHashMap, keep order
        System.out.println("Result 1 : " + result1);
    }

    /*
     * Group by the name + Count or Sum the Qty.
     */
    @Test
    public void testList2MapCountSum() {
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99")));

        Map<String, Long> counting = items.stream().collect(groupingBy(Item::getName, counting()));
        System.out.println(counting);

        Map<String, Integer> sum = items.stream().collect(groupingBy(Item::getName, summingInt(Item::getQty)));
        System.out.println(sum);
    }

    @Test
    public void testList2MapGroupingByMapping() {
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99")));

        //group by price
        Map<BigDecimal, List<Item>> groupByPriceMap = items.stream().collect(groupingBy(Item::getPrice));
        System.out.println(groupByPriceMap);

        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> result = items.stream()
                .collect(groupingBy(Item::getPrice, mapping(Item::getName, toSet())));
        System.out.println(result);
    }

    @Test
    public void testList2MapLocale() {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = locales.collect(toMap(
                l -> l.getDisplayLanguage(),
                l -> l.getDisplayLanguage(l),
                (existingValue, newValue) -> existingValue));
        System.out.println(toJson(languageNames));
    }

    /**
     * However, suppose we want to know all languages in a given country.
     * Then we need a Map<String, Set<String>>. For example, the value for
     * "Switzerland" is the set [French, German, Italian]. At first, we
     * store a singleton set for each language. Whenever a new language is
     * found for a given country, we form the union of the existing and the
     * new set.
     */
    @Test
    public void testList2MapSet() {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSets = locales.collect(toMap(
                l -> l.getDisplayCountry(),
                l -> Collections.singleton(l.getDisplayLanguage()),
                (a, b) -> {
                    // Union of a and b
                    Set<String> r = new HashSet<>(a);
                    r.addAll(b);
                    return r;
                }));
        System.out.println(toJson(countryLanguageSets));
    }

    @Test
    public void testGroupingByCount() {
        //3 apple, 2 banana, others 1
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
        Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));
        System.out.println(result);
    }

    @Test
    public void testGroupingByCountWithSorting() {
        //3 apple, 2 banana, others 1
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
        Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));
        System.out.println(result);

        Map<String, Long> finalMap = new LinkedHashMap<>();
        //Sort a map and add to finalMap
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        System.out.println(finalMap);
    }

    @Test
    public void testAll() {
        List<Integer> nums = Arrays.asList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);
        System.out.println("sum is:" + nums
                .stream()
                .filter(num -> num != null)
                .distinct()
                .mapToInt(num -> num * 2)
                .peek(System.out::println)
                .skip(2)
                .limit(4)
                .sum());
    }

    @Test
    public void testReduce() {
        int value = Stream.of(1, 2, 3, 4).reduce(100, (sum, item) -> sum + item);
        assertSame(value, 110);
        /* 或者使用方法引用 */
        value = Stream.of(1, 2, 3, 4).reduce(100, Integer::sum);
    }

    @Test
    public void testReduce2() {
        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println(concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println(minValue);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println(sumValue);
        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println(sumValue);
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0)
                .reduce("", String::concat);
        System.out.println(concat);
    }

    public void testLimitAndSkip() {
        List<Person> persons = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            persons.add(new Person(i, "name" + i));
        }
        List<String> personList2 = persons
                .stream()
                .map(Person::getName)
                .limit(10)
                .skip(3)
                .collect(Collectors.toList());
        System.out.println(personList2);
    }

    @Test
    public void testLimitAndSkip2() {
        List<Person> persons = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<Person> personList2 = persons
                .stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .limit(2)
                .collect(Collectors.toList());
        System.out.println(personList2);
    }

    @Test
    public void testMax() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("data.txt"));
        int longest = bufferedReader
                .lines()
                .mapToInt(String::length)
                .max()
                .getAsInt();
        bufferedReader.close();
        System.out.println(longest);
    }

    /*
     * @Test public void testDistinct() throws IOException { BufferedReader
     * bufferedReader = new BufferedReader(new FileReader("data.txt"));
     * List<String> words = bufferedReader .lines() .flatMap(line ->
     * Stream.of(line.split(" "))) .filter(word -> word.length() > 0)
     * .map(String::toLowerCase) .distinct() .sorted()
     * .collect(Collectors.toList()); bufferedReader.close();
     * System.out.println(words); }
     */

    @Test
    public void testGenerate() {
        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random)
                .limit(10)
                .forEach(System.out::println);
        // Another way
        IntStream.generate(() -> (int) (System.nanoTime() % 100))
                .limit(10)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        Stream.generate(new PersonSupplier())
                .limit(10)
                .forEach(p -> System.out.println(p.getName() + ", " + p.getAge()));
    }

    static class PersonSupplier implements Supplier<Person> {
        private int index = 0;
        private Random random = new Random();

        @Override
        public Person get() {
            return new Person(index++, "StormTestUser" + index, random.nextInt(100));
        }
    }

    @Test
    public void testCollect() {
        Map<Integer, List<Person>> personGroups = Stream.generate(new PersonSupplier())
                .limit(100)
                .collect(Collectors.groupingBy(Person::getAge));
        for (Integer key : personGroups.keySet()) {
            System.out.println("Age " + key + " = " + personGroups.get(key).size());
        }
    }

    @Test
    public void testCollect2() {
        Map<Boolean, List<Person>> children = Stream.generate(new PersonSupplier())
                .limit(100)
                .collect(Collectors.partitioningBy(p -> p.getAge() < 18));
        System.out.println("Children number: " + children.get(true).size());
        System.out.println("Adult number: " + children.get(false).size());
    }

    @Test
    public void test2Array() {
        String[] array = Stream.of("a", "b", "", "c")
                .filter(s -> !s.isEmpty())
                .toArray(size -> new String[size]);
        System.out.println(String.join(", ", array));
    }

    @Test
    public void test2List() {
        List<String> list = Stream.of("a", "b", "", "c")
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @Test
    public void testCollectorJoin() {
        List<Person> list = Arrays.asList(
                new Person("John", "Smith"),
                new Person("Anna", "Martinez"),
                new Person("Paul", "Watson "));

        String joinedFirstNames = list.stream()
                .map(Person::getFirstName)
                .collect(Collectors.joining(", ")); // "John, Anna, Paul"
        System.out.println(joinedFirstNames);
    }

    /**
     * Java 8 provides Streams, which makes many Collection operations easy.
     * Streaming items from a collector and filtering the data are trivial,
     * as well as are sorting, searching, and computing aggregates. That is,
     * if you are familiar with the many Collectors functions available. We
     * present some of these functions here.
     */
    @Test
    public void testIntegerSum() {
        Random random = new Random();
        /*
         * The following illustrates a collection operation to collect
         * integers from a streams pipeline into a List of integers.
         */
        List<Integer> numbers = random
                .ints(1, 10)//介于1(包含)到100(不包含)之间的随机数
                .limit(10)
                .boxed()
                .collect(toList());
        /*
         * Computing the sum total of numbers in a List? No longer do you
         * need a loop, an iterator, or temporary variables. Assuming
         * numbers contains a List of integers, the following neatly
         * computes the result.
         */
        int sum = numbers.stream().reduce(0, (x, y) -> x + y);
        System.out.println(sum);
        /*
         * Here is another way of computing the sum using Stream.collect()
         * instead of Stream.reduce() as above. You can use either
         * alternative as per your preference.
         */
        sum = numbers.stream().collect(summingInt(x -> x));
        System.out.println(sum);
    }

    @Test
    public void testAverages() {
        List<Integer> numbers = new Random()
                .ints(1, 12)
                .limit(12)
                .boxed()
                .collect(toList());
        numbers.forEach(System.out::println);
        double avg = numbers.stream().collect(Collectors.averagingInt(x -> x));
        System.out.println(avg);
    }

    @Test
    public void testMaxAndMin() {
        List<Integer> numbers = new Random()
                .ints(20, 50)
                .limit(9)
                .boxed()
                .collect(toList());
        Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Integer::compare));
        System.out.println(max);
        Optional<Integer> min = numbers.stream().collect(Collectors.minBy(Integer::compare));
        System.out.println(min);
        System.out.println(numbers.stream().map(n -> n.toString()).collect(Collectors.joining(",", "'", "'")));
    }

    /**
     * @of join某个list中元素，逗号隔开，并且每个元素用单引号括起来
     * '4', '5', '5', '5', '8', '9', '7', '3', '8'
     * @on
     */
    @Test
    public void testStreamJoin() {
        List<Integer> numbers = new Random()
                .ints(1, 10)
                .limit(9)
                .boxed()
                .collect(toList());
        String result = numbers.stream()
                .map(n -> "'" + n + "'")
                .collect(Collectors.joining(", "));
        System.out.println(result);
    }

    @Test
    public void testSortLocalDate() {
        List<LocalDate> dates = asList(LocalDate.of(2018, 2, 28), LocalDate.of(2018, 2, 27), LocalDate.of(2018, 3, 1));
        LocalDate firstDate = dates.stream()
                .sorted((date1, date2) -> date2.compareTo(date1))
                .findFirst()
                .get();
        System.out.println(firstDate);
    }

    /*
     * In this article, we will have a quick look into ways of getting the
     * last element of a Stream. Keep in mind that due to the nature of
     * streams, it’s not a natural operation. Always make sure that you’re
     * not working with infinite Streams.
     *
     * Reduce, simply put, reduces the set of elements in a Stream to a
     * single element.
     *
     * In this case, we’ll reduce the set of elements to get the last
     * element fn the Stream. Keep in mind that this method will only return
     * deterministic results for sequential Streams.
     */
    @Test
    public void testGetLastElementOfStream() {
        List<String> valueList = new ArrayList<>();
        valueList.add("Joe");
        valueList.add("John");
        valueList.add("Sean");
        String value = valueList.stream()
                .reduce((first, second) -> second)
                .orElse(null);
        System.out.println(value);

        LocalDate lastDay = asList(LocalDate.of(2018, 3, 7),
                LocalDate.of(2018, 3, 5),
                LocalDate.of(2018, 3, 3))
                .stream()
                .sorted()
                .reduce((first, second) -> second)
                .orElse(null);
        System.out.println(lastDay);

    }

    /**
     * Group by a List and display the total count of it.
     * 输出：{papaya=1, orange=1, banana=2, apple=3}
     *
     * @on
     */
    @Test
    public void testGroupingByCounting() {
        //3 apple, 2 banana, others 1
        List<String> items = Arrays.asList("apple", "apple", "banana",
                "apple", "orange", "banana", "papaya");

        Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));
        System.out.println(result);
    }

    /**
     * {apple=3, banana=2, papaya=1, orange=1}
     */
    @Test
    public void testGroupingByCountingSorted() {
        //3 apple, 2 banana, others 1
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");

        Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));

        Map<String, Long> finalMap = new LinkedHashMap<>();

        //Sort a map and add to finalMap
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap);

    }

    @Test
    public void testGroupByNameCount() {
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99")));

        Map<String, Long> counting = items.stream()
                .collect(groupingBy(Item::getName, counting()));
        System.out.println(counting);

        Map<String, Integer> sum = items.stream()
                .collect(groupingBy(Item::getName, summingInt(Item::getQty)));
        System.out.println(sum);
    }

    @Test
    public void testGroupByMapping() {
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99")));

        //group by price
        Map<BigDecimal, List<Item>> groupByPriceMap = items.stream()
                .collect(groupingBy(Item::getPrice));
        System.out.println(groupByPriceMap);

        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> result = items.stream()
                .collect(groupingBy(Item::getPrice, mapping(Item::getName, toSet())));
        System.out.println(result);
    }

    /**
     * @of +----------+------------+-----------------+
     * | Name     | City       | Number of Sales |
     * +----------+------------+-----------------+
     * | Alice    | London     | 200             |
     * | Bob      | London     | 150             |
     * | Charles  | New York   | 160             |
     * | Dorothy  | Hong Kong  | 190             |
     * +----------+------------+-----------------+
     * @on
     */
    @Test
    public void testGrouping() {
        List<Employee> employees = newArrayList(new Employee("Alice", "London", 200),
                new Employee("Bob", "London", 150),
                new Employee("Charles", "New York", 160),
                new Employee("Dorothy", "Hong Kong", 190));

        //传统做法
        Map<String, List<Employee>> result = new HashMap<String, List<Employee>>();
        for (Employee employee : employees) {
            String city = employee.getCity();
            List<Employee> empsInCity = result.get(city);
            if (empsInCity == null) {
                empsInCity = new ArrayList<>();
                result.put(city, empsInCity);
            }
            empsInCity.add(employee);
        }

        //Java8的做法
        Map<String, List<Employee>> employeesByCity = employees.stream().collect(groupingBy(Employee::getCity));
        System.out.println(toJson(employeesByCity));

        /*
         * It’s also possible to count the number of employees in each city,
         * by passing a counting collector to the groupingBy collector. The
         * second collector performs a further reduction operation on all
         * the elements in the stream classified into the same group.
         */
        Map<String, Long> numEmployeesByCity = employees.stream()
                .collect(groupingBy(Employee::getCity, counting()));
        System.out.println(numEmployeesByCity);

        /*
         * Another example is calculating the average number of sales in
         * each city, which can be done using the averagingInt collector in
         * conjuction with the groupingBy collector
         */
        Map<String, Double> avgSalesByCity = employees.stream()
                .collect(groupingBy(Employee::getCity, Collectors.averagingInt(Employee::getSalesInt)));
        System.out.println(avgSalesByCity);
    }

    /**
     * Partitioning is a special kind of grouping, in which the resultant
     * map contains at most two different groups – one for true and one for
     * false. For instance, if you want to find out who your best employees
     * are, you can partition them into those who made more than N sales and
     * those who didn’t, using the partitioningBy collector
     */
    @Test
    public void testPartitioning() {
        List<Employee> employees = newArrayList(new Employee("Alice", "London", 200),
                new Employee("Bob", "London", 150),
                new Employee("Charles", "New York", 160),
                new Employee("Dorothy", "Hong Kong", 190));
        Map<Boolean, List<Employee>> partitioned = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.getSales() > 150));
        System.out.println(toJson(partitioned));

        /*
         * You can also combine partitioning and grouping by passing a
         * groupingBy collector to the partitioningBy collector. For
         * example, you could count the number of employees in each city
         * within each partition:
         */
        Map<Boolean, Map<String, Long>> result = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.getSales() > 150,
                        groupingBy(Employee::getCity, counting())));
        System.out.println(result);
    }

    @Test
    public void testGroupingTwoVO() {
        List<String[]> userPhones = new ArrayList<String[]>();
        userPhones.add(new String[]{"rico", "21", "2012-11-09", "iphone", "13913528171"});
        userPhones.add(new String[]{"jason", "26", "2012-11-08", "iphone", "1381372626"});
        userPhones.add(new String[]{"vivi", "23", "2012-11-07", "xiaomi", "13913528172"});
        userPhones.add(new String[]{"jason", "26", "2012-11-08", "iphone", "1381372626"});
        userPhones.add(new String[]{"rico", "21", "2012-11-09", "xiaomi", "13913528171"});
        userPhones.add(new String[]{"rico", "21", "2012-11-09", "iphone", "1381372626"});

        //userPhones.stream()
        //.collect(groupingBy(collectors, downstream))
    }

    @Test
    public void testGroupingBySummingBigdecimal() {
        Map<String, BigDecimal> result = data().stream()
                .collect(Collectors.groupingBy(Emp::getDept,
                        Collectors.mapping(Emp::getSalary, reducing(BigDecimal.ZERO, BigDecimal::add))));
        System.out.println(result);
    }

    // -------- 测试三种List<String> 转 String的方法
    @Test
    public void testList2Str() {
        List<String> cities = Arrays.asList("Milan", "London", "New York", "San Francisco");
        String citiesCommaSeparated = String.join(",", cities);
        System.out.println(citiesCommaSeparated); //Output: Milan,London,New York,San Francisco
    }

    @Test
    public void testList2StrStream() {
        List<String> cities = Arrays.asList("Milan", "London", "New York", "San Francisco");
        String citiesCommaSeparated = cities.stream().collect(Collectors.joining(","));
        System.out.println(citiesCommaSeparated); //Output: Milan,London,New York,San Francisco
    }

    @Test
    public void testList2StrJava7() {
        String separator = ",";
        List<String> cities = Arrays.asList("Milan", "London", "New York", "San Francisco");
        StringBuilder csvBuilder = new StringBuilder();
        for (String city : cities) {
            csvBuilder.append(city);
            csvBuilder.append(separator);
        }
        String csv = csvBuilder.toString();
        System.out.println(csv); //OUTPUT: Milan,London,New York,San Francisco,
        //Remove last comma
        csv = csv.substring(0, csv.length() - separator.length());
        System.out.println(csv); //OUTPUT: Milan,London,New York,San Francisco }
    }

    private static class Emp {
        public Emp(String name, String dept, BigDecimal salary) {
            super();
            this.name = name;
            this.dept = dept;
            this.salary = salary;
        }

        private String name;
        private String dept;
        private BigDecimal salary;

        public String getName() {
            return name;
        }

        public String getDept() {
            return dept;
        }

        public BigDecimal getSalary() {
            return salary;
        }
    }

    private static List<Emp> data() {
        Emp e1 = new Emp("e1", "dept1", BigDecimal.valueOf(1.1));
        Emp e2 = new Emp("e2", "dept1", BigDecimal.valueOf(1.1));
        Emp e3 = new Emp("e3", "dept2", BigDecimal.valueOf(2.1));
        Emp e4 = new Emp("e4", "dept2", BigDecimal.valueOf(1.9));
        return Arrays.asList(e1, e2, e3, e4);
    }

    private static class User {
        private String name;
        private int age;
        private LocalDateTime birthday;

        private List<Phone> phones = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LocalDateTime getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDateTime birthday) {
            this.birthday = birthday;
        }

    }

    private static class Phone {
        private String type;
        private String phoneNumber;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

    }

    private static class Employee {
        private String name;
        private String city;
        private long sales;

        public Employee(String name, String city, long sales) {
            this.name = name;
            this.city = city;
            this.sales = sales;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public long getSales() {
            return sales;
        }

        public void setSales(long sales) {
            this.sales = sales;
        }

        public int getSalesInt() {
            return (int) sales;
        }

    }

    private static class Item {
        private String name;
        private int qty;
        private BigDecimal price;

        @Override
        public String toString() {
            return toJson(this);
        }

        public Item(String name, int qty, BigDecimal price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

    }
}
