package com.loserico.binary;

import org.junit.Test;

public class BitHash {

	@Test
	public void testHash() {
		int h = "hollischuang".hashCode();
		System.out.println("h                     => " + Integer.toBinaryString(h));
		System.out.println("h >>> 20              => " + Integer.toBinaryString(h >>> 20));
		System.out.println("h >>> 12              => " + Integer.toBinaryString(h >>> 12));
		h ^= (h >>> 20);
		System.out.println("h^(h>>>20)            => " + Integer.toBinaryString(h));
		h ^= h >>> 12;
		System.out.println("h=h^(h>>>20)^(h>>>12) => " + Integer.toBinaryString(h));
		System.out.println("h >>> 7               => " + Integer.toBinaryString(h >>> 7));
		System.out.println("h >>> 4               => " + Integer.toBinaryString(h >>> 4));
		h ^= (h >>> 7) ^ (h >>> 4);
		System.out.println("h^(h>>>7)^(h>>>4)     => " + Integer.toBinaryString(h));
		System.out.println("15                    => " + Integer.toBinaryString(15));
		System.out.println("h & 15                => " + Integer.toBinaryString(h & 15));
	}
}
