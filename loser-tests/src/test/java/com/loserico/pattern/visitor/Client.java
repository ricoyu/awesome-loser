package com.loserico.pattern.visitor;

import java.util.ArrayList;
import java.util.List;

public class Client {

	public static void main(String[] args) {
		List<Employee> employeeList = new ArrayList<Employee>();
		employeeList.add(new ManagerEmployee("王总", 8, 20000, 10));
		employeeList.add(new ManagerEmployee("谢经理", 8, 15000, 15));
		employeeList.add(new GeneralEmployee("小杰", 8, 8000, 8));
		employeeList.add(new GeneralEmployee("小晓", 8, 8500, 12));
		employeeList.add(new GeneralEmployee("小虎", 8, 7500, 0));

		// 财务部 对公司员工的工资核算/访问  
		Department department = new FADepartment();
		for (Employee employee : employeeList) {
			employee.accept(department);
		}

		department = new HRDepartment();
		for (Employee employee : employeeList) {
			employee.accept(department);
		}
	}
}