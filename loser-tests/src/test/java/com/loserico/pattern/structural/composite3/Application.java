package com.loserico.pattern.structural.composite3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 组合节点<p>
 * A composite class.
 * A composite class could be any module that uses a collection of different types of Property.
 * 
 * Benefits of using Composite Design pattern
 * The benefits of composite design pattern are better understood when the pattern is not used. 
 * Imagine two different scenarios.
 * <ul>
 * <li>A scenario where different types of properties are all handled by a single class named Property
 * <li>A scenario where there is no generic interface and all the classes are independent
 * </ul>
 * In the first scenario, all the data will be gathered by the class Property. Thus, the amount 
 * of data and memory occupied by the object will be higher since the data from multiple property 
 * types will be present in every object. This increases redundancy in terms of data
 * 
 * In the second scenario, all the data will be distributed property wise in respective classes. 
 * However, even the common data will be distributed among the classes which will be a code redundancy. 
 * This code redundancy becomes difficult to manage when the property types increase or rather the leaf 
 * classes increase.
 * 
 * In this manner, Composite design pattern not only helps in optimising memory usage but also helps 
 * in reducing code.
 * <p>
 * Points to note<p>
 * For the composite design pattern, below are few points that need to be noted
 * <ul>
 * <li>Apply composite design pattern only on the objects that have common group behaviour.
 * <li>Composite design pattern is used to create a tree leaf structure using the classes to represent a part-whole structure.
 * <li>When dealing with Tree-structured data, programmers often have to discriminate between a leaf-node and a branch. 
 * This makes code more complex, and therefore, error prone. 
 * The solution is an interface that allows treating complex and primitive objects uniformly.
 * <li>In object-oriented programming, a composite is an object designed as a composition of one-or-more similar objects, 
 * all exhibiting similar functionality. This is known as a “has-a” relationship between objects.
 * </ul>
 * <p>
 * Scenarios where composite pattern is a NO<p>
 * With Composite design pattern, it is a tough job to restrict the type of components in the composite class. 
 * So, composite design pattern shouldn’t be used when we wish to conceal the partial or full hierarchy of the objects.
 * 
 * Composite design pattern generalises the design to a great extent. Not every developer is fond of developing 
 * a code that is so generalised. When the aim is to develop a certain level of complexity in the code structure, 
 * composite design pattern is a bad choice to use.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:41
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Application {

	public static void main(String[] args) {
		/*
		 * Notice carefully that the properties being used is of type Property. Although
		 * it is a class, Java consider interface to be a type when you to define a
		 * generic instance. These element which are qualified by interface type can
		 * then be initialised using any objects of the class implementing the
		 * respective interface. Thus, the list in the above code can contain three
		 * different types of object – Apartments, Tenaments & Bungalow.
		 * 
		 * In this manner, the class contains all the three types of objects, without
		 * really declaring an object of corresponding type. Such a class is called a
		 * composite class. This and several other such classes, collectively form a
		 * project using composite design pattern. It should be understood here that
		 * composite design pattern is used only when there are a large variety of
		 * entities with similar properties and these entities need to be used together
		 * in various classes of the application class.
		 */
		List<Property> properties = new ArrayList<>();
		Scanner in = new Scanner(System.in);
		for (int i = 0; i < 5; i++) {
			Property p = null;
			System.out.println("Choose type of property to add");
			System.out.println("1. Apartment\n2. Tenaments\n3. Bungalow");
			int type = in.nextInt();

			// Initialise with respective type
			if (type == 1) {
				p = new Apartment();
			} else if (type == 2) {
				p = new Tenaments();
			} else {
				p = new Bungalow();
			}
			// Gather the properties
			// Do the desired task with the properties
		}
	}
}
