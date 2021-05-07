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
import com.revature.models.Upload;
import com.revature.utils.CassandraUtil;


@Component
public class UploadDaoCass implements UploadDao {

	@Autowired
	private CqlSession session;

	public UploadDaoCass(CqlSession session) {
		this.session = session;
	}
	
	private Upload buildUpload(Row row) {
		Upload upload = new Upload();
		upload.setEmployeeID(row.getInt("employeeId"));
		upload.setFileURL(row.getString("fileURL"));
		upload.setS3Key(row.getString("s3Key"));
		return upload;
	}

	@Override
	public List<Upload> getUploads(Employee employee) {
		List<Upload> uploads = new ArrayList<>();
		String query = "select * from upload where employeeId=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(employee.getId());
		ResultSet results = session.execute(bound);
		
		for(Row row : results){
			uploads.add(buildUpload(row));
		}
		
		return uploads;
	}

	@Override
	public void addUpload(Upload upload) {
		String query = "insert into upload (employeeId, fileURL, s3Key) "
				+ "values (?, ?, ?)";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(upload.getEmployeeID(), upload.getFileURL(), upload.getS3Key());
		session.execute(bound);
	}

	@Override
	public void deleteUpload(Upload upload) {
		String query = "delete from upload where employeeId=? and s3Key=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(upload.getEmployeeID(), upload.getS3Key());
		session.execute(bound);
	}

}
