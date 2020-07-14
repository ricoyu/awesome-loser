package com.loserico.pattern.visitor;

/** 
 * 具体访问者对象：公司人力资源部<br/> 
 * 人力资源部的职责就是负责统计核算员工的每月上班时长 
 * 
 */
public class HRDepartment extends Department {

	/** 
	 * 访问公司管理者对象的每月实际上班时长统计 
	 */
	@Override
	public void visit(ManagerEmployee manager) {
		System.out.println("管理者" + manager.getName() + "上班时长: " + manager.getTotalTimeSheet());
	}

	/** 
	 * 访问公司普通员工对象的每月实际上班时长统计 
	 */
	@Override
	public void visit(GeneralEmployee employee) {
		System.out.println("普通员工" + employee.getName() + "上班时长: " + employee.getTotalTimeSheet());
	}

}