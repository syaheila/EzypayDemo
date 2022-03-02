package com.demo.object;

public class BaseResponse {

	private String status;
    private Integer code;
    private double amount;
	private String subsType;
    private String invoiceDates;
    
    public String getStatus() {
        return status;
    }
  
    public void setStatus(String status) {
        this.status = status;
    }
  
    public Integer getCode() {
        return code;
    }
  
    public void setCode(Integer code) {
        this.code = code;
    }
    
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

	public String getInvoiceDates() {
		return invoiceDates;
	}

	public void setInvoiceDates(String invoiceDates) {
		this.invoiceDates = invoiceDates;
	}
}
