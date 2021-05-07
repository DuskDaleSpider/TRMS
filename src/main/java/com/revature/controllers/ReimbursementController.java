package com.revature.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.revature.models.Employee;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.ReimbursementService;
import com.revature.services.ReimbursementServiceImpl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;

@Controller
public class ReimbursementController {

	private Logger logger = LogManager.getLogger(ReimbursementController.class);
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private EmployeeService employeeSerivce;
	public static final double YEAR_CAP = 1000.0;

	// Authenticated
	// GET /reimbursements/:id
	public void getReimbursementById(Context ctx) {
		logger.trace("Getting reimbursement by id");
		int id = Integer.parseInt(ctx.pathParam("id"));
		Reimbursement result = reimbursementService.getReimbursement(id);
		if (result == null) {
			logger.warn("Unable to find Reimbursement with ID " + id);
			throw new NotFoundResponse();
		}
		ctx.json(result);
	}

	// Authenticated
	// POST /reimbursements
	public void createReimbursement(Context ctx) {
		Reimbursement form;
		Employee user = ctx.attribute("user");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime weekFromNow = now.plusWeeks(1);
		LocalDateTime twoWeeksFromNow = now.plusWeeks(2);

		// Parse json input
		try {
			form = ctx.bodyAsClass(Reimbursement.class);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			for (StackTraceElement el : e.getStackTrace()) {
				logger.debug(el);
			}
			throw new BadRequestResponse();
		}

		// validate
		if (form.getEventDate() == null || form.getEventLocation() == null || form.getEventDescription() == null
				|| form.getEventType() == null || form.getEventCost() == 0 || form.getEventGradingFormat() == null
				|| form.getJustification() == null) {
			logger.warn("Missing required fields for reimbursements");
			throw new BadRequestResponse("Missing required fields");
		}
		// Make sure event date is > 1 Week
		if (form.getEventDate().isBefore(weekFromNow)) {
			logger.warn("Reimbursement form submitted has event that's in less than a week");
			throw new BadRequestResponse("Must submit the application at least a week before the event");
		}

		form.setEmployee(user.getId());
		form.setDateSubmitted(now);
		form.setDateUpdated(now);
		form.setProofOfPassing("");
		form.setMessage("");
		form.setExceededEmployeesAvailableFunds(false);
		form.setFinalReport("");

		if (form.getPassingGrade() == 0) {
			form.setPassingGrade(form.getEventGradingFormat().getPassPercentage());
		}

		// true if 1 week < event date < 2 weeks
		form.setUrgent(form.getEventDate().isAfter(weekFromNow) && form.getEventDate().isBefore(twoWeeksFromNow));

		// check for preApproval
		if (form.getPreApprovalEmail() != null && !form.getPreApprovalEmail().trim().isEmpty()) {
			if (form.getPreApprovalType() == Status.SUPER_APPROVED) {
				form.setStatus(form.getPreApprovalType());

				// fetch the super's info to get the dept head
				Employee supervisor = employeeSerivce.getEmployee(user.getSupervisor());
				form.setAssignedTo(supervisor.getSupervisor());
			} else if (form.getPreApprovalType() == Status.DEPT_HEAD_APPROVED) {
				form.setStatus(form.getPreApprovalType());

				// Check if user that requested the reimbursement is a ben co
				// So we can make sure we don't assign it
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
						.filter(benco -> benco.getId() != user.getId()).collect(Collectors.toList());
				Collections.shuffle(bencos);
				form.setAssignedTo(bencos.get(0).getId());

			} else {
				form.setStatus(Status.SUBMITTED);
				form.setAssignedTo(user.getSupervisor());
			}

		} else {
			System.out.println("==================");
			form.setStatus(Status.SUBMITTED);
			form.setAssignedTo(user.getSupervisor());
		}

		List<Reimbursement> appsThisYear = reimbursementService.getReimbursementsByUserAndYear(user.getId(),
				now.getYear());
		double totalForYear = appsThisYear.stream()
				.map(app -> app.getAwardedAmount() == 0 ? app.getProjectedAmount() : app.getAwardedAmount())
				.reduce(0.0, (total, amount) -> total + amount);

		double projectedAmount = form.getEventCost() * form.getEventType().getPercent();
		if (totalForYear + projectedAmount > YEAR_CAP) {
			projectedAmount = YEAR_CAP - totalForYear;
		}
		if (projectedAmount == 0) {
			logger.warn("User " + user.getId() + " does not have any available balance");
			throw new ConflictResponse("You don't have any available balance for reimbursements");
		}
		form.setProjectedAmount(projectedAmount);

		if (reimbursementService.addReimbursement(form)) {
			ctx.json(form);
		} else {
			logger.warn("Unable to add reimbursement");
			throw new InternalServerErrorResponse();
		}
	}

	// Authenticated
	// PUT /reimbursements/:id
	// TODO: Refactor this mess you created cause of rushing to finish
	public void updateReimbursement(Context ctx) {
		int id;
		Reimbursement input;
		Employee user = ctx.attribute("user");

		try {
			id = Integer.parseInt(ctx.pathParam("id"));
			input = ctx.bodyAsClass(Reimbursement.class);
		} catch (Exception e) {
			logger.warn("Unable to parse path param id to number");
			throw new BadRequestResponse();
		}

		Reimbursement reimbursement = reimbursementService.getReimbursement(id);
		if (reimbursement == null) {
			logger.warn("Unable to find reimbursement of id " + id);
			throw new NotFoundResponse();
		}

		// make sure the user updating the form is the one the form is assigned to
		if (reimbursement.getAssignedTo() != user.getId()) {
			logger.warn("User " + user.getId() + " tried to update reimbursement " + reimbursement.getId()
					+ " without permission");
			throw new ForbiddenResponse();
		}

		// validate
		if (input.getStatus() == null && user.getRole() != Role.ASSOCIATE) {
			logger.warn("Request is missing required fields");
			throw new BadRequestResponse();
		}

		// update
		switch (user.getRole()) {
		case ASSOCIATE:
			if(input.getEventDescription() != null && !input.getEventDescription().trim().isEmpty()) {
				reimbursement.setEventDescription(input.getEventDescription());
			}
			if(input.getEventAttachments() != null && input.getEventAttachments().size() > 0) {
				reimbursement.setEventAttachments(input.getEventAttachments());
			}
			
			switch(reimbursement.getStatus()) {
			case SUPER_NEEDS_INFO:
				reimbursement.setAssignedTo(user.getSupervisor());
				break;
			case DHEAD_NEEDS_INFO:
				Employee supervisor = employeeSerivce.getEmployee(user.getSupervisor());
				reimbursement.setAssignedTo(supervisor.getSupervisor());
				break;
			case BENCO_NEEDS_INFO:
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
				.filter(emp -> emp.getId() != reimbursement.getEmployee()).collect(Collectors.toList());
				Collections.shuffle(bencos);
				reimbursement.setAssignedTo(bencos.get(0).getId());
				break;
			default:
			}
			
			break;
		case SUPERVISOR:
			reimbursement.setStatus(input.getStatus());
			if (input.getStatus() == Status.SUPER_APPROVED) {
				reimbursement.setAssignedTo(user.getSupervisor()); // Assign to Department Head
			} else if (input.getStatus() == Status.SUPER_NEEDS_INFO) {
				reimbursement.setAssignedTo(reimbursement.getEmployee()); // reassign back to employee
				reimbursement.setMessage(input.getMessage());
			} else if (input.getStatus() == Status.REJECTED) {
				if (input.getMessage() == null || input.getMessage().trim().isEmpty()) {
					logger.warn("Missing required field message");
					throw new BadRequestResponse("Must include a message if application is rejected");
				}
			}else if(input.getStatus() == Status.DHEAD_NEEDS_INFO) {
				if(input.getEventDescription() != null && !input.getEventDescription().trim().isEmpty()) {
					reimbursement.setEventDescription(input.getEventDescription());
				}
				if(input.getEventAttachments() != null && input.getEventAttachments().size() > 0) {
					reimbursement.setEventAttachments(input.getEventAttachments());
				}
				reimbursement.setAssignedTo(user.getSupervisor());
			}else if(input.getStatus() == Status.BENCO_NEEDS_INFO) {
				if(input.getEventDescription() != null && !input.getEventDescription().trim().isEmpty()) {
					reimbursement.setEventDescription(input.getEventDescription());
				}
				if(input.getEventAttachments() != null && input.getEventAttachments().size() > 0) {
					reimbursement.setEventAttachments(input.getEventAttachments());
				}
				
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
						.filter(benco -> benco.getId() != user.getId()).collect(Collectors.toList());
				Collections.shuffle(bencos);
				reimbursement.setAssignedTo(bencos.get(0).getId());
			} else if (input.getStatus() == Status.AWARDED) {
				// notify requester of award
			}

			break;
		case DEPT_HEAD:
		case SUPER_DEPTHEAD:

			reimbursement.setStatus(input.getStatus());
			if (input.getStatus() == Status.DEPT_HEAD_APPROVED) {
				// filter out the requested employee from bencos in case the requester was a
				// benco
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
						.filter(emp -> emp.getId() != reimbursement.getEmployee()).collect(Collectors.toList());
				Collections.shuffle(bencos);
				reimbursement.setAssignedTo(bencos.get(0).getId());
			} else if (input.getStatus() == Status.DHEAD_NEEDS_INFO) {
				if (input.getAssignedTo() == 0) {
					logger.warn("Mising required field: assignedTo");
					throw new BadRequestResponse("Must include assignedTo field with the value of the employee");
				}

				reimbursement.setAssignedTo(input.getAssignedTo());
				reimbursement.setMessage(input.getMessage());
			} else if(input.getStatus() == Status.BENCO_NEEDS_INFO) {
				if(input.getEventDescription() != null && !input.getEventDescription().trim().isEmpty()) {
					reimbursement.setEventDescription(input.getEventDescription());
				}
				if(input.getEventAttachments() != null && input.getEventAttachments().size() > 0) {
					reimbursement.setEventAttachments(input.getEventAttachments());
				}
				
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
						.filter(benco -> benco.getId() != user.getId()).collect(Collectors.toList());
				Collections.shuffle(bencos);
			} else if (input.getStatus() == Status.REJECTED) {
				if (input.getMessage() == null || input.getMessage().trim().isEmpty()) {
					logger.warn("Missing required field message");
					throw new BadRequestResponse("Must include a message if application is rejected");
				}
			}

			break;
		case CEO:
			reimbursement.setStatus(input.getStatus());
			if (input.getStatus() == Status.DEPT_HEAD_APPROVED) {
				List<Employee> bencos = employeeSerivce.getAllBenCos().stream()
						.filter(emp -> emp.getId() != reimbursement.getEmployee()).collect(Collectors.toList());
				Collections.shuffle(bencos);
				reimbursement.setAssignedTo(bencos.get(0).getId());
			} else if (input.getStatus() == Status.REJECTED) {
				if (input.getMessage() == null || input.getMessage().trim().isEmpty()) {
					logger.warn("Missing required field message");
					throw new BadRequestResponse("Must include a message if application is rejected");
				}
			}
		case BENCO:
			reimbursement.setStatus(input.getStatus());
			if (input.getStatus() == Status.PENDING) {
				if (input.getAwardedAmount() == 0) {
					logger.warn("Missing awarded ammount");
					throw new BadRequestResponse("Must include final award amount (awardedAmount)");
				}

				// check if the awarded amount is greater than requesters
				// available balance
				List<Reimbursement> appsThisYear = reimbursementService.getReimbursementsByUserAndYear(user.getId(),
						LocalDateTime.now().getYear());
				double totalForYear = appsThisYear.stream()
						.map(app -> app.getAwardedAmount() == 0 ? app.getProjectedAmount() : app.getAwardedAmount())
						.reduce(0.0, (total, amount) -> total + amount);

				reimbursement.setExceededEmployeesAvailableFunds(totalForYear + input.getAwardedAmount() > YEAR_CAP);

				if (reimbursement.getExceededEmployeesAvailableFunds()) {
					if (input.getMessage() == null || input.getMessage().trim().isEmpty()) {
						logger.warn("Missing required field message");
						throw new BadRequestResponse("Need to include message if awarded"
								+ " ammount suprasses employees available balance.");
					}
					reimbursement.setMessage(input.getMessage());

				}

				reimbursement.setAwardedAmount(input.getAwardedAmount());
				// reassign to the requester and wait for grade/presentation upload
				reimbursement.setAssignedTo(reimbursement.getEmployee());
			} else if (input.getStatus() == Status.AWARDED) {
				// notify requester of award
			} else if (input.getStatus() == Status.BENCO_NEEDS_INFO) {
				if (input.getAssignedTo() == 0) {
					logger.warn("Mising required field: assignedTo");
					throw new BadRequestResponse("Must include assignedTo field with the value of the employee");
				}
				reimbursement.setAssignedTo(input.getAssignedTo());
			} else if (input.getStatus() == Status.REJECTED) {
				if (input.getMessage() == null || input.getMessage().trim().isEmpty()) {
					logger.warn("Missing required field message");
					throw new BadRequestResponse("Must include a message if application is rejected");
				}
			}
			break;

		default:
			break;
		}

		// save
		if (reimbursementService.updateReimbursement(reimbursement)) {
			ctx.json(reimbursement);
		} else {
			logger.warn("Unable to update reimbursement");
			throw new InternalServerErrorResponse();
		}

	}
	
	//authenticated
	//get /reimbursements/assigned
	public void getAssigned(Context ctx) {
		logger.trace("Getting assigned reimbursemetns for user");
		
		Employee user = ctx.attribute("user");
		List<Reimbursement> assigned = reimbursementService.getAssignedReimbursements(user.getId());
		ctx.json(assigned);
	}
}
