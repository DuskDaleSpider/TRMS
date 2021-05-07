package com.revature.dao;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementDao {

	public List<Reimbursement> getAssignedReimbursements(int employeeId);
	public List<Reimbursement> getReimbursements(int employeeId);
	public Reimbursement getReimbursement(int id);
	public void addReimbursement(Reimbursement reimbursement);
	public void updateReimbursement(Reimbursement reimbursement);
		
}
