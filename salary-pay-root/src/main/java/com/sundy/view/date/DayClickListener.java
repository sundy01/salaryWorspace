package com.sundy.view.date;

import java.awt.event.MouseEvent;

public interface DayClickListener {
	 /** 
     * 一个日期组件被点击事件 
     * @param day 
     */  
    public  void dayClicked(DayPanel day,MouseEvent e);  
}
