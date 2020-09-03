package com.loserico.nio.pipes;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;

/**
 * https://www.studytrails.com/java-io/pipes-print-writer/
 * <p>
 * Copyright: (C), 2020-08-19 17:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReadFromConsoleUsingPipes {
	
	/**
	 * We see the use of a buffered reader and writer and a piped reader and writer.
	 * The example also demonstrates how to read from a console continuously.
	 * The example reads from a console using a buffered reader, passes the characters to a piped writer.
	 * A piped reader reads the characters and writes to a file using buffered writer
	 *
	 * @param args
	 */
	@SneakyThrows
	public static void main(String[] args) {
		PipedReader pipedReader = new PipedReader();
		PipedWriter pipedWriter = new PipedWriter(pipedReader);
		
		ScreenReader reader = new ScreenReader(pipedWriter);
		OutputWriter writer = new OutputWriter(pipedReader);
		
		reader.start();
		writer.start();
	}
	
	/**
	 * A Reader thread to read continously from the input. The characters read are written to a piped writer
	 */
	static class ScreenReader extends Thread {
		private PipedWriter writer;
		
		public ScreenReader(PipedWriter pipedWriter) {
			this.writer = pipedWriter;
		}
		
		@SneakyThrows
		@Override
		public void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String s = reader.readLine();
				if ("exit".equalsIgnoreCase(s.trim())) {
					reader.close();
					writer.close();
					break;
				}
				writer.write(s);
			}
		}
	}
	
	/**
	 * A writer thread to write data to a file. The data is read from a piped reader
	 */
	static class OutputWriter extends Thread {
		
		private PipedReader reader;
		
		public OutputWriter(PipedReader pipedReader) {
			this.reader = pipedReader;
		}
		
		@SneakyThrows
		@Override
		public void run() {
			PrintWriter writer = new PrintWriter(new FileWriter("pipedOut.txt"), true);
			BufferedReader consoleReader = new BufferedReader(reader);
			
			while (true) {
				String s = consoleReader.readLine();
				if (s == null) {
					reader.close();
					writer.close();
					return;
				}
				writer.println(s);
			}
		}
	}
}
