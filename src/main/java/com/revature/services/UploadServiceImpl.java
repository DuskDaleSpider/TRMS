package com.revature.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.dao.UploadDao;
import com.revature.dao.UploadDaoCass;
import com.revature.models.Employee;
import com.revature.models.Upload;

@Service
public class UploadServiceImpl implements UploadService {

	private static final Logger logger = LogManager.getLogger(UploadServiceImpl.class);

	@Autowired
	UploadDao uploadDao;

	public UploadServiceImpl(UploadDao uploadDao) {
		this.uploadDao = uploadDao;
	}

	@Override
	public List<Upload> getUploads(Employee employee) {
		try {
			return uploadDao.getUploads(employee);
		} catch (Exception e) {
			logger.warn(e);
			for (StackTraceElement el : e.getStackTrace()) {
				logger.debug(el);
			}
			return null;
		}
	}

	@Override
	public boolean addUpload(Upload upload) {
		try {
			uploadDao.addUpload(upload);
			return true;
		} catch (Exception e) {
			logger.warn(e);
			for (StackTraceElement el : e.getStackTrace()) {
				logger.debug(el);
			}
			return false;
		}
	}

	@Override
	public boolean deleteUpload(Upload upload) {
		try {
			uploadDao.deleteUpload(upload);
			return true;
		} catch (Exception e) {	
			logger.warn(e);
			for (StackTraceElement el : e.getStackTrace()) {
				logger.debug(el);
			}
			return false;
		}
	}
	
	public void setUploadDao(UploadDao dao) {
		this.uploadDao = dao;
	}

}
