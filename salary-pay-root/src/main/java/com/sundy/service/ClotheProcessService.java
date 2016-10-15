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

import com.sundy.dao.ClotheProcessDao;
import com.sundy.domain.ProcessBean;
import com.sundy.domain.StyleItem;



@Component
public class ClotheProcessService {
	@Autowired
	private ClotheProcessDao clotheProcessDao;
	
	
	public ProcessBean getProcessBeanById(Integer id){
		return this.clotheProcessDao.getProcessBeanById(id);
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
