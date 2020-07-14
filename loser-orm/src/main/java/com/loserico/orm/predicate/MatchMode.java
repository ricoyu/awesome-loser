package com.loserico.orm.predicate;

public enum MatchMode {

	/**
	 * 完全匹配
	 */
	EXACT {
		@Override
		public String toMatchString(String pattern) {
			return pattern;
		}
	},

	/**
	 * 开头匹配
	 */
	START {
		@Override
		public String toMatchString(String pattern) {
			return pattern + '%';
		}
	},
	
	/**
	 * 开头匹配
	 */
	START_IGNORE_CASE {
		@Override
		public String toMatchString(String pattern) {
			return pattern.toUpperCase() + '%';
		}
	},

	/**
	 * 结尾匹配
	 */
	END {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern;
		}
	},

	/**
	 * 结尾匹配,不区分大小写
	 */
	END_IGNORE_CASE {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern.toUpperCase();
		}
	},

	/**
	 * 任意匹配
	 */
	ANYWHERE {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern + '%';
		}
	},
	
	/**
	 * 任意匹配,不区分大小写
	 */
	ANYWHERE_IGNORE_CASE {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern.toUpperCase() + '%';
		}
	};

	/**
	 * Convert the pattern, by appending/prepending "%"
	 *
	 * @param pattern The pattern for convert according to the mode
	 *
	 * @return The converted pattern
	 */
	public abstract String toMatchString(String pattern);

}