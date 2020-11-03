package com.loserico.io;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The class java.io.InputStream is the base class for all Java IO input streams. 
 * If you are writing a component that needs to read input from a stream, try to make our component depend on an InputStream, 
 * rather than any of it's subclasses (e.g. FileInputStream). Doing so makes your code able to work with all types of input streams, 
 * instead of only the concrete subclass.
 * 
 * You typically read data from an InputStream by calling the read() method. 
 * The read() method returns a int containing the byte value of the byte read. 
 * If there is no more data to be read, the read() method typically returns -1;
 * 
 * <p>
 * Copyright: (C), 2020-10-13 17:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InputStreamTest {
	
	@Test
	public void testRead() {
		try (InputStream in = new FileInputStream("D:\\Dropbox\\Lua\\Lua学习笔记.txt")) {
			int data = in.read();
			while (data != -1) {
				System.out.println(data);
				data = in.read();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
