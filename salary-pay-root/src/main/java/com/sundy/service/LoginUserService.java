package com.sundy.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sundy.dao.LoginUserDao;
import com.sundy.domain.LoginUserBean;



@Component
public class LoginUserService {
	private static final Logger log=Logger.getLogger(LoginUserService.class);
	@Autowired
	private LoginUserDao loginUserDao;
	
	
   public boolean loginCheckUsername(String username,String password){
	   boolean flag=false;
	   LoginUserBean user=this.loginUserDao.queryLoginUserBeanByUsername(username);
	   if(user!=null){
		    
		       if(StringUtils.hasText(user.getPassword()) && user.getPassword().equals(password)){
		    	   return true;
		       }else{
		    	   log.error("用户username="+username+"的密码错误!");
		       }
		   
        
	   }else{
		   log.info("username=="+username+"不存在!");
	   }
	   
	   return flag;
   }

}
