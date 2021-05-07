package com.revature.models;

public enum EventType {
	UNIVERSITY_COURSE(0.8),
	SEMINAR(0.6),
	CERT_PREP_CLASS(.75),
	CERTIFICATION(1),
	TECHNICAL_TRAINING(.9),
	OTHER(.3);
	
	private double percent;
	
	EventType(double percent){
		this.percent = percent;
	}
	
	public double getPercent() {
		return percent;
	}
}
