package com.sundy.view.customerColumn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sundy.view.date.CalendarPop;
import com.sundy.view.date.DayClickListener;
import com.sundy.view.date.DayCompCreateListener;
import com.sundy.view.date.DayPanel;

public class DatePickPanel extends JPanel implements DayClickListener,DayCompCreateListener {
	private JTextField textFieldFile;
	private JButton   btnFile;
	
	
	
	
	public DatePickPanel(){
		this.textFieldFile=new JTextField();
		this.btnFile=new JButton("选择日期");
		 this.setLayout(new BorderLayout());
		 this.add(textFieldFile);
		 this.add(btnFile,BorderLayout.EAST);
		 this.textFieldFile.setEditable(false);
		 btnFile.setPreferredSize(new Dimension(20,getHeight())); 
		 
		 btnFile.addMouseListener(new MouseAdapter() {  
	            @Override  
	            public void mouseClicked(MouseEvent e) {  
	                CalendarPop calendarPop  = new CalendarPop();  
	                calendarPop.addDayClickListener(DatePickPanel.this);  
	                calendarPop.addDayCreateListener(DatePickPanel.this);  
	                calendarPop.updateDate(Calendar.getInstance());  
	                calendarPop.show(btnFile, e.getX(), e.getY());  
	            }  
	        }); 
	}
	
	
	public void setFile(String file){
		this.textFieldFile.setText(file);
	}
	
	public String getFile(){
		return this.textFieldFile.getText();
	}


	@Override
	public void dayCompCreated(DayPanel day) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dayClicked(DayPanel day, MouseEvent e) {
		Calendar current= day.getCalendar();
    	Date date=current.getTime();
    	
    	SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
    	
    	String value=formate.format(date);
    	this.setFile(value);
		
	}

}
