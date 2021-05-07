package com.revature.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.dao.EmployeeDao;
import com.revature.dao.EmployeeDaoCass;
import com.revature.dao.ReimbursementDao;
import com.revature.models.Employee;
import com.revature.models.Role;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
//	public EmployeeServiceImpl() {
//		employeeDao = new EmployeeDaoCass();
//	}
	
	public EmployeeServiceImpl(EmployeeDao dao) {
		this.employeeDao = dao;
	}
	
	@Override
	public List<Employee> getAllBenCos() {
		List<Employee> employees = employeeDao.getEmployees();
		return employees.stream()
				.filter(emp -> emp.getRole() == Role.BENCO)
				.collect(Collectors.toList());
	}
	
	@Override
	public Employee getEmployee(int id) {
		Employee e = null;
		try {
			e = employeeDao.getEmployee(id);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return e;
	}

	@Override
	public boolean addEmployee(Employee employee) {
		try {
			employeeDao.addEmployee(employee);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateEmployee(Employee employee) {
		try {
			employeeDao.updateEmployee(employee);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteEmployee(Employee employee) {
		try {
			employee.setRole(Role.TERMINATED);
			employeeDao.updateEmployee(employee);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setEmployeeDao(EmployeeDao dao) {
		this.employeeDao = dao;
	}

}
