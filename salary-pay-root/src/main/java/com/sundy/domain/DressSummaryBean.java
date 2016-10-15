package com.sundy.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DressSummaryBean {
	private String payId;
	private String employeeId;
	private String employeeName;
	private String styleId;
	private String styleName;
	private String processId;
	private String processName;
	private BigDecimal processPrice;
	private int workSum;
	private BigDecimal paySum;
	private Date payDate;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public BigDecimal getProcessPrice() {
		return processPrice;
	}
	public void setProcessPrice(BigDecimal processPrice) {
		this.processPrice = processPrice;
	}
	public int getWorkSum() {
		return workSum;
	}
	public void setWorkSum(int workSum) {
		this.workSum = workSum;
	}
	public BigDecimal getPaySum() {
		return paySum;
	}
	public void setPaySum(BigDecimal paySum) {
		this.paySum = paySum;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	
	
	
	

}
