package com.revature.models;

public enum GradingFormat {
	PERCENTAGE(.7), PASS_FAIL(1), PRESENTATION(1);

	private double passPercentage;
	
	GradingFormat(double i) {
		this.passPercentage = i;
	}
	
	public double getPassPercentage() {
		return this.passPercentage;
	}
}
