package com.revature.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.revature.models.EventType;
import com.revature.models.GradingFormat;
import com.revature.models.Reimbursement;
import com.revature.models.Status;

@RunWith(MockitoJUnitRunner.class)
public class ReimbursementDaoTest {

	private ReimbursementDaoCass dao;
	private Reimbursement testReimbursement;
	private List<Reimbursement> reimbursements;
	
	@Before
	public void setUp() throws Exception {
		this.dao = new ReimbursementDaoCass(mock(CqlSession.class));
		
		testReimbursement = new Reimbursement();
		testReimbursement.setId(1);
		testReimbursement.setAssignedTo(1);
		testReimbursement.setEmployee(2);
		testReimbursement.setEventDate(LocalDateTime.of(2021, 5, 30, 9, 0)); // 5/30/2021 9:00 AM
		testReimbursement.setEventLocation("Las Vegas, NV");
		testReimbursement.setEventDescription("2021 Reactive Java seminar");
		testReimbursement.setEventType(EventType.SEMINAR);
		testReimbursement.setEventCost(100.00);
		testReimbursement.setEventGradingFormat(GradingFormat.PERCENTAGE);
		testReimbursement.setPassingGrade(GradingFormat.PERCENTAGE.getPassPercentage());
		testReimbursement.setEventAttachments(new ArrayList<String>());
		testReimbursement.setJustification("This will further improve my reactive skills for Java");
		testReimbursement.setStatus(Status.SUBMITTED);
		testReimbursement.setDateSubmitted(LocalDateTime.now());
		testReimbursement.setDateUpdated(LocalDateTime.now());
		testReimbursement.setPreApprovalEmail("");
		testReimbursement.setPreApprovalType(null);
		testReimbursement.setUrgent(false);
		testReimbursement.setProjectedAmount(testReimbursement.getEventCost() * testReimbursement.getEventType().getPercent());
		testReimbursement.setAwardedAmount(0);
		testReimbursement.setWorkTimeMissed(null);
		testReimbursement.setMessage(null);
		testReimbursement.setExceededEmployeesAvailableFunds(false);
		testReimbursement.setFinalReport(null);
		
		reimbursements = new ArrayList<Reimbursement>();
		reimbursements.add(testReimbursement);
		
		Reimbursement temp = new Reimbursement();
		temp.setId(2);
		temp.setAssignedTo(1);
		temp.setEmployee(2);
		temp.setEventDate(LocalDateTime.of(2021, 5, 20, 9, 0)); // 5/30/2021 9:00 AM
		temp.setEventLocation("New York, NY");
		temp.setEventDescription("2021 Cassandra seminar");
		temp.setEventType(EventType.SEMINAR);
		temp.setEventCost(150.00);
		temp.setEventGradingFormat(GradingFormat.PASS_FAIL);
		temp.setPassingGrade(GradingFormat.PASS_FAIL.getPassPercentage());
		temp.setEventAttachments(new ArrayList<String>());
		temp.setJustification("This will further improve my reactive skills with Cassandra");
		temp.setStatus(Status.SUBMITTED);
		temp.setDateSubmitted(LocalDateTime.now());
		temp.setDateUpdated(LocalDateTime.now());
		temp.setPreApprovalEmail("");
		temp.setPreApprovalType(null);
		temp.setUrgent(false);
		temp.setProjectedAmount(testReimbursement.getEventCost() * testReimbursement.getEventType().getPercent());
		temp.setAwardedAmount(0);
		temp.setAwardedAmount(0);
		temp.setWorkTimeMissed(null);
		temp.setMessage(null);
		temp.setExceededEmployeesAvailableFunds(false);
		temp.setFinalReport(null);
		
		reimbursements.add(temp);		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAssignedReimbursementsReturns() {
		CqlSession session = mock(CqlSession.class);
		ResultSet result = mock(ResultSet.class);
		Row row = mock(Row.class);
		Iterator<Row> iterator = mock(Iterator.class);
		
		dao.setSession(session);
		
		when(session.execute("select * from reimbursement")).thenReturn(result);
		when(iterator.hasNext()).thenReturn(true, true, false);
		when(iterator.next()).thenReturn(row, row);
		when(result.iterator()).thenReturn(iterator);
		
		when(row.getInt("assignedTo")).thenReturn(reimbursements.get(0).getAssignedTo(), reimbursements.get(0).getAssignedTo(), reimbursements.get(1).getAssignedTo(), reimbursements.get(1).getAssignedTo());
		when(row.getString("status")).thenReturn(reimbursements.get(0).getStatus().toString(), reimbursements.get(0).getStatus().toString(), reimbursements.get(1).getStatus().toString(), reimbursements.get(1).getStatus().toString());
		
		
		when(row.getInt("id")).thenReturn(reimbursements.get(0).getId(), reimbursements.get(1).getId());
		//when(row.getInt("assignedTo")).thenReturn(reimbursements.get(0).getAssignedTo(), reimbursements.get(1).getAssignedTo());
		when(row.getInt("employee")).thenReturn(reimbursements.get(0).getEmployee(), reimbursements.get(1).getEmployee());
		when(row.getString("eventDate")).thenReturn(reimbursements.get(0).getEventDate().toString(), reimbursements.get(1).getEventDate().toString());
		when(row.getString("eventLocation")).thenReturn(reimbursements.get(0).getEventLocation(), reimbursements.get(1).getEventLocation());
		when(row.getString("eventDescription")).thenReturn(reimbursements.get(0).getEventDescription(), reimbursements.get(1).getEventDescription());
		when(row.getString("eventType")).thenReturn(reimbursements.get(0).getEventType().toString(), reimbursements.get(1).getEventType().toString());
		when(row.getDouble("eventCost")).thenReturn(reimbursements.get(0).getEventCost(), reimbursements.get(1).getEventCost());
		when(row.getString("eventGradingFormat")).thenReturn(reimbursements.get(0).getEventGradingFormat().toString(), reimbursements.get(1).getEventGradingFormat().toString());
		when(row.getDouble("passingGrade")).thenReturn(reimbursements.get(0).getPassingGrade(), reimbursements.get(1).getPassingGrade());
		when(row.getList("eventAttachments", String.class)).thenReturn(reimbursements.get(0).getEventAttachments(), reimbursements.get(1).getEventAttachments());
		when(row.getString("justification")).thenReturn(reimbursements.get(0).getJustification(), reimbursements.get(1).getJustification());
		//when(row.getString("status")).thenReturn(reimbursements.get(0).getStatus().toString(), reimbursements.get(1).getStatus().toString());
		when(row.getString("dateSubmitted")).thenReturn(reimbursements.get(0).getDateSubmitted().toString(), reimbursements.get(1).getDateSubmitted().toString());
		when(row.getString("dateUpdated")).thenReturn(reimbursements.get(0).getDateUpdated().toString(), reimbursements.get(1).getDateUpdated().toString());
		when(row.getString("preApprovalEmail")).thenReturn(reimbursements.get(0).getPreApprovalEmail(), reimbursements.get(1).getPreApprovalEmail());
		when(row.getString("preApprovalType")).thenReturn(
				reimbursements.get(0).getPreApprovalType() != null ? reimbursements.get(0).getPreApprovalType().toString() : null,
				reimbursements.get(1).getPreApprovalType() != null ? reimbursements.get(1).getPreApprovalType().toString() : null);
		when(row.getBoolean("isUrgent")).thenReturn(reimbursements.get(0).isUrgent(), reimbursements.get(1).isUrgent());
		when(row.getDouble("projectedAmount")).thenReturn(reimbursements.get(0).getProjectedAmount(), reimbursements.get(1).getProjectedAmount());
		when(row.getDouble("awardedAmount")).thenReturn(reimbursements.get(0).getAwardedAmount(), reimbursements.get(1).getAwardedAmount());
		
		when(row.getString("workTimeMissed")).thenReturn(reimbursements.get(0).getWorkTimeMissed(), reimbursements.get(1).getWorkTimeMissed());
		when(row.getString("message")).thenReturn(reimbursements.get(0).getMessage(), reimbursements.get(1).getMessage());
		when(row.getBoolean("exceededEmployeesAvailableFunds")).thenReturn(reimbursements.get(0).getExceededEmployeesAvailableFunds(), reimbursements.get(1).getExceededEmployeesAvailableFunds());
		when(row.getString("finalReport")).thenReturn(reimbursements.get(0).getFinalReport(), reimbursements.get(1).getFinalReport());
		
		List<Reimbursement> test = dao.getAssignedReimbursements(reimbursements.get(0).getAssignedTo());
		assertEquals("List should be equals", this.reimbursements, test);
	}	

	@Test
	public void getReimbursementReturnsReimbursement() {
		
		//set up mocks
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		ResultSet result = mock(ResultSet.class);
		Row row = mock(Row.class);
		
		//inject mock session
		dao.setSession(session);
		
		//mock the query sequence
		when(session.prepare("select * from reimbursement where id=?")).thenReturn(prepared);
		when(prepared.bind(testReimbursement.getId())).thenReturn(bound);
		when(session.execute(bound)).thenReturn(result);
		when(result.one()).thenReturn(row);
		
		//mock the row data returned
		when(row.getInt("id")).thenReturn(testReimbursement.getId());
		when(row.getInt("assignedTo")).thenReturn(testReimbursement.getAssignedTo());
		when(row.getInt("employee")).thenReturn(testReimbursement.getEmployee());
		when(row.getString("eventDate")).thenReturn(testReimbursement.getEventDate().toString());
		when(row.getString("eventLocation")).thenReturn(testReimbursement.getEventLocation());
		when(row.getString("eventDescription")).thenReturn(testReimbursement.getEventDescription());
		when(row.getString("eventType")).thenReturn(testReimbursement.getEventType().toString());
		when(row.getDouble("eventCost")).thenReturn(testReimbursement.getEventCost());
		when(row.getString("eventGradingFormat")).thenReturn(testReimbursement.getEventGradingFormat().toString());
		when(row.getList("eventAttachments", String.class)).thenReturn(testReimbursement.getEventAttachments());
		when(row.getString("justification")).thenReturn(testReimbursement.getJustification());
		when(row.getString("status")).thenReturn(testReimbursement.getStatus().toString());
		when(row.getString("dateSubmitted")).thenReturn(testReimbursement.getDateSubmitted().toString());
		when(row.getString("dateUpdated")).thenReturn(testReimbursement.getDateUpdated().toString());
		when(row.getString("preApprovalEmail")).thenReturn(testReimbursement.getPreApprovalEmail());
		when(row.getString("preApprovalType")).thenReturn(
				testReimbursement.getPreApprovalType() != null ? testReimbursement.getPreApprovalType().toString() : null);
		when(row.getBoolean("isUrgent")).thenReturn(testReimbursement.isUrgent());
		when(row.getDouble("projectedAmount")).thenReturn(testReimbursement.getProjectedAmount());
		when(row.getDouble("awardedAmount")).thenReturn(testReimbursement.getAwardedAmount());
		
		
		Reimbursement reimbursement = dao.getReimbursement(testReimbursement.getId());
		assertEquals("Reimbursement id should be " + testReimbursement.getId(), 
				testReimbursement.getId(), reimbursement.getId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getReimbursementListReturnsList() {
		//set up mocks
		CqlSession session = mock(CqlSession.class);
		ResultSet result = mock(ResultSet.class);
		Row row = mock(Row.class);
		Iterator<Row> iterator = mock(Iterator.class);
		
		//inject mock session
		dao.setSession(session);
		
		//mock the query sequence
		when(session.execute("select * from reimbursement")).thenReturn(result);
		
		when(iterator.hasNext()).thenReturn(true, true, false);
		when(iterator.next()).thenReturn(row, row);
		when(result.iterator()).thenReturn(iterator);
		
		//mock the row data returned
		when(row.getInt("id")).thenReturn(reimbursements.get(0).getId(), reimbursements.get(1).getId());
		when(row.getInt("assignedTo")).thenReturn(reimbursements.get(0).getAssignedTo(), reimbursements.get(1).getAssignedTo());
		when(row.getInt("employee")).thenReturn(reimbursements.get(0).getEmployee(), reimbursements.get(1).getEmployee());
		when(row.getString("eventDate")).thenReturn(reimbursements.get(0).getEventDate().toString(), reimbursements.get(1).getEventDate().toString());
		when(row.getString("eventLocation")).thenReturn(reimbursements.get(0).getEventLocation(), reimbursements.get(1).getEventLocation());
		when(row.getString("eventDescription")).thenReturn(reimbursements.get(0).getEventDescription(), reimbursements.get(1).getEventDescription());
		when(row.getString("eventType")).thenReturn(reimbursements.get(0).getEventType().toString(), reimbursements.get(1).getEventType().toString());
		when(row.getDouble("eventCost")).thenReturn(reimbursements.get(0).getEventCost(), reimbursements.get(1).getEventCost());
		when(row.getString("eventGradingFormat")).thenReturn(reimbursements.get(0).getEventGradingFormat().toString(), reimbursements.get(1).getEventGradingFormat().toString());
		when(row.getDouble("passingGrade")).thenReturn(reimbursements.get(0).getPassingGrade(), reimbursements.get(1).getPassingGrade());
		when(row.getList("eventAttachments", String.class)).thenReturn(reimbursements.get(0).getEventAttachments(), reimbursements.get(1).getEventAttachments());
		when(row.getString("justification")).thenReturn(reimbursements.get(0).getJustification(), reimbursements.get(1).getJustification());
		when(row.getString("status")).thenReturn(reimbursements.get(0).getStatus().toString(), reimbursements.get(1).getStatus().toString());
		when(row.getString("dateSubmitted")).thenReturn(reimbursements.get(0).getDateSubmitted().toString(), reimbursements.get(1).getDateSubmitted().toString());
		when(row.getString("dateUpdated")).thenReturn(reimbursements.get(0).getDateUpdated().toString(), reimbursements.get(1).getDateUpdated().toString());
		when(row.getString("preApprovalEmail")).thenReturn(reimbursements.get(0).getPreApprovalEmail(), reimbursements.get(1).getPreApprovalEmail());
		when(row.getString("preApprovalType")).thenReturn(
				reimbursements.get(0).getPreApprovalType() != null ? reimbursements.get(0).getPreApprovalType().toString() : null,
				reimbursements.get(1).getPreApprovalType() != null ? reimbursements.get(1).getPreApprovalType().toString() : null);
		when(row.getBoolean("isUrgent")).thenReturn(reimbursements.get(0).isUrgent(), reimbursements.get(1).isUrgent());
		when(row.getDouble("projectedAmount")).thenReturn(reimbursements.get(0).getProjectedAmount(), reimbursements.get(1).getProjectedAmount());
		when(row.getDouble("awardedAmount")).thenReturn(reimbursements.get(0).getAwardedAmount(), reimbursements.get(1).getAwardedAmount());
		
		when(row.getString("workTimeMissed")).thenReturn(reimbursements.get(0).getWorkTimeMissed(), reimbursements.get(1).getWorkTimeMissed());
		when(row.getString("message")).thenReturn(reimbursements.get(0).getMessage(), reimbursements.get(1).getMessage());
		when(row.getBoolean("exceededEmployeesAvailableFunds")).thenReturn(reimbursements.get(0).getExceededEmployeesAvailableFunds(), reimbursements.get(1).getExceededEmployeesAvailableFunds());
		when(row.getString("finalReport")).thenReturn(reimbursements.get(0).getFinalReport(), reimbursements.get(1).getFinalReport());
				
		
		List<Reimbursement> list = dao.getReimbursements(2);
		assertEquals("Reimbursements should equal list", reimbursements, list);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void addReimbursementSucceeds() {
		//set up mocks
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		ResultSet result = mock(ResultSet.class);
		List<?> rows = mock(ArrayList.class);
		
		dao.setSession(session);
		
		//generate the id
		when(session.execute("select * from reimbursement")).thenReturn(result);
		when(result.all()).thenReturn((List<Row>) rows);
		when(rows.size()).thenReturn(0);
		
		//bind and execute query
		when(session.prepare("insert into reimbursement (id, employee, assignedTo, eventDate, eventLocation, eventDescription, "
				+ "eventCost, eventGradingFormat, passingGrade, eventAttachments, eventType, justification, status, dateSubmitted,"
				+ "dateUpdated, preApprovalEmail, preApprovalType, isUrgent, proofOfPassing, projectedAmount, awardedAmount, "
				+ "workTimeMissed, message, exceededEmployeesAvailableFunds, finalReport) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")).thenReturn(prepared);
		when(prepared.bind(testReimbursement.getId(), testReimbursement.getEmployee(), testReimbursement.getAssignedTo(), testReimbursement.getEventDate().toString(), 
				testReimbursement.getEventLocation(), testReimbursement.getEventDescription(), testReimbursement.getEventCost(), testReimbursement.getEventGradingFormat().toString(), testReimbursement.getPassingGrade(), 
				testReimbursement.getEventAttachments(), testReimbursement.getEventType().toString(), testReimbursement.getJustification(), testReimbursement.getStatus().toString(),
				testReimbursement.getDateSubmitted().toString(), testReimbursement.getDateUpdated().toString(), testReimbursement.getPreApprovalEmail(), 
				testReimbursement.getPreApprovalType() == null ? null : testReimbursement.getPreApprovalType().toString(),
				testReimbursement.isUrgent(), testReimbursement.getProofOfPassing(), testReimbursement.getProjectedAmount(), testReimbursement.getAwardedAmount(),
				testReimbursement.getWorkTimeMissed(), testReimbursement.getMessage(), testReimbursement.getExceededEmployeesAvailableFunds(), testReimbursement.getFinalReport())).thenReturn(bound);
				
		dao.addReimbursement(testReimbursement);
		verify(session).execute(bound);
	}

	@Test
	public void updateReimbursementSucceeds() {
		testReimbursement.setStatus(Status.AWARDED);
		testReimbursement.setDateUpdated(LocalDateTime.now());
		
		CqlSession session = mock(CqlSession.class);
		PreparedStatement prepared = mock(PreparedStatement.class);
		BoundStatement bound = mock(BoundStatement.class);
		
		dao.setSession(session);
		
		when(session.prepare( "update reimbursement set assignedTo=?, eventDate=?, eventLocation=?, eventDescription=?,"
				+ "eventCost=?, eventGradingFormat=?, passingGrade=?, eventType=?, eventAttachments=?, justification=?, status=?, "
				+ "dateUpdated=?, preApprovalEmail=?, preApprovalType=?, isUrgent=?, proofOfPassing=?, projectedAmount=?,"
				+ "awardedAmount=?, workTimeMissed=?, message=?, exceededEmployeesAvailableFunds=?, finalReport=? where id=? and employee=? and dateSubmitted=?")).thenReturn(prepared);
		when(prepared.bind(testReimbursement.getAssignedTo(), testReimbursement.getEventDate().toString(), testReimbursement.getEventLocation(), testReimbursement.getEventDescription(),
				testReimbursement.getEventCost(), testReimbursement.getEventGradingFormat().toString(), testReimbursement.getPassingGrade(), testReimbursement.getEventType().toString(), testReimbursement.getEventAttachments(), testReimbursement.getJustification(),
				testReimbursement.getStatus().toString(), testReimbursement.getDateUpdated().toString(), testReimbursement.getPreApprovalEmail(), 
				testReimbursement.getPreApprovalType() != null ? testReimbursement.getPreApprovalType().toString() : null, testReimbursement.isUrgent() , testReimbursement.getProofOfPassing(), 
				testReimbursement.getProjectedAmount(), testReimbursement.getAwardedAmount(), testReimbursement.getWorkTimeMissed(), testReimbursement.getMessage(), testReimbursement.getExceededEmployeesAvailableFunds(), testReimbursement.getFinalReport(),
				testReimbursement.getId(), testReimbursement.getEmployee(), testReimbursement.getDateSubmitted().toString())).thenReturn(bound);
		
		dao.updateReimbursement(testReimbursement);
		verify(session).execute(bound);
	}
}
