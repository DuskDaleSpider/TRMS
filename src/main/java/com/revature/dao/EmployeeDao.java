package com.revature.dao;

import java.util.List;

import com.revature.models.Employee;

public interface EmployeeDao {
	
	public Employee getEmployee(int id);
	
	public void addEmployee(Employee employee);
	
	public void updateEmployee(Employee employee);

	public List<Employee> getEmployees();
}
