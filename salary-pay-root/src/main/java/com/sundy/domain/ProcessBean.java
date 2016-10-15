package com.sundy.domain;

import java.math.BigDecimal;
import java.util.Date;

public class ProcessBean {
	private Integer id;
	private Integer styleId;
	private String styleName;
	private String processName;
	private BigDecimal processPrice;
	private Date createDate;
	private Date updateDate;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStyleId() {
		return styleId;
	}
	public void setStyleId(Integer styleId) {
		this.styleId = styleId;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	

}
