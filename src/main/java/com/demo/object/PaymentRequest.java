package com.demo.object;

public class PaymentRequest {

	private double amount;
	private String subsType;
    private String subsDay;
    private String startDate;
    private String endDate;
    
    public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getSubsType() {
		return subsType;
	}

	public void setSubsType(String subsType) {
		this.subsType = subsType;
	}

	public String getSubsDay() {
		return subsDay;
	}

	public void setSubsDay(String subsDay) {
		this.subsDay = subsDay;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
