package com.loserico.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFileFromUrl {

	// File Location
	private static String filePath = "config/sample.txt";

	// Sample Url Location
	private static String sampleUrl = "http://www.google.com";

	// This Method Is Used To Download A Sample File From The Url
	private static void downloadFileFromUrlUsingNio() {
		URL urlObj = null;
		ReadableByteChannel rbcObj = null;
		FileOutputStream fOutStream = null;

		// Checking If The File Exists At The Specified Location Or Not
		Path filePathObj = Paths.get(filePath);
		boolean fileExists = Files.exists(filePathObj);
		if (fileExists) {
			try {
				urlObj = new URL(sampleUrl);
				rbcObj = Channels.newChannel(urlObj.openStream());
				fOutStream = new FileOutputStream(filePath);

				fOutStream.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
				System.out.println("! File Successfully Downloaded From The Url !");
			} catch (IOException ioExObj) {
				System.out.println("Problem Occured While Downloading The File= " + ioExObj.getMessage());
			} finally {
				try {
					if (fOutStream != null) {
						fOutStream.close();
					}
					if (rbcObj != null) {
						rbcObj.close();
					}
				} catch (IOException ioExObj) {
					System.out.println("Problem Occured While Closing The Object= " + ioExObj.getMessage());
				}
			}
		} else {
			System.out.println("File Not Present! Please Check!");
		}
	}

	public static void main(String[] args) {
		downloadFileFromUrlUsingNio();
	}
}