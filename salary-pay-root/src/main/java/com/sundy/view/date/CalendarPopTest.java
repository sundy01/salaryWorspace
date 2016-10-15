package com.sundy.view.date;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CalendarPopTest implements DayClickListener,DayCompCreateListener{

	 public static void main(String[] args){  
	        new CalendarPopTest().startUp();  
	    }  
	      
	    public void startUp(){  
	        JFrame frame = new JFrame();  
	        frame.setSize(new Dimension(200,300));  
	        frame.setLocation(400, 500);  
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	          
	        JPanel panel = new JPanel();  
	        final JButton button = new JButton("TestButton");  
	        button.addMouseListener(new MouseAdapter() {  
	            @Override  
	            public void mouseClicked(MouseEvent e) {  
	                CalendarPop calendarPop  = new CalendarPop();  
	                calendarPop.addDayClickListener(CalendarPopTest.this);  
	                calendarPop.addDayCreateListener(CalendarPopTest.this);  
	                calendarPop.updateDate(Calendar.getInstance());  
	                calendarPop.show(button, e.getX(), e.getY());  
	            }  
	        });  
	        panel.add(button);  
	        frame.add(panel);  
	        frame.setVisible(true);  
	    }  
	  
	    /** 
	     * 这里可以做些操作 
	     */  
	    @Override  
	    public void dayClicked(DayPanel day, MouseEvent e) {  
	    	
	    	Calendar current= day.getCalendar();
	    	Date date=current.getTime();
	    	
	    	SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
	    	
	    	String value=formate.format(date);
	    	
	    	System.out.println(value);
	    }  
	  
	    /** 
	     * 这里可以做些操作 
	     */  
	    @Override  
	    public void dayCompCreated(DayPanel day) {  
	        System.out.println("day created");  
	    }  

}
