package com.loserico.aio.sscdemo.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.CompletionHandler;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
	private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * The completed() method’s first task is to determine if a read() has been
	 * performed and respond accordingly. If so, it extracts the buffer’s bytes into
	 * an array, which is converted to a string that is subsequently output.
	 * 
	 * Next, completed() gives the user the opportunity to enter a message that will
	 * be sent to the server. If the user specifies end, the client thread is
	 * interrupted and the client terminates. Any other message’s bytes are extracted
	 * from the string and stored in the buffer, which is subsequently written to the
	 * socket channel.
	 */
	@Override
	public void completed(Integer result, Attachment attachment) {
		if (attachment.isReadMode) {
			attachment.buffer.flip();
			int limit = attachment.buffer.limit();
			byte[] bytes = new byte[limit];
			attachment.buffer.get(bytes, 0, limit);
			String msg = new String(bytes, UTF_8);
			System.out.printf("Server responded: %s%n", msg);

			try {
				msg = "";
				while (msg.length() == 0) {
					System.out.print("Enter message (\"end\" to quit): ");
					msg = bufferedReader.readLine();
				}
				if (msg.equalsIgnoreCase("end")) {
					attachment.mainThd.interrupt();
					return;
				}
			} catch (IOException ioe) {
				System.err.println("Unable to read from console");
			}
			attachment.isReadMode = false;
			attachment.buffer.clear();
			byte[] data = msg.getBytes(UTF_8);
			attachment.buffer.put(data);
			attachment.buffer.flip();
			attachment.channel.write(attachment.buffer, attachment, this);
		} else {
			attachment.isReadMode = true;
			attachment.buffer.clear();
			attachment.channel.read(attachment.buffer, attachment, this);
		}
	}

	@Override
	public void failed(Throwable t, Attachment att) {
		System.err.println("Server not responding");
		System.exit(1);
	}
}