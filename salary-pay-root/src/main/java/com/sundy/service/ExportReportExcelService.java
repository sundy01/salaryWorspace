package com.sundy.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.sundy.dao.PayMoneyDao;
import com.sundy.domain.DressSummaryBean;
import com.sundy.domain.HelpConstants;
import com.sundy.domain.PayMoneyBean;

@Component
public class ExportReportExcelService {
	
	@Autowired
	private PayMoneyDao payMoneyDao;
	
	
	public void processExportFile(String fileName,String savePath,Map<String,Object> map) throws Exception{
		
		List<DressSummaryBean> list=this.getEmployeeFinishPayMoney(map);
		
		this.exportReportExcelMessage(fileName, savePath, list);
		
	}
	
	
	private List<DressSummaryBean> getEmployeeFinishPayMoney(Map<String,Object> map){
		
		
		List<DressSummaryBean> dressList=new ArrayList<DressSummaryBean>();
		
		List<PayMoneyBean> list=this.payMoneyDao.queryPayMoneyBean(map);
		if(list!=null && list.size()>0){
			
			for(PayMoneyBean bean : list){
				DressSummaryBean summaryBean=new DressSummaryBean();
				summaryBean.setEmployeeName(bean.getEmployeeName());
				summaryBean.setPayDate(new Date());
				summaryBean.setPayId(bean.getId().toString());
				summaryBean.setPaySum(bean.getPayMoney());
				summaryBean.setProcessName(bean.getProcessName());
				summaryBean.setProcessPrice(bean.getProcessPrice());
				summaryBean.setStyleName(bean.getStyleCode());
				summaryBean.setWorkSum(bean.getFinishNum());
				summaryBean.setStyleId("01");
				summaryBean.setStatus("01");
				dressList.add(summaryBean);
			}
		}
		
		return dressList;
	}
	
	private String[] headNames={"员工姓名","款式编码","工序名称","工序价格","完成件数","结算金额","结算日期","结算状态"};
	
	private void exportReportExcelMessage(String fileName,String filePath,List<DressSummaryBean> list) throws Exception{
		
		String excelFileName=this.getFileName(fileName)+"结算工资单.xls";
		Workbook workbook=this.initExcelFile(excelFileName,list);
		this.createFilePath(filePath, excelFileName, workbook);
		
	}
	
	private String getFileName(String name){
		Date date=new Date();
		long currentDate=date.getTime();
		SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=formate.format(date);
		return name+"-"+dateStr+"-"+currentDate;
	}
	
	private Workbook initExcelFile(String excelName,List<DressSummaryBean> list) throws Exception{
        Workbook excelEntity=null;
      
       	 excelEntity=new HSSFWorkbook();
       	 Sheet sheet=excelEntity.createSheet(excelName);
       	 //创建头信息
       	 Row headRow=sheet.createRow(0);
       	 int j=0;
       	 for(String key : headNames){
       		 headRow.createCell(j).setCellValue(key);
       		 sheet.setColumnWidth(j,4000);
       		 j++;
       	 }
       	 
       	 if(list!=null && list.size()>0){
       		 //设置excel内容
	        	 for(int i=0;i<list.size();i++){
	        		 DressSummaryBean bean=list.get(i);
	        		 Row currentRow=sheet.createRow(i+1);
	        		 this.getCellValue(currentRow, bean);
	        	 }
	        	 
	        	 
	        	 //生成合计值
	        	 int sumRowNum=list.size()+2;
	        	 Row sumRow=sheet.createRow(sumRowNum);
	        	 
       		 this.initSumCellValue(sumRow,list);
       	 }
       	
        return excelEntity;

}
	 private void createFilePath(String filePath,String fileName,Workbook workbook) {
		StringBuffer path=new StringBuffer();
		path.append(filePath).append(File.separator);
		File file=new File(path.toString());
		if(!file.exists()){
			file.mkdir();
		}
		
		path.append(fileName);
		
		File currentFile=new File(path.toString());
		if(!currentFile.exists()){
			try{
				currentFile.createNewFile();
			}catch(Exception es){
				es.printStackTrace();
			}
		}
		FileOutputStream outs=null;
		try {
			
			outs=new FileOutputStream(path.toString());
			workbook.write(outs);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(outs!=null){
				try {
					outs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

private void initSumCellValue(Row sumRow, List<DressSummaryBean> list) {


BigDecimal sumValue=new BigDecimal(0);

int workNumSum=0;

for(DressSummaryBean bean : list){
	BigDecimal cureentValue=bean.getPaySum();
	sumValue=sumValue.add(cureentValue);
	
	int workNum=bean.getWorkSum();
	
	workNumSum=workNumSum+workNum;
	
}

sumRow.createCell(0).setCellValue("合计");
sumRow.createCell(4).setCellValue(workNumSum);
sumRow.createCell(5).setCellValue(sumValue.doubleValue());


}



private void getCellValue(Row row,DressSummaryBean bean){
			for(int i=0;i<headNames.length;i++){
				Cell cell=row.createCell(i);
				String headName=headNames[i];
				this.getCurrentCellValue(cell, headName,bean);
				
			}
}


private void getCurrentCellValue(Cell cell,String headName,DressSummaryBean bean){
	if(HelpConstants.employeeName.equals(headName)){
		cell.setCellValue(bean.getEmployeeName());
		
	}
	if(HelpConstants.styleName.equals(headName)){
		cell.setCellValue(bean.getStyleName());
	}
	if(HelpConstants.processName.equals(headName)){
		cell.setCellValue(bean.getProcessName());
	}
	if(HelpConstants.processPrice.equals(headName)){
		cell.setCellValue(bean.getProcessPrice().doubleValue());
	}
	if(HelpConstants.workSum.equals(headName)){
		cell.setCellValue(bean.getWorkSum());
	}
	if(HelpConstants.payMoney.equals(headName)){
		cell.setCellValue(bean.getPaySum().doubleValue());
	}
	if(HelpConstants.payDate.equals(headName)){
		String payDateStr=HelpConstants.getDateStr(bean.getPayDate());
		cell.setCellValue(payDateStr);
	}
	if(HelpConstants.payStatus.equals(headName)){
		String status=bean.getStatus();
		if(HelpConstants.status_01.equals(status)){
			cell.setCellValue("已经结算");
		}else if(HelpConstants.status_02.equals(status)){
			cell.setCellValue("未结算");
		}
	}
}

}
