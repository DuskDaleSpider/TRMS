package com.revature.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.models.Role;

public class EmployeeServiceImplTest {
	
	private EmployeeServiceImpl service;

	@Before
	public void setUp() throws Exception {
		service = new EmployeeServiceImpl(mock(EmployeeDao.class));
	}

	@Test
	public void getEmployeeReturnsEmployeeWithName() {
		Employee test = new Employee();
		test.setId(2);
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("testPassword");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		EmployeeDao dao = mock(EmployeeDao.class);
		service.setEmployeeDao(dao);
		when(dao.getEmployee(test.getId())).thenReturn(test);
		
		Employee employee = service.getEmployee(test.getId());
		assertEquals("Employee should have name " + test.getName(), test.getName(), employee.getName());
	}
	
	@Test
	public void getEmployeeReturnsNull() {
		EmployeeDao dao = mock(EmployeeDao.class);
		service.setEmployeeDao(dao);
		
		when(dao.getEmployee(10000)).thenReturn(null);
		
		Employee employee = service.getEmployee(10000);
		assertEquals("Employee should be null", null, employee);
	}
	
	@Test
	public void addEmployeeSuccessfully() {
		Employee test = new Employee();
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("testPassword");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		EmployeeDao dao = mock(EmployeeDao.class);
		service.setEmployeeDao(dao);
		
		assertTrue("Add employee should return true", service.addEmployee(test));
	}
	
	@Test
	public void updateEmployeeSuccessfully() {
		Employee test = new Employee();
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("newPassword");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		EmployeeDao dao = mock(EmployeeDao.class);
		service.setEmployeeDao(dao);
		
		assertTrue("Update employee should return true", service.updateEmployee(test));
	}
	
	@Test
	public void deleteEmployeeSuccessfully() {
		Employee test = new Employee();
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("newPassword");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		EmployeeDao dao = mock(EmployeeDao.class);
		service.setEmployeeDao(dao);
		
		assertTrue("Delete employee should return true", service.deleteEmployee(test));
	}

}
