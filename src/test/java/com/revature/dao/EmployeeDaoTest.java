package com.revature.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.revature.models.Employee;
import com.revature.models.Role;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeDaoTest {

	private EmployeeDaoCass employeeDao;
	
	@Before
	public void setupDao() {
		CqlSession session = mock(CqlSession.class);
		this.employeeDao = new EmployeeDaoCass(session);
	}
	
	@Test
	public void getEmployeeReturnsEmployeeWithName() {
		Employee testEmployee = new Employee();
		testEmployee.setId(1);
		testEmployee.setName("Dakota");
		testEmployee.setEmail("dakota.clark@revature.net");
		testEmployee.setPassword("password");
		testEmployee.setSupervisor(1);
		testEmployee.setRole(Role.SUPER_DEPTHEAD);
		
		
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		ResultSet results = mock(ResultSet.class);
		Row row = mock(Row.class);
		
		employeeDao.setSession(session);
		
		when(session.prepare("select * from employee where id=?")).thenReturn(prepared);
		when(prepared.bind(testEmployee.getId())).thenReturn(bound);
		when(session.execute(bound)).thenReturn(results);
		when(results.one()).thenReturn(row);
		
		when(row.getInt("id")).thenReturn(testEmployee.getId());
		when(row.getString("name")).thenReturn(testEmployee.getName());
		when(row.getString("email")).thenReturn(testEmployee.getEmail());
		when(row.getString("password")).thenReturn(testEmployee.getPassword());
		when(row.getInt("supervisor")).thenReturn(testEmployee.getSupervisor());
		when(row.getString("role")).thenReturn(testEmployee.getRole().toString());
		
		Employee employee = employeeDao.getEmployee(testEmployee.getId());
		assertEquals("Employee should have the name " + testEmployee.getName(), testEmployee.getName(), employee.getName());
	}
	
	@Test
	public void getEmployeeRetunsNull() {
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		ResultSet results = mock(ResultSet.class);
		
		employeeDao.setSession(session);
		
		when(session.prepare("select * from employee where id=?")).thenReturn(prepared);
		when(prepared.bind(100)).thenReturn(bound);
		when(session.execute(bound)).thenReturn(results);
		when(results.one()).thenReturn(null);
		
		Employee employee = employeeDao.getEmployee(100);
		
		assertEquals("Employee should be null", null, employee);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void addEmployeeSuccessfully() {
		Employee test = new Employee();
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("test");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		ResultSet results = mock(ResultSet.class);
		List<?> rows = mock(ArrayList.class);
		employeeDao.setSession(session);
		
		//for generate ID (Will generate an ID of 2)
		when(session.execute("select * from employee")).thenReturn(results);
		when(results.all()).thenReturn((List<Row>) rows);
		when(rows.size()).thenReturn(1);
		
		//executing insert query
		when(session.prepare("insert into employee (id, name, email, password, role, supervisor) "
				+ "values (?, ?, ?, ?, ?, ?)")).thenReturn(prepared);
		when(prepared.bind(2, test.getName(), test.getEmail(),
				test.getPassword(), test.getRole().toString(), test.getSupervisor()))
				.thenReturn(bound);
		when(session.execute(bound)).thenReturn(results);		
		
		//verify query was executed
		employeeDao.addEmployee(test);
		verify(session).execute(bound);
	}
	
	@Test
	public void updateEmployeeSuccessfully() {
		Employee test = new Employee();
		test.setId(2);
		test.setName("Test");
		test.setEmail("test@gmail.com");
		test.setPassword("test");
		test.setRole(Role.ASSOCIATE);
		test.setSupervisor(1);
		
		Employee update = new Employee();
		update.setId(test.getId());
		update.setName(test.getName());
		update.setEmail(test.getEmail());
		update.setPassword("updatedPassword");
		update.setRole(test.getRole());
		update.setSupervisor(test.getSupervisor());
		
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		employeeDao.setSession(session);
		
		when(session.prepare("update employee "
				+ "set password=?, role=?, supervisor=? "
				+ "where id=? and name=? and email=?")).thenReturn(prepared);
		when(prepared.bind(update.getPassword(), update.getRole().toString(), update.getSupervisor(), 
				update.getId(), update.getName(), update.getEmail())).thenReturn(bound);
				
		employeeDao.updateEmployee(update);
		verify(session).execute(bound);
		
	}

//	@Test
//	public void deleteEmployeeSuccessfully() {
//		Employee test = new Employee();
//		test.setId(2);
//		test.setName("Test");
//		test.setEmail("test@gmail.com");
//		test.setPassword("test");
//		test.setRole(Role.ASSOCIATE);
//		test.setSupervisor(1);
//		
//		CqlSession session = mock(CqlSession.class);
//		PreparedStatement prepared = mock(PreparedStatement.class);
//		BoundStatement bound = mock(BoundStatement.class);
//		employeeDao.setSession(session);
//		
//		when(session.prepare("delete from employee where id=?")).thenReturn(prepared);
//		when(prepared.bind(test.getId())).thenReturn(bound);
//		
//		employeeDao.deleteEmployee(test);
//		verify(session).execute(bound);
//	}
}