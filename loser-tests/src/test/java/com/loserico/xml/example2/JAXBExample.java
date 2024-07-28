package com.loserico.xml.example2;

import com.loserico.json.jackson.JacksonUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Paths;

/**
 * <p>
 * Copyright: (C), 2020/6/24 14:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JAXBExample {
	
	@SneakyThrows
	@Test
	public void testMarshal() {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("mkyong");
		customer.setAge(29);
		
		File file = Paths.get("D:\\file.xml").toFile();
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		// output pretty printed
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(customer, file);
		marshaller.marshal(customer, System.out);
	}
	
	@SneakyThrows
	@Test
	public void testUnmarshal() {
		File file = Paths.get("D:\\file.xml").toFile();
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Customer customer = (Customer) unmarshaller.unmarshal(file);
		System.out.println(customer);
		System.out.println(JacksonUtils.toJson(customer));
	}
}
