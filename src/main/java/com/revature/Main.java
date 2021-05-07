package com.revature;

import static io.javalin.apibuilder.ApiBuilder.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.revature.controllers.AuthController;
import com.revature.controllers.EmployeeController;
import com.revature.controllers.ReimbursementController;
import com.revature.controllers.UploadController;

import io.javalin.Javalin;

public class Main {

	/*
	 * ENV Variables Needed: CASS_USER CASS_PASS JWT_SECRET
	 */

	/*
	 * TODO:
	 * - Rename supervisor to supervisorID in Employee to make it clearer
	 */

	public static final int PORT = 8080;
	private static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("spring.xml");

		Javalin app = Javalin.create(conf -> {
			conf.requestLogger((ctx, responseTime) -> {
				logger.debug(ctx.method() + " -> " + ctx.path() + " -> " + responseTime + "ms");
			});
			conf.enableDevLogging();
		}).start(PORT);

		AuthController authController = appCtx.getBean(AuthController.class);
		EmployeeController employeeController = appCtx.getBean(EmployeeController.class);
		ReimbursementController reimburseController = appCtx.getBean(ReimbursementController.class);
		UploadController uploadController = appCtx.getBean(UploadController.class);

		app.routes(() -> {
			path("login", () -> {
				post(authController::login);
			});
			path("employees", () -> {
				post(employeeController::createEmployee);

				before(":id", authController::authenticate);
				path(":id", () -> {
					/*
					 * Javalin will not register this before so I added it one level up
					 */
					// before(authController::authenticate);
					get(employeeController::getEmployeeById);
					put(employeeController::updateEmployee);
				});
			});
			path("reimbursements", () -> {
				before(authController::authenticate);
				post(reimburseController::createReimbursement);
				
				path("assigned", () -> {
					get(reimburseController::getAssigned);
				});

				path(":id", () -> {
					get(reimburseController::getReimbursementById);
					
					put(reimburseController::updateReimbursement);
					path("finalreport", () -> {
						post(uploadController::uploadFinalReport);
					});
				});
			});
			before("uploads", authController::authenticate);
			path("uploads", () -> {
				// another one that's not working
				// before(authController::authenticate);
				post(uploadController::uploadFile);
				get(uploadController::getFilesByUser);
				
				before(":s3Key", authController::authenticate);
				path(":s3Key", () -> {
					delete(uploadController::deleteFile);
				});
			});
		});
		
	}
}
