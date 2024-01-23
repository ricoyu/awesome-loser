package com.loserico.pattern.builder;

import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import com.google.common.base.Preconditions;

/**
 * https://github.com/davidmoten/java-builder-pattern-tricks
 * https://www.mkyong.com/design-pattern/java-builder-design-pattern-example/
 * https://www.javacodegeeks.com/2015/09/builder-design-pattern.html
 * 
 * The humble java builder pattern has been described frequently but is
 * nearly always described in a very basic form without revealing its
 * true potential! If you push the pattern a bit harder then you get can
 * less verbosity in your API and more compile-time safety.
 * 
 * So what are these extra tricks?
 * <ul>
 * <li/>Format the code better (long method chained lines are yuk!)
 * <li/>Shortcut the builder() method
 * <li/>Enforce mandatory parameters at compile time with builder chaining
 * <li/>Remove final build() call when all fields mandatory
 * <li/>Build generic signatures with builder chaining
 * <li/>Improve discoverability
 * <ul/>
 * <br/>
 * <p>
 * Copyright: Copyright (c) 2018-08-23 14:13
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class BuilderTricks {

	@Test
	public void testBasicUsage() {
		Book book = Book.builder()
				.author("Charles Dickens")
				.title("Great Expectations")
				.category("Novel")
				.build();
		System.out.println(JacksonUtils.toJson(book));
	}
	
	/**
	 * 如果有必填字段，builder()方法可以省略
	 */
	@Test
	public void testTricks2() {
		
	}

	public static final class Book {
		// Make fields final, so we always know we've missed assigning one in the constructor!
		private final String author;
		private final String title;
		private final String category;

		//should not be public
		private Book(Builder builder) {
			//Be a bit defensive
			Preconditions.checkNotNull(builder.author);
			Preconditions.checkArgument(builder.author.trim().length() > 0);
			Preconditions.checkNotNull(builder.title);
			Preconditions.checkArgument(builder.title.trim().length() > 0);
			Preconditions.checkNotNull(builder.category);
			this.author = builder.author;
			this.title = builder.title;
			this.category = builder.category;
		}

		public String author() {
			return author;
		}

		public String title() {
			return title;
		}

		public String category() {
			return category;
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder {
			String author;
			String title;
			String category;

			// should not be public to force use of the static builder() method
			private Builder() {
			}

			public Builder author(String author) {
				this.author = author;
				return this;
			}

			public Builder title(String title) {
				this.title = title;
				return this;
			}

			public Builder category(String category) {
				this.category = category;
				return this;
			}

			public Book build() {
				return new Book(this);
			}
		}
	}
}
