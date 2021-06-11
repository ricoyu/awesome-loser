package com.loserico.search;

import com.loserico.search.pojo.Product;
import org.junit.Test;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-09 15:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GetQueryTest {
	
	@Test
	public void testGetById() {
		Product product = ElasticUtils.Query.byId("products2", "aPbE4HkBHVbtFXGM3D0Z", Product.class);
		assertThat(product).isNotNull();
		assertEquals(product.getId(), "aPbE4HkBHVbtFXGM3D0Z");
		System.out.println(toPrettyJson(product));
	}
}
