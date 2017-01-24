package com.sundy.dao;

import java.util.List;
import java.util.Map;

import com.sundy.domain.Item;
import com.sundy.domain.ProcessBean;
import com.sundy.domain.StyleItem;

public interface ClotheProcessDao {
	
	public List<ProcessBean> queryProcessBean(Map<String,Object> map);
	
	
	public List<ProcessBean> queryProcessBeanByStyleCode(Map<String,Object> map);
	
	public Integer queryCount(Map<String,Object> map);

	
	public void update(ProcessBean style);
	
	public void insert(ProcessBean style);
	
	public void delete(Integer id);
	
	public List<StyleItem> queryAllStyle();
	
	public ProcessBean getProcessBeanById(Integer id);
	
	public void deleteUserFinishNum(Integer id);
	
	
	public List<ProcessBean> queryProcessBeanByStyleId(Integer styleId);
	
}
