package com.sundy.service;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sundy.dao.StudentDao;
import com.sundy.domain.Student;
import com.sundy.domain.StyleItem;



@Component
public class EmployeeInfoService {
	@Autowired
	private StudentDao studentDao;
	
	public  Vector<StyleItem> queryAllStudentConvertItem(){
		List<Student> list=this.studentDao.loadAllStudent();
		
		 Vector<StyleItem> itemList=new  Vector<StyleItem>();
		
		if(list!=null && list.size()>0){
			
			for(Student student : list){
				Integer id=student.getId();
				String name=student.getName();
				
				StyleItem item=new StyleItem();
				item.setKey(id);
				item.setValue(name);
				
				itemList.add(item);
			}
			
		}
		
		return itemList;
	}
	
	
	public Vector queryStudentData(Map<String,Object> paramterMap){
		
		
		List<Student> list=this.studentDao.queryStudent(paramterMap);
		
		Vector dataList=new Vector();
		
		for(Student student : list){
			Vector dataRow=new Vector();
			dataRow.add(student.getId());
			dataRow.add(student.getName());
			dataRow.add(student.getGender());
			dataRow.add(student.getBirthday());
			dataRow.add(student.getPhone());
			dataList.add(dataRow);
		}
		
		return dataList;
		
	}
	
	public int queryAllRecord(Map<String,Object> paramterMap){
		
		return this.studentDao.queryCount(paramterMap);
	}
	
	@Transactional
	public void saveOrUpdateStudent(List<Student> list){
		  if(list!=null && list.size()>0){
			  for(Student student : list){
				  Integer id=student.getId();
				  if(id!=null){
					  this.studentDao.update(student);
				  }else{
					  this.studentDao.insert(student);
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
				
				this.studentDao.delete(currentValue);
				this.studentDao.deleteFinishNumById(currentValue);
				
			}
			
			flag=true;
		}
		
		return flag;
	}
	
	
	

}
