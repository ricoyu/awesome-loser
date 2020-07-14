package com.loserico.pattern.visitor;

/** 
 * 公司普通员工（具体的被访问者对象） 
 * 
 */
public class GeneralEmployee extends Employee {
	// 员工姓名  
	private String name;
	// 每天上班时长  
	private int timeSheet;
	// 每月工资  
	private double wage;
	// 请假/迟到 惩罚时长  
	private int punishmentTime;

	public GeneralEmployee(String name, int timeSheet, double wage, int punishmentTime) {
		this.name = name;
		this.timeSheet = timeSheet;
		this.wage = wage;
		this.punishmentTime = punishmentTime;
	}

	@Override
	public void accept(Department department) {
		department.visit(this);
	}

	/** 
	 * 获取每月的上班实际时长 = 每天上班时长 * 每月上班天数 - 惩罚时长 
	 * @return 
	 */
	public int getTotalTimeSheet() {
		return timeSheet * 22 - punishmentTime;
	}

	/** 
	 * 获取每月实际应发工资 = 每月固定工资 - 惩罚时长 * 10<br/> 
	 * <作为公司普通员工  每迟到1小时 扣10块钱  坑吧？  哈哈> 
	 *  
	 * @return 
	 */
	public double getTotalWage() {
		return wage - punishmentTime * 10;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWage() {
		return wage;
	}

	public void setWage(double wage) {
		this.wage = wage;
	}

	public int getPunishmentTime() {
		return punishmentTime;
	}

	public void setPunishmentTime(int punishmentTime) {
		this.punishmentTime = punishmentTime;
	}

}