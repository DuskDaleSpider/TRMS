package com.revature.services;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementService {
	
	public List<Reimbursement> getAssignedReimbursements(int employeeId);
	public List<Reimbursement> getReimbursements(int employeeId);
	public List<Reimbursement> getReimbursementsByUserAndYear(int employeeId, int year);
	public Reimbursement getReimbursement(int id);
	public boolean addReimbursement(Reimbursement reimbursement);
	public boolean updateReimbursement(Reimbursement reimbursement);
	
}
