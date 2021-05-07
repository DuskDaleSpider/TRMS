package com.revature.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.revature.models.EventType;
import com.revature.models.GradingFormat;
import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.utils.CassandraUtil;

@Component
public class ReimbursementDaoCass implements ReimbursementDao {
	
	@Autowired
	private CqlSession session;

	public ReimbursementDaoCass(CqlSession session) {
		this.session = session;
	}
	
	private Reimbursement buildReimbursement(Row row) {
		Reimbursement r = new Reimbursement();
		
		r.setId(row.getInt("id"));
		r.setAssignedTo(row.getInt("assignedTo"));
		r.setEmployee(row.getInt("employee"));
		
		r.setEventDate(LocalDateTime.parse(row.getString("eventDate")));
		r.setEventLocation(row.getString("eventLocation"));
		r.setEventDescription(row.getString("eventDescription"));
		r.setEventType(EventType.valueOf(row.getString("eventType")));
		r.setEventCost(row.getDouble("eventCost"));
		r.setEventGradingFormat(GradingFormat.valueOf(row.getString("eventGradingFormat")));
		r.setEventAttachments(row.getList("eventAttachments", String.class));
		r.setPassingGrade(row.getDouble("passingGrade"));
		
		r.setJustification(row.getString("justification"));
		r.setStatus(Status.valueOf(row.getString("status")));
		r.setDateSubmitted(LocalDateTime.parse(row.getString("dateSubmitted")));
		r.setDateUpdated(LocalDateTime.parse(row.getString("dateUpdated")));
		r.setPreApprovalEmail(row.getString("preApprovalEmail"));
		r.setUrgent(row.getBoolean("isUrgent"));
		r.setProjectedAmount(row.getDouble("projectedAmount"));
		r.setAwardedAmount(row.getDouble("awardedAmount"));
		
		r.setWorkTimeMissed(row.getString("workTimeMissed"));
		r.setMessage(row.getString("message"));
		r.setFinalReport(row.getString("finalReport"));
		r.setExceededEmployeesAvailableFunds(row.getBoolean("exceededEmployeesAvailableFunds"));
		
		//optional
		if(row.getString("preApprovalType") != null && !row.getString("preApprovalType").equals("")) {
			r.setPreApprovalType(Status.valueOf(row.getString("preApprovalType")));
		}
		
		return r;
	}

	private int generateId() {
		String query = "select * from reimbursement";
		ResultSet results = session.execute(query);
		return results.all().size() + 1;
	}
	
	@Override
	public List<Reimbursement> getAssignedReimbursements(int employeeId) {
		List<Reimbursement> reimbursements = new ArrayList<>();
		String query = "select * from reimbursement";
		ResultSet results = session.execute(query);
		
		for(Row row : results) {
			if(row.getInt("assignedTo") == employeeId && !row.getString("status").equals(Status.AWARDED.toString()))
				reimbursements.add(buildReimbursement(row));
		}
		
		return reimbursements;
	}

	@Override
	public List<Reimbursement> getReimbursements(int employeeId) {
		List<Reimbursement> reimbursements = new ArrayList<>();
		String query = "select * from reimbursement";
		ResultSet results = session.execute(query);
		
		for(Row row : results) {
			if(row.getInt("employee") == employeeId)
				reimbursements.add(buildReimbursement(row));
		}
				
		return reimbursements;
	}

	@Override
	public Reimbursement getReimbursement(int id) {
		String query = "select * from reimbursement where id=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(id);
		ResultSet results = session.execute(bound);
		Row row = results.one();
		return row != null ? buildReimbursement(row) : null;
	}

	@Override
	public void addReimbursement(Reimbursement r) {
		r.setId(generateId());
		
		String query = "insert into reimbursement (id, employee, assignedTo, eventDate, eventLocation, eventDescription, "
				+ "eventCost, eventGradingFormat, passingGrade, eventAttachments, eventType, justification, status, dateSubmitted,"
				+ "dateUpdated, preApprovalEmail, preApprovalType, isUrgent, proofOfPassing, projectedAmount, awardedAmount, "
				+ "workTimeMissed, message, exceededEmployeesAvailableFunds, finalReport) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prepared = session.prepare(query);
		
		BoundStatement bound = prepared.bind(r.getId(), r.getEmployee(), r.getAssignedTo(), r.getEventDate().toString(), 
				r.getEventLocation(), r.getEventDescription(), r.getEventCost(), r.getEventGradingFormat().toString(), r.getPassingGrade(), 
				r.getEventAttachments(), r.getEventType().toString(), r.getJustification(), r.getStatus().toString(),
				r.getDateSubmitted().toString(), r.getDateUpdated().toString(), r.getPreApprovalEmail(), 
				r.getPreApprovalType() == null ? null : r.getPreApprovalType().toString(),
				r.isUrgent(), r.getProofOfPassing(), r.getProjectedAmount(), r.getAwardedAmount(),
				r.getWorkTimeMissed(), r.getMessage(), r.getExceededEmployeesAvailableFunds(), r.getFinalReport());
		
		session.execute(bound);	
	}

	@Override
	public void updateReimbursement(Reimbursement r) {
		String query = "update reimbursement set assignedTo=?, eventDate=?, eventLocation=?, eventDescription=?,"
				+ "eventCost=?, eventGradingFormat=?, passingGrade=?, eventType=?, eventAttachments=?, justification=?, status=?, "
				+ "dateUpdated=?, preApprovalEmail=?, preApprovalType=?, isUrgent=?, proofOfPassing=?, projectedAmount=?,"
				+ "awardedAmount=?, workTimeMissed=?, message=?, exceededEmployeesAvailableFunds=?, finalReport=? where id=? and employee=? and dateSubmitted=?";
		PreparedStatement prepared = session.prepare(query);
		BoundStatement bound = prepared.bind(r.getAssignedTo(), r.getEventDate().toString(), r.getEventLocation(), r.getEventDescription(),
				r.getEventCost(), r.getEventGradingFormat().toString(), r.getPassingGrade(), r.getEventType().toString(), r.getEventAttachments(), r.getJustification(),
				r.getStatus().toString(), r.getDateUpdated().toString(), r.getPreApprovalEmail(), 
				r.getPreApprovalType() != null ? r.getPreApprovalType().toString() : null, r.isUrgent() , r.getProofOfPassing(), 
				r.getProjectedAmount(), r.getAwardedAmount(), r.getWorkTimeMissed(), r.getMessage(), r.getExceededEmployeesAvailableFunds(), r.getFinalReport(),
				r.getId(), r.getEmployee(), r.getDateSubmitted().toString());

		session.execute(bound);
	}
	
	public void setSession(CqlSession session) {
		this.session = session;
	}

}
