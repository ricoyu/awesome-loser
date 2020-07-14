package com.loserico.pattern.structural.adapter;

/**
 * Suppose we have a bird that can makeSound(), and we have a plastic toy duck that can squeak().
 * Now suppose our client changes the requirement and he wants the toyDuck to makeSound
 * <p>
 * Simple solution is that we will just change the implementation class to the new adapter class and
 * tell the client to pass the instance of the bird(which wants to squeak()) to that class.
 * <p>
 * Before : ToyDuck toyDuck = new PlasticToyDuck();
 * After : ToyDuck toyDuck = new BirdAdapter(sparrow);
 * <p>
 * You can see that by changing just one line the toyDuck can now do Chirp Chirp !!
 * <p>
 * Copyright: (C), 2020/2/15 12:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Client {
	
	public static void main(String[] args) {
		Bird sparrow = new Sparrow();
		ToyDuck toyDuck = new PlasticToyDuck();
		
		/*
		 * Wrap a bird in a birdAdapter so that it behaves like toy duck
		 */
		ToyDuck birdAdapter = new BirdAdapter(sparrow);
		
		System.out.println("Sparrow...");
		sparrow.fly();
		sparrow.makeSound();
		
		System.out.println("ToyDuck...");
		toyDuck.squeak();
		
		// toy duck behaving like a bird  
		System.out.println("BirdAdapter...");
		birdAdapter.squeak();
	}
}
