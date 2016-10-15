package com.sundy.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sundy.dao.ProcessFinishNumDao;
import com.sundy.domain.ProcessFinishNumBean;
import com.sundy.domain.StyleItem;



@Component
public class ProcessFinishNumService {
	@Autowired
	private ProcessFinishNumDao processFinishNumDao;
	
	
	public Vector queryProcessFinishNumByProcessId(Map<String,Object> map){
      
		List<ProcessFinishNumBean> list=this.processFinishNumDao.queryProcessFinishNumBean(map);
		Vector dataList=new Vector();
		
		for(ProcessFinishNumBean finishBean : list){
			Vector dataRow=new Vector();
			dataRow.add(finishBean.getId());
			dataRow.add(finishBean.getEmployeeId());
			dataRow.add(finishBean.getFinishNum());
			dataList.add(dataRow);
		}
		
		return dataList;
	}
	
	
	
	public int queryAllRecord(Map<String,Object> paramterMap){
		
		return this.processFinishNumDao.queryCount(paramterMap);
	}
	
	@Transactional
	public void saveOrUpdateProcessFinishNumBean(List<ProcessFinishNumBean> list){
		  if(list!=null && list.size()>0){
			  for(ProcessFinishNumBean styleBean : list){
				  Integer id=styleBean.getId();
				  if(id!=null){
					  styleBean.setUpdateDate(new Date());
					  this.processFinishNumDao.update(styleBean);
				  }else{
					  styleBean.setCreateDate(new Date());
					  styleBean.setUpdateDate(new Date());
					  this.processFinishNumDao.insert(styleBean);
				  }
			  }
		  }
	}
	
	@Transactional
	public boolean deleteRowData(JTable table,int[] selectRows){
		
		boolean flag=false;
		
		if(selectRows.length>0){
			
			for(int row : selectRows){
				
				Integer currentValue=(Integer) table.getValueAt(row,0);
				
				this.processFinishNumDao.delete(currentValue);
				
			}
			
			flag=true;
		}
		
		return flag;
	}
	
	
}
