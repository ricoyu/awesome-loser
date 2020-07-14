package com.loserico.aio.sscdemo;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

/**
 * ReadWriteHandler is a CompletionHandler that responds to read() or write() method
 * completions. Its completed() method is called to respond to a successful read() or
 * write(), which includes issuing a counterpart write() or read(). Its failed()
 * method is called when the client breaks off its connection with the server.
 * 
 * @author Rico Yu
 * @since 2016-12-18 16:10
 * @version 1.0
 *
 */
public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	/*
	 * The completed() method’s first task is to respond to a read() call that returns
	 * -1, which indicates that no bytes could be read because end-ofstream has been
	 * reached. (The client may have terminated before writing data.) It closes the
	 * client socket channel, outputs a message to indicate that the server is no
	 * longer listening to the client, and returns.
	 * 
	 * Assuming that the read/write handler is operating in read mode (true), the
	 * buffer contents are flipped and extracted to a bytes array, which is
	 * subsequently converted to a string that is output.
	 * 
	 * The read/write handler is then configured for write mode by assigning false to
	 * isReadMode, the buffer is rewound in preparation for being written, and the
	 * buffer along with the attachment and current completion handler are passed to
	 * the client socket channel’s write() method so that the buffer contents can be
	 * sent to the client.
	 * 
	 * After a successful write operation, the read/write handler’s completed() method
	 * is called. Because isReadMode is no longer true, the else part of the if-else
	 * statement executes. It toggles isReadMode to true, clears the buffer, and
	 * initiates a read operation to read from the client. This pattern continues
	 * until the client presents nothing more to read and the completion handler can
	 * terminate. (This latter scenario will probably never happen.)
	 */
	@Override
	public void completed(Integer result, Attachment attachment) {
		if (result == -1) {
			try {
				attachment.channelClient.close();
				System.out.printf("Stopped listening to client %s%n", attachment.clientAddr);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return;
		}
		if (attachment.isReadMode) {
			attachment.buffer.flip();
			int limit = attachment.buffer.limit();
			byte bytes[] = new byte[limit];
			attachment.buffer.get(bytes, 0, limit);
			System.out.printf("Client at %s sends message: %s%n", attachment.clientAddr, new String(bytes, UTF_8));
			attachment.isReadMode = false;
			attachment.buffer.rewind();
			attachment.channelClient.write(attachment.buffer, attachment, this);
		} else {
			attachment.isReadMode = true;
			attachment.buffer.clear();
			attachment.channelClient.read(attachment.buffer, attachment, this);
		}
	}

	@Override
	public void failed(Throwable t, Attachment att) {
		System.out.println("Connection with client broken");
	}
}