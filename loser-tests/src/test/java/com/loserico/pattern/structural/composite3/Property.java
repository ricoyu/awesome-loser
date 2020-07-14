package com.loserico.pattern.structural.composite3;

/**
 * 这是抽象类 Base Component Class
 * 
 * A base component class in a composite design pattern is the class that
 * actually implements the core object or the parent object for succeeding
 * class. For instance, a Airways class, A Vehicle class, A Shape are also
 * generic classes which will be then implemented by related sub classes. The
 * base component class provides an abstract layer of functionalities required
 * by the implementing subclasses. These subclasses will then have similar
 * functionalities as their parent classes plus the features specific to
 * themselves.
 * 
 * The primary reason for creating this base component class is not only to
 * provide common functionalities to the implementing classes but also to allow
 * the final composite class to utilise the Leaf classes in a single collection.
 * This will be further explained in the coding part
 * 
 * In order to implement and understand composite design pattern, we will need a
 * few real life entities with common behaviour. Let us take the case of a real
 * estate company. A real estate company sells a variety of properties. A
 * property is the base component class here.
 * 
 * The next step is to create different types of properties that might be
 * possible. Here we are considering three different types of properties –
 * Apartments, Bungalow and Tenaments.
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:24
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface Property {

	public void purchase();

	public void sell();
}