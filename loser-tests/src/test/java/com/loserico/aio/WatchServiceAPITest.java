package com.loserico.aio;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.junit.Test;

/**
 * The improved file system interface includes a Watch Service API that’s used to
 * watch registered directories for changes and events.
 * 
 * For example, a file manager can use a watch service to monitor a directory for
 * changes so that it can update its list-of-files display when files are created or
 * deleted.
 * 
 * @author Rico Yu
 * @since 2016-12-17 10:08
 * @version 1.0
 *
 */
public class WatchServiceAPITest {

	@Test
	public void testWatchService() throws IOException {
		FileSystem fsDefault = FileSystems.getDefault();
		WatchService ws = fsDefault.newWatchService();
		String temp = System.getProperty("java.io.tmpdir");
		/*
		 * It then creates a watch service, converts the temp to a Path object, and
		 * registers the Path object with the watch service. Events are to be
		 * triggered when any entries are created, deleted, or modified in the
		 * directory identified by the Path object.
		 */
		Path dir = fsDefault.getPath(temp);
		dir.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

		/*
		 * At this point, an infinite loop is entered to take the next watch key and
		 * poll its events. For each event, the kind is determined by calling
		 * WatchEvent’s WatchEvent.Kind<T> kind() method. If the kind is OVERFLOW, a
		 * message stating this fact is output. Otherwise, WatchEvent’s T context()
		 * method is called to return the event context, which is subsequently output.
		 * For ENTRY_CREATE, ENTRY_DELETE, and ENTRY_MODIFY events, the context is a
		 * Path identifying the relative path between the directory registered with
		 * the watch service and the entry that’s created, deleted, or modified.
		 */
		for (;;) {
			WatchKey key;
			try {
				key = ws.take();
			} catch (InterruptedException ie) {
				return;
			}
			for (WatchEvent event : key.pollEvents()) {
				if (event.kind() == OVERFLOW) {
					System.out.println("overflow");
					continue;
				}
				WatchEvent ev = (WatchEvent) event;
				Path filename = (Path) ev.context();
				System.out.printf("%s: %s%n", ev.kind(), filename);
			}

			/*
			 * Lastly, the loop attempts to reset the key. If reset fails because the
			 * key is no longer valid (perhaps because the watch service has been
			 * closed), the loop is broken and the application ends. Resetting the key
			 * is very important. If you fail to invoke reset(), this key will not
			 * receive any further events.
			 */
			boolean valid = key.reset();
			if (!valid)
				break;
		}
	}
}
