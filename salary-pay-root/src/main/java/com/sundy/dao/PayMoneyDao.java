package com.sundy.dao;

import java.util.List;
import java.util.Map;

import com.sundy.domain.PayMoneyBean;

public interface PayMoneyDao {
	
	public List<PayMoneyBean> queryPayMoneyBean(Map<String,Object> map);
	
	public Integer queryCount(Map<String,Object> map);
	
	
}
