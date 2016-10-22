package com.sundy.view;

import java.awt.EventQueue;

import com.sundy.view.panel.LoginPanel;

public class MainFrame {

	public static void main(String[] args) {
		
		try {
			//加载面板
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					LoginPanel panel=new LoginPanel();
				}
			});
			
			
	        
		} catch (Exception e) {
		     e.printStackTrace();
		}

	}

}
