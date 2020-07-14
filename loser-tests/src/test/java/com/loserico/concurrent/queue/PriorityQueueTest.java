package com.loserico.concurrent.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * <p>
 * Copyright: (C), 2020/1/8 9:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PriorityQueueTest {
	
	@Test
	public void testPriorityQueue() {
		PriorityQueue<Integer> queue = new PriorityQueue<>();
		queue.add(10);
		queue.add(20);
		queue.add(15);
		
		// Printing the top element of PriorityQueue 
		System.out.println(queue.peek());
		
		//Printing the top element and removing it from the PriorityQueue container 
		System.out.println(queue.poll());
		
		//Printing the top element again 
		System.out.println(queue.peek());
	}
	
	@Test
	public void testPriorityQueueBasicOperation() {
		PriorityQueue<String> queue = new PriorityQueue<>();
		queue.add("C");
		queue.add("C++");
		queue.add("Java");
		queue.add("Python");
		
		//Printing the most priority element
		System.out.println("Head value using peek function: " + queue.peek());
		
		//Printing all elements 
		System.out.println("The queue elements:");
		for (String s : queue) {
			System.out.println(s);
		}
		
		// Removing the top priority element (or head) and printing the modified pQueue using poll() 
		queue.poll();
		System.out.println("After removing an element with poll function:");
		Iterator<String> it = queue.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
		//Removing Java using remove() 
		queue.remove("Java");
		System.out.println("after removing Java with remove function:");
		for (String s : queue) {
			System.out.println(s);
		}
		
		//Check if an element is present using contains() 
		boolean b = queue.contains("C");
		System.out.println("Priority queue contains C or not?: " + b);
		
		//Getting objects from the queue using toArray() in an array and print the array
		Object[] arr = queue.toArray();
		System.out.println("Value in array: ");
		for (int i = 0; i < arr.length; i++) {
			System.out.println("Value: " + arr[i].toString());
		}
	}
	
	/**
	 * Java program to demonstrate working of comparator based priority queue constructor
	 */
	@Test
	public void testComparatorBasedPriorityQueue() {
		PriorityQueue<Student> queue = new PriorityQueue<>(5, new StudentComparator());
		
		Student student1 = new Student("Nandini", 3.2);
		queue.add(student1);
		
		Student student2 = new Student("Anmol", 3.6);
		queue.add(student2);
		
		Student student3 = new Student("Palak", 4.0);
		queue.add(student3);
		
		System.out.println("Students served in their priority order");
		
		while (!queue.isEmpty()) {
			System.out.println(queue.poll());
		}
	}
	
	class StudentComparator implements Comparator<Student> {
		@Override
		public int compare(Student s1, Student s2) {
			if (s1.cgpa > s2.cgpa) {
				return -1;
			}
			if (s1.cgpa < s2.cgpa) {
				return 1;
			}
			return 0;
		}
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	class Student {
		
		private String name;
		private double cgpa;
	}
}
