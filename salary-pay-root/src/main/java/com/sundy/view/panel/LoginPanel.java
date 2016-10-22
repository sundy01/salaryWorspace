package com.sundy.view.panel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.util.StringUtils;

import com.sundy.domain.LoginUserBean;
import com.sundy.service.DataBaseUtil;
import com.sundy.service.LoginUserService;

import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginPanel extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private MainControllerPanel mainFrame;
	public LoginPanel() {
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("账号:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		label.setBounds(195, 121, 89, 18);
		getContentPane().add(label);
		
		usernameField = new JTextField();
		usernameField.setBounds(288, 121, 152, 21);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		JLabel label_1 = new JLabel("密码:");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("宋体", Font.PLAIN, 15));
		label_1.setBounds(205, 170, 67, 15);
		getContentPane().add(label_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(288, 167, 152, 21);
		getContentPane().add(passwordField);
		
		loginButton = new JButton("登录");
		loginButton.setBounds(191, 310, 93, 23);
		loginButton.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		getContentPane().add(loginButton);
		
		loginButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 if(mainFrame!=null){
					 loginButton.setEnabled(false);
				 }else{
					 loginButton.setEnabled(true);
					 saveButtonEvent();
					 loginButton.setEnabled(false);
					 
				 }
			    
			}
		});
		
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonEvent();
			}
		});
		
		JButton cancelButton = new JButton("取消");
		cancelButton.setBounds(404, 310, 93, 23);
		getContentPane().add(cancelButton);
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				cancelButtonEvent();
			}
		});
		
		JLabel lblNewLabel = new JLabel("正浩服饰结算工资系统");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel.setBounds(195, 24, 315, 40);
		getContentPane().add(lblNewLabel);
		
	    this.setBounds(300, 100, 700, 500);
	    this.setResizable(false);
	    this.setVisible(true);
	    
	    this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void saveButtonEvent(){
		
		String username=this.usernameField.getText();
		String password=this.passwordField.getText();
		if(StringUtils.hasText(username) && StringUtils.hasText(password)){
			
			LoginUserService userService=DataBaseUtil.getLoginUserService();
			boolean flag=userService.loginCheckUsername(username,password);
			if(flag){
				
				mainFrame = new MainControllerPanel();
				mainFrame.setVisible(true);
				
				this.setVisible(false);
				
			}else{
				JOptionPane.showMessageDialog(null,"密码输入错误!!");
			}
			
		}else{
			JOptionPane.showMessageDialog(null,"请输入账号和密码");
		}
		
		
		
	}
	
	private void cancelButtonEvent(){
		this.usernameField.setText("");
		this.passwordField.setText("");
	}
}
