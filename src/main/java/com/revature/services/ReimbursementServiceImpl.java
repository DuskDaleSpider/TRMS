package com.revature.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.dao.ReimbursementDao;
import com.revature.dao.ReimbursementDaoCass;
import com.revature.models.Reimbursement;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

	@Autowired
	ReimbursementDao dao;

	public ReimbursementServiceImpl(ReimbursementDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Reimbursement> getAssignedReimbursements(int employeeId) {
		try {
			return dao.getAssignedReimbursements(employeeId);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Reimbursement> getReimbursements(int employeeId) {
		try {
			return dao.getReimbursements(employeeId);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Reimbursement getReimbursement(int id) {
		try {
			return dao.getReimbursement(id);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addReimbursement(Reimbursement reimbursement) {
		try {
			dao.addReimbursement(reimbursement);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateReimbursement(Reimbursement reimbursement) {
		try {
			dao.updateReimbursement(reimbursement);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<Reimbursement> getReimbursementsByUserAndYear(int employeeId, int year) {
		try {
			List<Reimbursement> usersReimbursements = dao.getReimbursements(employeeId);
			return usersReimbursements.stream().filter(reimbursement -> {
				return reimbursement.getEventDate().getYear() == LocalDate.now().getYear();
			}).collect(Collectors.toList());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setReimbursementDao(ReimbursementDao dao) {
		this.dao = dao;
	}

}
