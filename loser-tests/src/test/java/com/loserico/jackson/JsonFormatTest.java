package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import java.time.LocalDateTime;

public class JsonFormatTest {

	public class InvoiceDetail {
		private LocalDateTime birthday;

		@JsonFormat(shape=Shape.STRING, pattern="dd-MM-yyyy")
		public LocalDateTime getBirthday() {
			return birthday;
		}

		public void setBirthday(LocalDateTime birthday) {
			this.birthday = birthday;
		}
	}
	
	@Test
	public void test1() {
		InvoiceDetail invoiceDetail = new InvoiceDetail();
		invoiceDetail.setBirthday(LocalDateTime.now());
		System.out.println(JacksonUtils.toJson(invoiceDetail));
	}
	
	@Test
	public void test2() {
		InvoiceDetail invoiceDetail = JacksonUtils.toObject("{\"birthday\":\"01-11-2017\"}", InvoiceDetail.class);
		System.out.println(invoiceDetail.getBirthday());
	}
}
