package com.loserico.jackson.ignorefields;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * https://www.studytrails.com/java/json/java-jackson-data-binding-filters/
 * <p>
 * Copyright: (C), 2020-08-19 16:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
// Do not use fields to autodetect. use the public getter methods to autodetect properties
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
public class AlbumsFilter {
	
	private String title;
	
	private DatasetFilter[] datasetFilter;
	
	public String total_pages;
	
	public String getTitle() {
		return title;
	}
	
	public DatasetFilter[] getDatasetFilter() {
		return datasetFilter;
	}
	
	public String getTotal_pages() {
		return total_pages;
	}
}
