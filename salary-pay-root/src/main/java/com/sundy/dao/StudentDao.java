package com.sundy.dao;

import java.util.List;
import java.util.Map;

import com.sundy.domain.Student;

public interface StudentDao {
	
	public List<Student> queryStudent(Map<String,Object> map);
	
	public Integer queryCount(Map<String,Object> map);

	
	public void update(Student student);
	
	public void insert(Student student);
	
	public void delete(Integer id);
	
	public List<Student> loadAllStudent();
	
	public void deleteFinishNumById(Integer userId);
	
}
