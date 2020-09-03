package com.loserico.jackson.dynamicbeans;

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
public class AlbumsDynamic {
	
	private String title;
	
	private DatasetDynamic[] dataset;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public DatasetDynamic[] getDataset() {
		return dataset;
	}
	
	public void setDataset(DatasetDynamic[] dataset) {
		this.dataset = dataset;
	}
}
