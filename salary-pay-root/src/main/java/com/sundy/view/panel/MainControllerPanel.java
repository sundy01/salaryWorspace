package com.sundy.view.panel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

public class MainControllerPanel extends JFrame {
	private static final Logger log=Logger.getLogger(MainControllerPanel.class);
	
	public MainControllerPanel() {
		
		 this.setTitle("正浩服饰结算工资");
		
		EmployeeInfoPanel employeeInfoPanel=new EmployeeInfoPanel();
		ClotheStylePanel clotheStylePanel=new ClotheStylePanel();
		
	    EmployeeFinishClothePanel finishPanel=new EmployeeFinishClothePanel();
		
		PayMoneyPanel payMoneyPanel=new PayMoneyPanel();
		
		
		JFrame jf=new JFrame();
	    JTabbedPane jtp=new JTabbedPane();
	    
	    jf.getContentPane().add(jtp);
	    jtp.addTab("员工信息", null,employeeInfoPanel.getComponent(0), null);
	    jtp.addTab("款式信息", null,clotheStylePanel.getComponent(0), null);
	    jtp.addTab("完成件数", null,finishPanel.getComponent(0), null);
	    jtp.addTab("结算工资", null,payMoneyPanel.getComponent(0), null);
	    
	    jf.setBounds(300, 10, 1000, 705);
	    jf.setResizable(false);
	    jf.setVisible(true);
	    
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
	    
	}
}
