package com.sundy.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sundy.dao.PayMoneyDao;
import com.sundy.domain.PayMoneyBean;



@Component
public class PayMoneyService {
	@Autowired
	private PayMoneyDao payMoneyDao;
	
	
	public Vector queryPayMoneyBean(Map<String,Object> map){
      
		List<PayMoneyBean> list=this.payMoneyDao.queryPayMoneyBean(map);
		Vector dataList=new Vector();
		
		for(PayMoneyBean payBean : list){
			Vector dataRow=new Vector();
			dataRow.add(payBean.getId());
			dataRow.add(payBean.getStyleCode());
			dataRow.add(payBean.getEmployeeName());
			dataRow.add(payBean.getProcessName());
			dataRow.add(payBean.getProcessPrice());
			dataRow.add(payBean.getFinishNum());
			
			Date createDate=payBean.getCreateDate();
			if(createDate!=null){
				String date=getDateFormate(createDate);
				dataRow.add(date);
			}else{
				dataRow.add("");
			}
			dataRow.add(payBean.getPayMoney());
			dataList.add(dataRow);
		}
		
		return dataList;
	}
	
	private String getDateFormate(Date date){
		SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String value=formate.format(date);
		return value;
	}
	
	
	
	public int queryAllRecord(Map<String,Object> paramterMap){
		
		return this.payMoneyDao.queryCount(paramterMap);
	}
	
	

}
