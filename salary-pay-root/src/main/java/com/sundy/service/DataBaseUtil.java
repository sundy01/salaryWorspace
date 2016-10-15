package com.sundy.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataBaseUtil {
	
	private  static ApplicationContext applicationContext;
	
	private  static EmployeeInfoService employeeInfoService;
	
	private static ClotheStyleService clotheStyleService;
	
	private static ClotheProcessService processService;
	
	private static ProcessFinishNumService processFinishNumService;
	
	private static PayMoneyService payMoneyService;
	
	private static ExportReportExcelService exportReportExcelService;
	
	
	public static ApplicationContext getApplicationContext(){
		
		 if(applicationContext==null){
			 applicationContext=new ClassPathXmlApplicationContext(new String[] {"datasourceConfig.xml"});
		 }
		return applicationContext;
	}
	
	
	public static EmployeeInfoService getEmployeeInfoService(){
		 if(employeeInfoService==null){
			 employeeInfoService=(EmployeeInfoService) getApplicationContext().getBean("employeeInfoService");
		 }
		return employeeInfoService;
	}
	
	public static ClotheStyleService getClotheStyleService(){
		 if(clotheStyleService==null){
			 clotheStyleService=(ClotheStyleService) getApplicationContext().getBean("clotheStyleService");
		 }
		return clotheStyleService;
	}
	
	
	public static ClotheProcessService getClotheProcessService(){
		 if(processService==null){
			 processService=(ClotheProcessService) getApplicationContext().getBean("clotheProcessService");
		 }
		return processService;
	}
	
	
	public static ProcessFinishNumService getProcessFinishNumService(){
		 if(processFinishNumService==null){
			 processFinishNumService=(ProcessFinishNumService) getApplicationContext().getBean("processFinishNumService");
		 }
		return processFinishNumService;
	}
	
	public static PayMoneyService getPayMoneyService(){
		 if(payMoneyService==null){
			 payMoneyService=(PayMoneyService) getApplicationContext().getBean("payMoneyService");
		 }
		return payMoneyService;
	}
	
	
	public static ExportReportExcelService getExportReportExcelService(){
		 if(exportReportExcelService==null){
			 exportReportExcelService=(ExportReportExcelService) getApplicationContext().getBean("exportReportExcelService");
		 }
		return exportReportExcelService;
		
	}
	
	
	

}
