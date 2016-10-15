package com.sundy.view;

import java.awt.EventQueue;

import com.sundy.view.panel.MainControllerPanel;

public class MainFrame {

	public static void main(String[] args) {
		
		try {
			//加载面板
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					MainControllerPanel frame = new MainControllerPanel();
				}
			});
			
			
	        
		} catch (Exception e) {
		     e.printStackTrace();
		}

	}

}
