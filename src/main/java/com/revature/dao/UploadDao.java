package com.revature.dao;

import java.util.List;

import com.revature.models.Employee;
import com.revature.models.Upload;

public interface UploadDao {

	public List<Upload> getUploads(Employee employee);
	
	public void addUpload(Upload upload);
	
	public void deleteUpload(Upload upload); 
}
