package com.revature.services;

import java.util.List;

import com.revature.models.Employee;

public interface EmployeeService {

	public Employee getEmployee(int id);
	public List<Employee> getAllBenCos();
	public boolean addEmployee(Employee employee);
	public boolean updateEmployee(Employee employee);
	public boolean deleteEmployee(Employee employee);
	
}
