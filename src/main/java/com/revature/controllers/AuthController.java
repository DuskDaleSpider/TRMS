package com.revature.controllers;

import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Employee;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Controller
public class AuthController {

	private static Logger logger = LogManager.getLogger(AuthController.class);
	
	public static final int JWT_LIFETIME = 10 * 60 * 1000; // 10 minutes

	@Autowired
	private EmployeeService empService;

//	public AuthController() {
//		this.empService = new EmployeeServiceImpl();
//	}

	public void login(Context ctx) {
		Employee body;
		
		try {
			body = ctx.bodyAsClass(Employee.class);
		}catch(Exception e) {
			throw new BadRequestResponse();
		}
		
		if (body.getId() == 0 || body.getPassword() == null) {
			logger.debug("Bad log in request");
			throw new BadRequestResponse();
		}

		Employee employee = empService.getEmployee(body.getId());
		if (employee == null || !employee.getPassword().equals(body.getPassword())) {
			logger.debug("Unauthorized Log in request");
			throw new UnauthorizedResponse();
		}

		// Caller should be logged in
		// Generate a signed JWT and send it
		String secret = System.getenv("JWT_SECRET");
		if (secret == null) {
			logger.warn("The JWT_SECRET environment variable needs to be set");
			throw new InternalServerErrorResponse("Environment variables need to be set");
		}

		try {
			SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

			ObjectMapper mapper = new ObjectMapper();
			String userJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee);
			Date now = new Date();

			String jws = Jwts.builder().setSubject(employee.getName()).setIssuedAt(now).claim("user", userJson)
					.setExpiration(new Date(now.getTime() + (JWT_LIFETIME)))
					.signWith(key).compact();

			HashMap<String, String> map = new HashMap<>();
			map.put("token", jws);

			ctx.json(map);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			for (StackTraceElement st : e.getStackTrace())
				logger.debug(st.toString());
			throw new UnauthorizedResponse();
		}
	}

	public void authenticate(Context ctx) {
		String authHeader = ctx.header("Authorization");
		if(authHeader == null || authHeader == "") {
			logger.warn("No auth token in request");
			throw new UnauthorizedResponse();
		}
		String jwt = authHeader.split(" ")[1]; // extract the token

		String secret = System.getenv("JWT_SECRET");
		if (secret == null) {
			logger.warn("The JWT_SECRET environment variable needs to be set");
			throw new InternalServerErrorResponse("Environment variables need to be set");
		}

		try {
			Jws<Claims> jwtClaims = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(jwt);
			Employee user = new ObjectMapper().readValue(jwtClaims.getBody().get("user").toString(), Employee.class);
			ctx.attribute("user", user);
		} catch (SignatureException e) {
			logger.warn(e.getMessage());
			throw new UnauthorizedResponse();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new UnauthorizedResponse();
		}
	}
}
