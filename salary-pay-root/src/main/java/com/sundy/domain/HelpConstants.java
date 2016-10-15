package com.sundy.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpConstants {
	
	/**
	 * 结算
	 */
	public static final String status_01="01";
	/**
	 * 未结算
	 */
	public static final String status_02="02";
	
	public static final String employeeName="员工姓名";
	public static final String styleName="款式编码";
	public static final String processName="工序名称";
	public static final String processPrice="工序价格";
	public static final String workSum="完成件数";
	public static final String payMoney="结算金额";
	public static final String payDate="结算日期";
	public static final String payStatus="结算状态";
	
	public static String getDateStr(Date date){
		if(date!=null){
			SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
			return formate.format(date);
		}else{
			return "";
		}
		
	}

}
