package com.revature.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.json.LocalDateTimeDeserializer;
import com.revature.json.LocalDateTimeSerializer;

public class Reimbursement {
	private int id;
	private int employee;
	private int assignedTo;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime eventDate;
	private String eventLocation;
	private String eventDescription;
	private double eventCost;
	private GradingFormat eventGradingFormat;
	private List<String> eventAttachments;
	private EventType eventType;
	private double passingGrade;
	private String justification;
	private Status status;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateSubmitted;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateUpdated;
	
	private String preApprovalEmail;
	private Status preApprovalType;
	private boolean isUrgent;
	private String proofOfPassing;
	private double projectedAmount;
	private double awardedAmount;
	
	private String workTimeMissed; 
	private String message;
	private Boolean exceededEmployeesAvailableFunds;
	private String finalReport;

	public Reimbursement() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmployee() {
		return employee;
	}

	public void setEmployee(int employee) {
		this.employee = employee;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime localDateTime) {
		this.eventDate = localDateTime;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public double getEventCost() {
		return eventCost;
	}

	public void setEventCost(double eventCost) {
		this.eventCost = eventCost;
	}

	public GradingFormat getEventGradingFormat() {
		return eventGradingFormat;
	}

	public void setEventGradingFormat(GradingFormat eventGradingFormat) {
		this.eventGradingFormat = eventGradingFormat;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public double getPassingGrade() {
		return passingGrade;
	}

	public void setPassingGrade(double passingGrade) {
		this.passingGrade = passingGrade;
	}

	public List<String> getEventAttachments() {
		return eventAttachments;
	}

	public void setEventAttachments(List<String> eventAttachments) {
		this.eventAttachments = eventAttachments;
	}
	
	public void addEventAttachment(String attachment) {
		this.eventAttachments.add(attachment);
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(LocalDateTime dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getPreApprovalEmail() {
		return preApprovalEmail;
	}

	public void setPreApprovalEmail(String preApprovalEmail) {
		this.preApprovalEmail = preApprovalEmail;
	}

	public Status getPreApprovalType() {
		return preApprovalType;
	}

	public void setPreApprovalType(Status preApprovalType) {
		this.preApprovalType = preApprovalType;
	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public String getProofOfPassing() {
		return proofOfPassing;
	}

	public void setProofOfPassing(String proofOfPassing) {
		this.proofOfPassing = proofOfPassing;
	}

	public double getProjectedAmount() {
		return projectedAmount;
	}

	public void setProjectedAmount(double projectedAmount) {
		this.projectedAmount = projectedAmount;
	}

	public double getAwardedAmount() {
		return awardedAmount;
	}

	public void setAwardedAmount(double reimbursementAmount) {
		this.awardedAmount = reimbursementAmount;
	}
	
	

	public String getWorkTimeMissed() {
		return workTimeMissed;
	}

	public void setWorkTimeMissed(String workTimeMissed) {
		this.workTimeMissed = workTimeMissed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getExceededEmployeesAvailableFunds() {
		return exceededEmployeesAvailableFunds;
	}

	public void setExceededEmployeesAvailableFunds(Boolean exceededEmployeesAvailableFunds) {
		this.exceededEmployeesAvailableFunds = exceededEmployeesAvailableFunds;
	}

	public String getFinalReport() {
		return finalReport;
	}

	public void setFinalReport(String finalReport) {
		this.finalReport = finalReport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + assignedTo;
		long temp;
		temp = Double.doubleToLongBits(awardedAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((dateSubmitted == null) ? 0 : dateSubmitted.hashCode());
		result = prime * result + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result + employee;
		result = prime * result + ((eventAttachments == null) ? 0 : eventAttachments.hashCode());
		temp = Double.doubleToLongBits(eventCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + ((eventDescription == null) ? 0 : eventDescription.hashCode());
		result = prime * result + ((eventGradingFormat == null) ? 0 : eventGradingFormat.hashCode());
		result = prime * result + ((eventLocation == null) ? 0 : eventLocation.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result
				+ ((exceededEmployeesAvailableFunds == null) ? 0 : exceededEmployeesAvailableFunds.hashCode());
		result = prime * result + ((finalReport == null) ? 0 : finalReport.hashCode());
		result = prime * result + id;
		result = prime * result + (isUrgent ? 1231 : 1237);
		result = prime * result + ((justification == null) ? 0 : justification.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		temp = Double.doubleToLongBits(passingGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((preApprovalEmail == null) ? 0 : preApprovalEmail.hashCode());
		result = prime * result + ((preApprovalType == null) ? 0 : preApprovalType.hashCode());
		temp = Double.doubleToLongBits(projectedAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((proofOfPassing == null) ? 0 : proofOfPassing.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((workTimeMissed == null) ? 0 : workTimeMissed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (assignedTo != other.assignedTo)
			return false;
		if (Double.doubleToLongBits(awardedAmount) != Double.doubleToLongBits(other.awardedAmount))
			return false;
		if (dateSubmitted == null) {
			if (other.dateSubmitted != null)
				return false;
		} else if (!dateSubmitted.equals(other.dateSubmitted))
			return false;
		if (dateUpdated == null) {
			if (other.dateUpdated != null)
				return false;
		} else if (!dateUpdated.equals(other.dateUpdated))
			return false;
		if (employee != other.employee)
			return false;
		if (eventAttachments == null) {
			if (other.eventAttachments != null)
				return false;
		} else if (!eventAttachments.equals(other.eventAttachments))
			return false;
		if (Double.doubleToLongBits(eventCost) != Double.doubleToLongBits(other.eventCost))
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (eventDescription == null) {
			if (other.eventDescription != null)
				return false;
		} else if (!eventDescription.equals(other.eventDescription))
			return false;
		if (eventGradingFormat != other.eventGradingFormat)
			return false;
		if (eventLocation == null) {
			if (other.eventLocation != null)
				return false;
		} else if (!eventLocation.equals(other.eventLocation))
			return false;
		if (eventType != other.eventType)
			return false;
		if (exceededEmployeesAvailableFunds == null) {
			if (other.exceededEmployeesAvailableFunds != null)
				return false;
		} else if (!exceededEmployeesAvailableFunds.equals(other.exceededEmployeesAvailableFunds))
			return false;
		if (finalReport == null) {
			if (other.finalReport != null)
				return false;
		} else if (!finalReport.equals(other.finalReport))
			return false;
		if (id != other.id)
			return false;
		if (isUrgent != other.isUrgent)
			return false;
		if (justification == null) {
			if (other.justification != null)
				return false;
		} else if (!justification.equals(other.justification))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (Double.doubleToLongBits(passingGrade) != Double.doubleToLongBits(other.passingGrade))
			return false;
		if (preApprovalEmail == null) {
			if (other.preApprovalEmail != null)
				return false;
		} else if (!preApprovalEmail.equals(other.preApprovalEmail))
			return false;
		if (preApprovalType != other.preApprovalType)
			return false;
		if (Double.doubleToLongBits(projectedAmount) != Double.doubleToLongBits(other.projectedAmount))
			return false;
		if (proofOfPassing == null) {
			if (other.proofOfPassing != null)
				return false;
		} else if (!proofOfPassing.equals(other.proofOfPassing))
			return false;
		if (status != other.status)
			return false;
		if (workTimeMissed == null) {
			if (other.workTimeMissed != null)
				return false;
		} else if (!workTimeMissed.equals(other.workTimeMissed))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", employee=" + employee + ", assignedTo=" + assignedTo + ", eventDate="
				+ eventDate + ", eventLocation=" + eventLocation + ", eventDescription=" + eventDescription
				+ ", eventCost=" + eventCost + ", eventGradingFormat=" + eventGradingFormat + ", eventAttachments="
				+ eventAttachments + ", eventType=" + eventType + ", passingGrade=" + passingGrade + ", justification="
				+ justification + ", status=" + status + ", dateSubmitted=" + dateSubmitted + ", dateUpdated="
				+ dateUpdated + ", preApprovalEmail=" + preApprovalEmail + ", preApprovalType=" + preApprovalType
				+ ", isUrgent=" + isUrgent + ", proofOfPassing=" + proofOfPassing + ", projectedAmount="
				+ projectedAmount + ", awardedAmount=" + awardedAmount + ", workTimeMissed=" + workTimeMissed
				+ ", message=" + message + ", exceededEmployeesAvailableFunds=" + exceededEmployeesAvailableFunds
				+ ", finalReport=" + finalReport + "]";
	}

}
