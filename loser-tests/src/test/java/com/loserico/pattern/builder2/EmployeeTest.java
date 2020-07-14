package com.loserico.pattern.builder2;

public class EmployeeTest {
	public static void main(String[] args) {
		Employee employee = new Employee.EmployeeBuilder("Cristiano", "Ronaldo", 33, 7)
				.setPhone("0045-1234556")
				.setAddress("Juventus")
				.setMail("CR@Juventus.org").build();
	}
}