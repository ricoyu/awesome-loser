package com.loserico.nio.printwriter;

import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * <p>
 * Copyright: (C), 2020-08-19 17:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PrintWriterToText {
	
	@SneakyThrows
	public static void main(String[] args) {
		//Create a PrintWriter
		PrintWriter writer = new PrintWriter(new File("output.txt"));
		
		// It is also possible to create a filewriter with automatic flushing.
		// However autoflushing only happens when one of the println,printf or format methods are called.
		PrintWriter writer2 = new PrintWriter(new BufferedWriter(new FileWriter(new File("output2.txt"))), true);
		
		//Write characters to file using printwriter. All methods do the same thing
		writer.append('a');
		writer.write('b');
		writer.print('k');
		
		//write part of string to a file
		writer.append("def", 1, 1);
		writer.write("keep", 2, 2);
		
		//write java objects to a file
		writer.print(true);
		// double
		writer.print(1.434);
		// float
		writer.print(1.23f);
		writer.print(2);
		// writes any object to a file by calling its toString method.
		writer.print(writer);
		
		// terminates the current line. Uses the system specific line separator.
		writer.println();
		
		// use println methods with any object to write the string 
		//representation of the object followed by a new line.
		writer.println(3);
		
		// Write writer formatted string to file.
		// The java formatter can be used to format a string. This method writes
		// formatted string to a file based on the default formatter
		writer.printf("%a", 34.0);
		
		// the flush method needs to be called explicitly unless one of the
		// methods that flush automatically are called.
		writer.flush();
		
		//Check for errors since PrintWriter does not throw IOException
		writer.checkError();
		writer.close();
	}
}
