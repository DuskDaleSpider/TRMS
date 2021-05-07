package com.revature.services;

import java.util.List;

import com.revature.models.Employee;
import com.revature.models.Upload;

public interface UploadService {
	
	public List<Upload> getUploads(Employee employee);
	
	public boolean addUpload(Upload upload);
	
	public boolean deleteUpload(Upload upload);
	
}
