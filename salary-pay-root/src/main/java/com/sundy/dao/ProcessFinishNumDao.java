package com.sundy.dao;

import java.util.List;
import java.util.Map;

import com.sundy.domain.ProcessFinishNumBean;

public interface ProcessFinishNumDao {
	
	public List<ProcessFinishNumBean> queryProcessFinishNumBean(Map<String,Object> map);
	
	public Integer queryCount(Map<String,Object> map);

	
	public void update(ProcessFinishNumBean style);
	
	public void insert(ProcessFinishNumBean style);
	
	public void delete(Integer id);
	
}
