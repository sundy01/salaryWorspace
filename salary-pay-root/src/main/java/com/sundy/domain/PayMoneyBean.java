package com.sundy.domain;

import java.math.BigDecimal;
import java.util.Date;

public class PayMoneyBean {
	private Integer id;
	private String styleCode;
	private String processName;
	private String employeeName;
	private BigDecimal processPrice;
	private Integer finishNum;
	private BigDecimal payMoney; //支付的现金
    private Date createDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStyleCode() {
		return styleCode;
	}
	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public BigDecimal getProcessPrice() {
		return processPrice;
	}
	public void setProcessPrice(BigDecimal processPrice) {
		this.processPrice = processPrice;
	}
	public Integer getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(Integer finishNum) {
		this.finishNum = finishNum;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    
    
	

}
