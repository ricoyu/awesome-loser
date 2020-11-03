package com.loserico.io;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.FileReader;

/**
 * <p>
 * Copyright: (C), 2020-10-13 15:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReaderTest {
	
	@SneakyThrows
	@Test
	public void testFileReader() {
		FileReader reader = new FileReader("d:\\Dropbox\\Java\\Java技巧.txt");
		int theCharNum = reader.read();
		while (theCharNum != -1) {
			char theChar = (char)theCharNum;
			System.out.println(theChar);
			theCharNum = reader.read();
		}
	}
}
