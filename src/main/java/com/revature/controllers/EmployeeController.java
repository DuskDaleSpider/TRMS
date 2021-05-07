package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.revature.models.Employee;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService service;
	private static Logger logger = LogManager.getLogger(EmployeeController.class);


	public EmployeeController(EmployeeService service) {
		this.service = service;
	}

	// POST /employees
	public void createEmployee(Context ctx) {
		Employee empl = ctx.bodyAsClass(Employee.class);

		// validation
		if (empl.getName() == null || empl.getEmail() == null || empl.getPassword() == null
				|| empl.getPassword().length() < 8 || empl.getRole() == null || empl.getSupervisor() == 0) {
			logger.debug("Invalid input for creating employee");
			ctx.status(400).result("Invalid input.");
			return;
		}

		// Add employee to database
		if (service.addEmployee(empl)) {
			ctx.json(empl);
		} else {
			throw new InternalServerErrorResponse("Unable to add employee");
		}
	}

	// Authenticated
	// GET /employees/:id
	public void getEmployeeById(Context ctx) {

		int id = 0;
		try {
			id = Integer.parseInt(ctx.pathParam("id"));
		} catch (NumberFormatException e) {
			logger.warn("Inavlid input for id");
			throw new BadRequestResponse("ID must be a number");
		}

		Employee e = service.getEmployee(id);
		if (e == null) {
			logger.warn("No Employee found for id " + id);
			throw new NotFoundResponse();
		}

		ctx.json(e);
	}

	// Authenticated
	// PUT /employees/:id
	// An employee can only modify their own password
	// An employees super can additionally update said employees role and super
	public void updateEmployee(Context ctx) {

		int id = 0;
		Employee emp;
		Employee existing;
		Employee user = ctx.attribute("user");

		try {
			id = Integer.parseInt(ctx.pathParam("id"));
			emp = ctx.bodyAsClass(Employee.class);
		} catch (Exception e) {
			throw new BadRequestResponse();
		}

		// fetch old employee data from service
		existing = service.getEmployee(id);
		if (existing == null) {
			throw new NotFoundResponse();
		}

		// Validate that the employee or the employee's super is making the update
		if (user.getId() != existing.getId() && user.getId() != existing.getSupervisor()) {
			logger.warn("Forbidden: " + user.getName() + " tried to update " + existing.getName());
			throw new ForbiddenResponse();
		}
		
		// check if employee is trying to change their own role or supervisor
		if (user.getId() == existing.getId() && (emp.getRole() != null || emp.getSupervisor() != 0)) {
			logger.warn("Forbidden: " + user.getName() + " tried to update themselves to " + emp.toString());
			throw new ForbiddenResponse();
		}
		
		//update existing emp and save
		if(emp.getRole() != null) existing.setRole(emp.getRole());
		if(emp.getSupervisor() != 0) existing.setSupervisor(emp.getSupervisor());
		if(emp.getPassword() != null) existing.setPassword(emp.getPassword());

		if (service.updateEmployee(existing)) {
			ctx.json(existing);
		} else {
			throw new InternalServerErrorResponse();
		}

	}

}
