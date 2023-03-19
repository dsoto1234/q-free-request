package com.conduent.ibts.alpr.process;

public class Cust {
	private int age;
	private String employeeName;
	private String company;
	private String employeeStatus;
		
	public Cust(int age, String employeeName, String company, String employeeStatus) {
		super();
		this.age = age;
		this.employeeName = employeeName;
		this.company = company;
		this.employeeStatus = employeeStatus;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	@Override
	public String toString() {
		return "Cust [age=" + age + ", employeeName=" + employeeName + ", company=" + company + ", employeeStatus="
				+ employeeStatus + "]";
	}
	
	
}
