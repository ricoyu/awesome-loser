package com.loserico.jackson;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class JacksonUtilsTest {

	@Test
	public void testToList() {

		String jsonArr = IOUtils.readClassPathFileAsString("nursery.json");
		List<Nursery> nurseries = JacksonUtils.toList(jsonArr, Nursery.class);
		nurseries.forEach(System.out::println);
	}

	static class Nursery {
		private String name;

		private String province;

		private String city;

		private String address;

		private String area;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

	}
	
	@Test
	public void testSerializeNullKey() {
		Map<Object, Object> params = new HashMap<>();
		params.put("one", "hello");
		params.put(1, "asd");
//		params.put(null, "sss");
//		params.put(new int[] {1}, 1);

		String output = toJson(params);
		System.out.println(output);
	}
	
	@Test
	public void testEnumDeserializer() {
		String json = "[{\"selectedCentreId\": 1 ,\"closeOperate\": \"0\"}]";
		JacksonUtils.toList(json, AccountCloseVO.class)
			.forEach((accountClose) -> {
				System.out.println(accountClose.getCloseOperate());
			});;
	}
	
	public static enum CloseOperate {
	    CLOSE,
	    OPEN,
	}
	
	public static class AccountCloseVO {

	    private CloseOperate closeOperate;
	    private Long selectedCentreId;

	    public CloseOperate getCloseOperate(){
	        return this.closeOperate;
	    }

	    public void setCloseOperate(CloseOperate closeOperate){
	        this.closeOperate = closeOperate;
	    }

		public Long getSelectedCentreId() {
			return selectedCentreId;
		}

		public void setSelectedCentreId(Long selectedCentreId) {
			this.selectedCentreId = selectedCentreId;
		}
	}
	
	@Test
	public void testArrayToJson() {
		List<Long> centreIds = asList(3L);
		System.out.println(toJson(centreIds));
	}
	
	@Test
	public void testToMap() {
		//String detail = null;
		String detail = "{\"username\":\"admin\",\"password\":\"123456\",\"srcport\":\"11995\",\"pid\":\"43\"}";
		Map<String, Object> map = JacksonUtils.toMap(detail);
		System.out.println(toJson(map));
		assertEquals(toJson(map), detail);
	}
	
	@Test
	public void testToList2() {
		List<String> list = new ArrayList<>();
		String jsonArr = JacksonUtils.toJson(list);
		System.out.println(jsonArr);
		List<String> list2 = JacksonUtils.toList(jsonArr, String.class);
		assertEquals(0, list2.size());
	}
}
