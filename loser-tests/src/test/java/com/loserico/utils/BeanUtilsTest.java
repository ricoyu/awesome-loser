package com.loserico.utils;

import com.loserico.common.lang.utils.BeanUtils;
import org.junit.Test;

import java.time.LocalDateTime;

public class BeanUtilsTest {

	class User {
		private String name;
		private int age;
		private Long count;
		private LocalDateTime birthday;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Long getCount() {
			return count;
		}

		public void setCount(Long count) {
			this.count = count;
		}

		public LocalDateTime getBirthday() {
			return birthday;
		}

		public void setBirthday(LocalDateTime birthday) {
			this.birthday = birthday;
		}
	}

	@Test
	public void testCopyProperties() {
		User source = new User();
		source.setAge(12);
		source.setCount(Long.valueOf(34));
		source.setName("rico");
		User target = new User();
		BeanUtils.copyProperties(source, target);
	}
	
	@Test
	public void testCopyPropertiesIgnoreNull() {
		User source = new User();
		source.setAge(12);
//		source.setCount(Long.valueOf(34));
//		source.setName("rico");
		User target = new User();
		target.setAge(123);
		target.setCount(Long.valueOf(56));
		target.setName("yu");
		target.setBirthday(LocalDateTime.now());
//		BeanUtils.copyProperties(source, target);
//		BeanUtils.copyProperties(source, target, true);
		BeanUtils.copyProperties(source, target, "age", "birthday");
//		copyProperties(source, target, ignoreNull)
	}
}
