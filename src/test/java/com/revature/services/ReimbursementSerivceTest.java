package com.revature.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.dao.ReimbursementDao;
import com.revature.dao.ReimbursementDaoCass;
import com.revature.models.EventType;
import com.revature.models.GradingFormat;
import com.revature.models.Reimbursement;
import com.revature.models.Status;

@RunWith(MockitoJUnitRunner.class)
public class ReimbursementSerivceTest {
	
	private ReimbursementServiceImpl service;
	private Reimbursement testReimbursement;
	private List<Reimbursement> reimbursements;
	private ReimbursementDao dao;

	@Before
	public void setUp() throws Exception {
		//create service
		this.service = new ReimbursementServiceImpl(mock(ReimbursementDao.class));
		
		
		//create a test reimbursement with dummy data
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
		testReimbursement.setAwardedAmount(testReimbursement.getProjectedAmount());
		
		//Also add it to a list and add another one
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
		temp.setAwardedAmount(testReimbursement.getProjectedAmount());
		
		reimbursements.add(temp);	
		
		//create dao and inject
		dao = mock(ReimbursementDaoCass.class);
		service.setReimbursementDao(dao);
	}

	@Test
	public void getReimbursementReturnsReimbursement() {
		when(dao.getReimbursement(testReimbursement.getId())).thenReturn(testReimbursement);
		
		Reimbursement reimbursement = service.getReimbursement(testReimbursement.getId());
		assertEquals("Reimbursement should have id " + testReimbursement.getId(), testReimbursement.getId(), reimbursement.getId());
	}
	
	@Test
	public void getReimbursementListReturnsList() {
		when(dao.getReimbursements(2)).thenReturn(reimbursements);
		
		List<Reimbursement> list = service.getReimbursements(2);
		assertEquals("Lists should be equal", reimbursements, list);
	}
	
	@Test
	public void getAssignedReimbursementsReturnsList() {
		when(dao.getAssignedReimbursements(1)).thenReturn(reimbursements);
		
		List<Reimbursement> list = service.getAssignedReimbursements(1);
		assertEquals("Lists should be equal", reimbursements, list);
	}
	
	@Test
	public void addReimbursementReturnsTrue() {
		assertTrue("add reimbursement should return true", service.addReimbursement(testReimbursement));
	}
	
	@Test
	public void updateReimbursementReturnsTrue() {
		testReimbursement.setStatus(Status.AWARDED);
				
		assertTrue("add reimbursement should return true", service.updateReimbursement(testReimbursement));
	}

}
