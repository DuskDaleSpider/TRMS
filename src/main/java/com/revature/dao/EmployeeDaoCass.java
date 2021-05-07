package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.revature.models.Employee;
import com.revature.models.Role;
import com.revature.utils.CassandraUtil;

@Component
public class EmployeeDaoCass implements EmployeeDao {

	@Autowired
	private CqlSession session;
	
//	public EmployeeDaoCass() {
//		this.session = CassandraUtil.getSession();
//	}
	
	public EmployeeDaoCass(CqlSession session) {
		this.session = session;
	}
	
	private Employee buildEmployee(Row row) {
		Employee employee = new Employee();
		employee.setId(row.getInt("id"));
		employee.setName(row.getString("name"));
		employee.setEmail(row.getString("email"));
		employee.setPassword(row.getString("password"));
		employee.setSupervisor(row.getInt("supervisor"));
		employee.setRole(Role.valueOf(row.getString("role")));
		return employee;
	}
	
	private int generateId() {
		String query = "select * from employee";
		ResultSet results = session.execute(query);
		return results.all().size() + 1;
	}
	
	@Override
	public List<Employee> getEmployees() {
		String query = "select * from employee";
		List<Employee> employees = new ArrayList<>();
		ResultSet results = session.execute(query);
		for(Row row : results) {
			employees.add(buildEmployee(row));
		}
		
		return employees;
	}
	
	
	
	@Override
	public Employee getEmployee(int id) {
		String baseQuery = "select * from employee where id=?";
		PreparedStatement prepared = session.prepare(baseQuery);
		BoundStatement bound = prepared.bind(id);
		ResultSet results = session.execute(bound);
		Row row = results.one();
		if(row != null) {
			return buildEmployee(row);
		}
		return null;
	}

	@Override
	public void addEmployee(Employee employee) {
		employee.setId(generateId());	
		String query = "insert into employee (id, name, email, password, role, supervisor) "
				+ "values (?, ?, ?, ?, ?, ?)";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(employee.getId(), employee.getName(), employee.getEmail(),
				employee.getPassword(), employee.getRole().toString(), employee.getSupervisor());
		session.execute(bound);
	}

	@Override
	public void updateEmployee(Employee employee) {
		String query = "update employee "
				+ "set password=?, role=?, supervisor=? "
				+ "where id=? and name=? and email=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(employee.getPassword(), employee.getRole().toString(), employee.getSupervisor(),
				employee.getId(), employee.getName(), employee.getEmail());
		session.execute(bound);
	}
	
	public void setSession(CqlSession session) {
		this.session = session;
	}

}
