package com.loserico.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadExample {

	private Thread currentThread;

	public static void main(String[] args) throws Exception {
		new ReadExample().readFile();
	}

	private void readFile() throws IOException {
		String filePath = "readfile.txt";
		printFileContents(filePath);
		Path path = Paths.get(filePath);

		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		currentThread = Thread.currentThread();
		
		channel.read(buffer, 0, "Read operation ALFA", new CompletionHandler<Integer, Object>() {
			@Override
			public void completed(Integer result, Object attachment) {
				System.out.println(attachment + " completed and " + result + " bytes are read. ");
				currentThread.interrupt();
			}

			@Override
			public void failed(Throwable e, Object attachment) {
				System.out.println(attachment + " failed with exception:");
				e.printStackTrace();
				currentThread.interrupt();
			}
		});

		System.out.println("Waiting for completion...");

		try {
			currentThread.join();
		} catch (InterruptedException e) {
		}

		buffer.flip();

		System.out.print("Buffer contents: ");

		while (buffer.hasRemaining()) {
			System.out.print((char) buffer.get());
		}
		System.out.println(" ");

		buffer.clear();
		channel.close();
	}

	private void printFileContents(String path)
			throws IOException {

		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);

		String textRead = br.readLine();
		System.out.println("File contents: ");

		while (textRead != null) {

			System.out.println("     " + textRead);
			textRead = br.readLine();
		}

		fr.close();
		br.close();
	}
}