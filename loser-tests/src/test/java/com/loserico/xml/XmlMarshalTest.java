package com.loserico.xml;

import lombok.SneakyThrows;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020/6/24 10:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class XmlMarshalTest {
	
	@SneakyThrows
	@Test
	public void testMap2Xml() {
		Map<Integer, Employee> map = new HashMap<>();
		
		Employee empl1 = new Employee();
		empl1.setId(1);
		empl1.setFirstName("Lokesh");
		empl1.setLastName("Gupta");
		empl1.setIncome(100.0);
		
		Employee empl2 = new Employee();
		empl2.setId(2);
		empl2.setFirstName("John");
		empl2.setLastName("Mclane");
		empl2.setIncome(200.0);
		
		map.put(1, empl1);
		map.put(2, empl2);
		
		//Add employees in map
		EmployeeMap employeeMap = new EmployeeMap();
		employeeMap.setEmployees(map);
		
		/******************** Marshalling example *****************************/
		JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeMap.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(employeeMap, System.out);
		marshaller.marshal(employeeMap, new File("d://employees.xml"));
	}
	
	@SneakyThrows
	@Test
	public void test() {
		JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeMap.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		EmployeeMap empMap = (EmployeeMap) jaxbUnmarshaller.unmarshal(new File("d://employees.xml"));
		
		for (Integer empId : empMap.getEmployees().keySet()) {
			System.out.println(empMap.getEmployees().get(empId).getFirstName());
			System.out.println(empMap.getEmployees().get(empId).getLastName());
		}
	}
}
