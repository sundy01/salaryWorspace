package com.sundy.dao;

import java.util.List;
import java.util.Map;

import com.sundy.domain.ClotheStyleBean;

public interface ClotheStyleDao {
	
	public List<ClotheStyleBean> queryClotheStyleBean(Map<String,Object> map);
	
	public Integer queryCount(Map<String,Object> map);

	
	public void update(ClotheStyleBean style);
	
	public void insert(ClotheStyleBean style);
	
	public void delete(Integer id);
	
	
	public ClotheStyleBean getClotheStyleBean(Integer id);
	
	public void deleteProcessByStyleId(Integer styleId);
	
	public void deleteFinishNumByStyleId(Integer styleId);
}
