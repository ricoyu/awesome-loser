package com.loserico.jackson.ignorefields;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URL;

/**
 * <p>
 * Copyright: (C), 2020-08-19 17:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DataBindingFilter {
	
	@SneakyThrows
	public static void main(String[] args) {
		String url = "http://freemusicarchive.org/api/get/albums.json?api_key=60BLHNQCAOUFPIBZ&limit=2";
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		AlbumsFilter albums = mapper.readValue(new URL(url), AlbumsFilter.class);
		System.out.println(albums.getTotal_pages());
		System.out.println(albums.getTitle());
		for (DatasetFilter dataset : albums.getDatasetFilter()) {
			System.out.println(dataset.getAlbum_comments());
			System.out.println(dataset.get("album_images"));
			System.out.println(dataset.get("tags"));
			System.out.println(dataset.get("album_listens"));
			break;
		}
	}
}
