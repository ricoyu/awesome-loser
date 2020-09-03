package com.loserico.jackson.dynamicbeans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URL;

/**
 * <p>
 * Copyright: (C), 2020-08-19 16:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DataBindingDynaBean {
	
	@SneakyThrows
	public static void main(String[] args) {
		String url = "http://freemusicarchive.org/api/get/albums.json?api_key=60BLHNQCAOUFPIBZ&limit=2";
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		AlbumsDynamic albums = mapper.readValue(new URL(url), AlbumsDynamic.class);
		DatasetDynamic[] datasets = albums.getDataset();
		for (DatasetDynamic dataset : datasets) {
			System.out.println(dataset.get("album_type"));
		}
	}
}
