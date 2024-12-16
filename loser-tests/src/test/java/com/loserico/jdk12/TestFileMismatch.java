package com.loserico.jdk12;

import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestFileMismatch {

	@Test
	public void test() throws IOException {
		FileWriter fileWriter = new FileWriter("d:/a.txt");
		fileWriter.write("a");
		fileWriter.write("b");
		fileWriter.write("c");
		fileWriter.close();
		FileWriter fileWriterB = new FileWriter("d:/b.txt");
		fileWriterB.write("a");
		fileWriterB.write("1");
		fileWriterB.write("c");
		fileWriterB.close();
		System.out.println(Files.mismatch(Path.of("d:/a.txt"),Path.of("d:/b.txt")));
	}
}
