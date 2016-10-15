package com.sundy.view.customerColumn;

import java.awt.Component;
import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.springframework.util.StringUtils;

public class NumberColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	
    private JLabel renderLabel;
	private JTextField editorTextField;
	private String tempValue;
	
	public NumberColumn(){
		 this.renderLabel=new JLabel();
    	 this.renderLabel.setOpaque(true);
    	 this.renderLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	 this.editorTextField=new JTextField();
	}
	
	 @Override
	 public boolean shouldSelectCell(EventObject anEvent) {
	    	 // 当单元格进入编辑状态前执行此处代码
	    	 // 将单元格内的字符保存成double类型的变量，以便在输入错误时恢复输入之前时的值 
	    	 String editorValue=editorTextField.getText();
	    	 if(StringUtils.hasText(editorValue)){
	    		 try{
	    			 tempValue = editorValue;
	    		 }catch(Exception es){
	    			 System.out.println(es.getMessage());
	    		 }
	    	 }
	    	return super.shouldSelectCell(anEvent);
	    }
	
	
	@Override
	public Object getCellEditorValue() {
		String phoneText="";
		try{
		    phoneText=this.editorTextField.getText();
		    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		    if(StringUtils.hasText(phoneText)){
		    	  Matcher m=pattern.matcher(phoneText);
		    	  if(!m.matches()){
		    		  JOptionPane.showConfirmDialog(null, "输入的数据有误,请输入正确的数值","格式错误",JOptionPane.ERROR_MESSAGE); 
		    		  phoneText=tempValue;
		    	  }
		    }
	      
		}catch(Exception es){
			 JOptionPane.showConfirmDialog(null, "输入的数据有误,请输入正确的数值","格式错误",JOptionPane.ERROR_MESSAGE); 
			 phoneText=tempValue;
		}
		finally{
			return phoneText;
		}
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(value!=null){
			this.renderLabel.setText(value.toString()); 
		}else{
			this.renderLabel.setText(""); 
		}
		renderLabel.setBackground(isSelected ? table.getSelectionBackground():table.getBackground()); 
		return this.renderLabel;
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		if(value!=null){
			this.editorTextField.setText(value.toString());
		}else{
			this.editorTextField.setText("");
		}
		return this.editorTextField;
	}

}
