package com.sundy.dao;

import com.sundy.domain.LoginUserBean;

public interface LoginUserDao {
	
	public LoginUserBean queryLoginUserBeanByUsername(String username);
	
}
