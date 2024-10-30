package com.loserico.general;

import org.junit.Test;

public class TextBlockTest {

	@Test
	public void test() {
		var works = """
				{
				  "description": "A blog pipeline",
				  "processors": [
				    {
				      "split": {
				        "field": "tags",
				        "separator": ","
				      }
				    },
				    {
				      "set": {
				        "field": "views",
				        "value": 0
				      }
				    }
				  ]
				}
				""";

		System.out.println(works);
	}
}
