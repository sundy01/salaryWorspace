package com.sundy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sundy.dao.ClotheStyleDao;
import com.sundy.domain.ClotheStyleBean;



@Component
public class ClotheStyleService {
	@Autowired
	private ClotheStyleDao clotheStyleDao;
	
	
	public ClotheStyleBean getClotheStyleBeanById(Integer id){
		return this.clotheStyleDao.getClotheStyleBean(id);
	}
	
	
	public Vector queryClotheStyleData(Map<String,Object> paramterMap){
		
		
		List<ClotheStyleBean> list=this.clotheStyleDao.queryClotheStyleBean(paramterMap);
		
		Vector dataList=new Vector();
		
		for(ClotheStyleBean styleBean : list){
			Vector dataRow=new Vector();
			dataRow.add(styleBean.getId());
			dataRow.add(styleBean.getStyleName());
			dataRow.add(styleBean.getStyleCode());
			dataRow.add(styleBean.getStyleNum());
			dataList.add(dataRow);
		}
		
		return dataList;
		
	}
	
	public int queryAllRecord(Map<String,Object> paramterMap){
		
		return this.clotheStyleDao.queryCount(paramterMap);
	}
	
	@Transactional
	public void saveOrUpdateClotheStyleBean(List<ClotheStyleBean> list){
		  if(list!=null && list.size()>0){
			  for(ClotheStyleBean styleBean : list){
				  Integer id=styleBean.getId();
				  if(id!=null){
					  styleBean.setUpdateDate(new Date());
					  this.clotheStyleDao.update(styleBean);
				  }else{
					  styleBean.setCreateDate(new Date());
					  styleBean.setUpdateDate(new Date());
					  this.clotheStyleDao.insert(styleBean);
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
				
				this.clotheStyleDao.delete(currentValue);
				
				//删除款式的时候删除掉对应的工序和员工完成改工序的件数
				this.clotheStyleDao.deleteFinishNumByStyleId(currentValue);
				this.clotheStyleDao.deleteProcessByStyleId(currentValue);
			
				
			}
			
			flag=true;
		}
		
		return flag;
	}
	
	
	

}
