package com.loserico.xml;

import com.loserico.map.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020/6/24 10:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeMap {
	
	private Map<Integer, Employee> employees = new HashMap<>();
	
	public Map<Integer, Employee> getEmployees() {
		return employees;
	}
	
	public void setEmployees(Map<Integer, Employee> employees) {
		this.employees = employees;
	}
	
	
}
