package com.revature.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.revature.models.Employee;
import com.revature.models.GradingFormat;
import com.revature.models.Reimbursement;
import com.revature.models.Upload;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.ReimbursementService;
import com.revature.services.ReimbursementServiceImpl;
import com.revature.services.UploadService;
import com.revature.services.UploadServiceImpl;
import com.revature.utils.S3Util;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.core.sync.RequestBody;

@Controller
public class UploadController {
	
	@Autowired
	private UploadService uploadService;
	@Autowired
	private ReimbursementService rmbsmtService;
	@Autowired
	private EmployeeService empService;
	
	private static Logger logger = LogManager.getLogger(UploadController.class);
	
//	public UploadController() {
//		this.uploadService = new UploadServiceImpl();
//		this.rmbsmtService = new ReimbursementServiceImpl();
//		this.empService = new EmployeeServiceImpl();
//	}
	
	// authenticated
	// POST /uploads 
	public void uploadFile(Context ctx){
		List<Upload> uploads = new ArrayList<>();
		Employee user = ctx.attribute("user");
		Random r = new Random();
		
		//loop through and validate it's one of the correct extensions
		for(UploadedFile file : ctx.uploadedFiles()) {
			if(!file.getExtension().equals(".msg") 
					&& !file.getExtension().equals(".pdf")
					&& !file.getExtension().equals(".jpg") && !file.getExtension().equals(".jpeg")
					&& !file.getExtension().equals(".png")
					&& !file.getExtension().equals(".docx")
					&& !file.getExtension().equals(".txt")) {
				throw new BadRequestResponse(file.getFilename() + ": " + file.getExtension() + " is not one of the accepted formats (.msg, .pdf, .jpg, .png, .docx, .txt)");
			}
		}
		
		for(UploadedFile file : ctx.uploadedFiles()) {	
			
			//generate a key and upload to s3
			String key = user.getId() + "." + r.nextInt(Integer.MAX_VALUE) + file.getFilename();
			
			S3Util.getInstance().uploadToBucket(key, 
					RequestBody.fromInputStream(file.getContent(), file.getSize()));
			String url = S3Util.getInstance().getObjectUrl(key);
			
			//create upload object, add it to db, and add it to list
			Upload upload = new Upload();
			upload.setEmployeeID(user.getId());
			upload.setS3Key(key);
			upload.setFileURL(url);
			
			uploadService.addUpload(upload);
			
			uploads.add(upload);
		}
		
		ctx.json(uploads);
	}
	
	
	// authenticated
	// POST /reimbursements/:id/finalreport
	public void uploadFinalReport(Context ctx) {
		Employee user = ctx.attribute("user");
		UploadedFile file = ctx.uploadedFile("file");
		Random r = new Random();
		int id;
		
		try {
			id = Integer.parseInt(ctx.pathParam("id"));
		}catch(Exception e) {
			logger.warn("Invalid id for reimbursement");
			throw new BadRequestResponse("Invalid id for reimbursement");
		}
		
		Reimbursement reimbursement = rmbsmtService.getReimbursement(id);
		if(reimbursement == null) {
			logger.warn("Did not find reimbursement id " + id + ".");
			throw new NotFoundResponse();
		}
		
		if(user.getId() != reimbursement.getAssignedTo()) {
			logger.warn("Forbidden: User " + user.getId() + " tried to upload final report on reimbursement " + reimbursement.getId());
			throw new ForbiddenResponse();
		}
		
		if(file == null) {
			logger.warn("No file was found for final upload");
			throw new BadRequestResponse("Must include a file field");
		}
		
		if(!file.getExtension().equals(".pdf")
				&& !file.getExtension().equals(".jpg") && !file.getExtension().equals(".jpeg")
				&& !file.getExtension().equals(".png")
				&& !file.getExtension().equals(".docx")
				&& !file.getExtension().equals(".pptx")
				&& !file.getExtension().equals(".txt")) {
			throw new BadRequestResponse(file.getFilename() + ": " + file.getExtension() + " is not one of the accepted formats (.pptx, .pdf, .jpg, .png, .docx, .txt)");
		}
		
		String key = user.getId() + "." + r.nextInt(Integer.MAX_VALUE) + file.getFilename();
		
		S3Util.getInstance().uploadToBucket(key, RequestBody.fromInputStream(file.getContent(), file.getSize()));
		String url = S3Util.getInstance().getObjectUrl(key);
		
		Upload upload = new Upload();
		upload.setEmployeeID(user.getId());
		upload.setS3Key(key);
		upload.setFileURL(url);
		
		if(!uploadService.addUpload(upload)) {
			throw new InternalServerErrorResponse();
		}
		
		
		//update reimbursement 
		
		reimbursement.setFinalReport(url);
		if(reimbursement.getEventGradingFormat() == GradingFormat.PRESENTATION){
			reimbursement.setAssignedTo(user.getSupervisor());
		}else {
			List<Employee> benCos = empService.getAllBenCos().stream()
					.filter(emp -> emp.getId() != user.getId())
					.collect(Collectors.toList());
			Collections.shuffle(benCos);
			reimbursement.setAssignedTo(benCos.get(0).getId());
		}
		
		if(rmbsmtService.updateReimbursement(reimbursement)) {
			ctx.json(reimbursement);
		}else {
			throw new InternalServerErrorResponse();
		}
		
	}
	
	// authenticated
	// GET /uploads
	public void getFilesByUser(Context ctx) {
		Employee user = ctx.attribute("user");
		List<Upload> files = uploadService.getUploads(user);
		ctx.json(files);
		
	}

	// authenticated
	// DELETE /uploads/:id
	public void deleteFile(Context ctx) {
		Employee user = ctx.attribute("user");
		String s3Key = ctx.pathParam("s3Key");
		S3Util.getInstance().deleteObject(s3Key);
		
		Upload upload = new Upload();
		upload.setEmployeeID(user.getId());
		upload.setFileURL(S3Util.getInstance().getObjectUrl(s3Key));
		upload.setS3Key(s3Key);
		
		if(uploadService.deleteUpload(upload)) {
			ctx.status(204);
		}else {
			throw new InternalServerErrorResponse();
		}
	}
}
