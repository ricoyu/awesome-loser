package com.loserico.io;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The class java.io.OutputStream is the base class of all Java IO output streams. 
 * If you are writing a component that needs to write output to a stream, 
 * try to make sure that component depends on an OutputStream and not one of its subclasses.
 * 
 * <p>
 * Copyright: (C), 2020-10-13 18:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OutputStreamTest {
	
	@Test
	public void testWrite() {
		File file;
		try (OutputStream output = new FileOutputStream("D:\\rico.txt")) {
			output.write("Hello World".getBytes(UTF_8));
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
