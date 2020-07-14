package com.loserico.common.lang.enums;

/**
 * 性别
 * <p>
 * Copyright: Copyright (c) 2019-03-04 21:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public enum Gender {

	FEMALE {

		@Override
		public String desc() {
			return "女";
		}

	},
	MALE {

		@Override
		public String desc() {
			return "男";
		}

	},
	UNKNOWN {

		@Override
		public String desc() {
			return "未知";
		}

	};

	public abstract String desc();
}