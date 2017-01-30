package com.sundy.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sundy.dao.ClotheProcessDao;
import com.sundy.dao.ProcessFinishNumDao;
import com.sundy.dao.StudentDao;
import com.sundy.domain.ProcessBean;
import com.sundy.domain.ProcessFinishNumBean;
import com.sundy.domain.Student;
import com.sundy.domain.StyleItem;



@Component
public class ClotheProcessService {
	private static final Logger log=Logger.getLogger(ClotheProcessService.class);
	@Autowired
	private ClotheProcessDao clotheProcessDao;
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private ProcessFinishNumDao processFinishNumDao;
	
	public List<ProcessFinishNumBean> getProcessFinishNumBeanByMap(Map<String,Object> parameterMap){
		 return this.processFinishNumDao.getProcessFinishNumBeanByMap(parameterMap);
	}
	

	
	public Vector getEmployeeFinishNumData(Vector headData,Integer styleId){
		
		Vector dataList=new Vector();
		
		Vector copyDataList=new Vector();
		
		for(Object object : headData){
			copyDataList.add(object);
			
		}
		
		if(copyDataList!=null && copyDataList.size()>2){
			copyDataList.remove(0);
			copyDataList.remove(0);
		}else{
			return null;
		}
		
		List<Student> studentList=this.studentDao.loadAllStudent();
		if(studentList!=null){
			
		
			
			for(Student student : studentList){
				
				Vector dataRow=new Vector();
				
				Integer employeeId=student.getId();
				String employeeName=student.getName();
				
				dataRow.add(employeeId);
				dataRow.add(employeeName);
				
				Map<String,Object> parameterMap=new HashMap<String,Object>();
				parameterMap.put("styleId", styleId);
				parameterMap.put("employeeId", employeeId);
				
			
				for(Object headValue: copyDataList){
					
					String headValueStr=headValue.toString();
					log.info("headValue=="+headValueStr);
					
					String[] strList=headValueStr.split("\\|");
					
					String processIdStr=strList[0];
					Integer processId=Integer.valueOf(processIdStr);
					parameterMap.put("processId", processId);
					List<ProcessFinishNumBean> finishNumList=this.processFinishNumDao.getProcessFinishNumBeanByMap(parameterMap);
					if(finishNumList!=null && finishNumList.size()==1){
						ProcessFinishNumBean finishNumBean=finishNumList.get(0);
						dataRow.add(finishNumBean.getFinishNum());
					}else{
						dataRow.add(0);
					}
					
					
				}
				
				dataList.add(dataRow);
				
				
			}
		}
		
		return dataList;
	}
	
	public List<ProcessBean> getProcessDataByStyleId(Integer styleId){
		return this.clotheProcessDao.queryProcessBeanByStyleId(styleId);
	}
	
	public ProcessBean getProcessBeanById(Integer processId){
		return this.clotheProcessDao.getProcessBeanById(processId);
	}
	
   	
	public Vector queryClotheProcessDataByStyleCode(String styleCode){
      
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("styleCode", styleCode);
		List<ProcessBean> list=this.clotheProcessDao.queryProcessBeanByStyleCode(map);
		
		Vector dataList=new Vector();
		
		for(ProcessBean styleBean : list){
			Vector dataRow=new Vector();
			dataRow.add(styleBean.getId());
			dataRow.add(styleBean.getStyleName());
			dataRow.add(styleBean.getProcessName());
			dataRow.add(styleBean.getProcessPrice());
			dataList.add(dataRow);
		}
		
		return dataList;
	}
	
	
	public Vector queryClotheProcessData(Map<String,Object> paramterMap){
		
		
		List<ProcessBean> list=this.clotheProcessDao.queryProcessBean(paramterMap);
		
		Vector dataList=new Vector();
		
		for(ProcessBean styleBean : list){
			Vector dataRow=new Vector();
			dataRow.add(styleBean.getId());
			dataRow.add(styleBean.getStyleName());
			dataRow.add(styleBean.getProcessName());
			dataRow.add(styleBean.getProcessPrice());
			dataList.add(dataRow);
		}
		
		return dataList;
		
	}
	
	public int queryAllRecord(Map<String,Object> paramterMap){
		
		return this.clotheProcessDao.queryCount(paramterMap);
	}
	
	@Transactional
	public void saveOrUpdateProcessBean(List<ProcessBean> list){
		  if(list!=null && list.size()>0){
			  for(ProcessBean styleBean : list){
				  Integer id=styleBean.getId();
				  if(id!=null){
					  styleBean.setUpdateDate(new Date());
					  this.clotheProcessDao.update(styleBean);
				  }else{
					  styleBean.setCreateDate(new Date());
					  styleBean.setUpdateDate(new Date());
					  this.clotheProcessDao.insert(styleBean);
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
				
				this.clotheProcessDao.delete(currentValue);
				this.clotheProcessDao.deleteUserFinishNum(currentValue);
				
				
			}
			
			flag=true;
		}
		
		return flag;
	}
	
	
	public Vector queryStyleItem(){
		
		List<StyleItem> list=this.clotheProcessDao.queryAllStyle();
		Vector<StyleItem> vector=new Vector<StyleItem>();
		if(list!=null && list.size()>0){
			for(StyleItem item : list){
				vector.add(item);
			}
		}
		
		return vector;
		
	}
	
	
	

}
