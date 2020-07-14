package com.loserico.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionRemoveTest {

	/**
	 * in the below code we have first added a couple of good programming books e.g.
	 * Programming Pearls, Clean Code, Code Complete into ArrayList and then removing
	 * any element which has Code in its title.
	 * 
	 * In this code, I have used Java 1.5 enhanced for loop, you must know how
	 * enhanced for loop works in Java.
	 * 
	 * Difference between for loop and enhanced for loop is that later internally uses
	 * an Iterator for going over all elements of a collection.
	 */
	@Test
	public void testListRemove() {
		List<String> listOfBooks = new ArrayList<>();
		listOfBooks.add("Programming Pearls");
		listOfBooks.add("Clean Code");
		listOfBooks.add("Effective Java");
		listOfBooks.add("Code Complete");

		// Using forEach loop to iterate and removing 
		// element during iteration will throw 
		// ConcurrentModificationException in Java
		for (String book : listOfBooks) {
			if (book.contains("Code")) {
				listOfBooks.remove(book);
			}
		}
	}

	/**
	 * 使用经典的循环，这次却不会抛ConcurrentModificationException
	 * 
	 * This code doesn’t throw ConcurrentModificationException because here we are not
	 * using Iterator but we are just using traditional for loop.
	 * 
	 * It’s the Iterator which throws ConcurrentModificationException, and not the
	 * remove method of ArrayList, hence you don’t see that error in below code.
	 * 
	 * If you look at the code for ArrayList.java, you will notice that there is a
	 * nested class which implemented Iterator interface and it’s next() method calls
	 * checkForComodification() function which actually checks if ArrayList has
	 * modified during iteration or not, if modCount doesn’t match with
	 * expectedModCount then it throws ConcurrentModificationException.
	 */
	@Test
	public void testListRemove2() {
		List<String> listOfBooks = new ArrayList<>();
		listOfBooks.add("Programming Pearls");
		listOfBooks.add("Clean Code");
		listOfBooks.add("Effective Java");
		listOfBooks.add("Code Complete");

		System.out.println("List before : " + listOfBooks);
		for (int i = 0; i < listOfBooks.size(); i++) {
			String book = listOfBooks.get(i);
			if (book.contains("Programming")) {
				System.out.println("Removing " + book);
				listOfBooks.remove(i); // will throw CME
			}
		}
		System.out.println("List after : " + listOfBooks);
	}

	/**
	 * Right way to remove element is by using Iterator’s remove method
	 * 
	 * Finally, here is the right way to delete an element from ArrayList during
	 * iteration. In this example, we have used Iterator both iterate as well as
	 * remove the element. The code is ok but it has a serious limitation, you can
	 * only use this code to remove the current element. You cannot remove any
	 * arbitrary element from ArrayList in Java.
	 * 
	 * The same behavior is applicable to ListIterator as well. I mean you can replace
	 * Iterator with ListIterator and code will work fine. The ListIterator also allow
	 * you to navigate in both directions i.e. forward and backward.
	 */
	@Test
	public void testListRemoveViaIterator() {
		List<String> listOfBooks = new ArrayList<>();
		listOfBooks.add("Programming Pearls");
		listOfBooks.add("Clean Code");
		listOfBooks.add("Effective Java");
		listOfBooks.add("Code Complete");

		System.out.println("List before : " + listOfBooks);
		for (Iterator<String> it = listOfBooks.iterator(); it.hasNext();) {
			String book = it.next();
			System.out.println("Removing " + book);
			it.remove();
		}
		System.out.println("List after : " + listOfBooks);
	}
}
