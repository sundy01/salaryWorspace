package com.sundy.view.customerColumn;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.springframework.util.StringUtils;

public class MoneyColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	
	private JLabel renderLabel;
	
	private JTextField editorTextField;
	
	private double tempValue;
	
	
     public MoneyColumn(){
    	 this.renderLabel=new JLabel();
    	 this.renderLabel.setOpaque(true);
    	 this.renderLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    	 this.editorTextField=new JTextField();
     }
     
     
     @Override
    public boolean shouldSelectCell(EventObject anEvent) {
    	 // 当单元格进入编辑状态前执行此处代码
    	 // 将单元格内的字符保存成double类型的变量，以便在输入错误时恢复输入之前时的值 
    	 String editorValue=editorTextField.getText();
    	 if(StringUtils.hasText(editorValue)){
    		 try{
    			 tempValue = Double.valueOf(editorValue);
    		 }catch(Exception es){
    			 System.out.println(es.getMessage());
    		 }
    	 }else{
    		 tempValue=0;
    	 }
    	
    	return super.shouldSelectCell(anEvent);
    }

	@Override
	public Object getCellEditorValue() {
		Double price=null;
		try{
			price=Double.valueOf(this.editorTextField.getText());
		}catch(Exception es){
			JOptionPane.showConfirmDialog(null, "输入的数据有误，有输入数值型数据！","格式错误",JOptionPane.ERROR_MESSAGE); 
			price = tempValue; 
		}
		finally{
			return price;
		}
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(value!=null){
			this.renderLabel.setText(value.toString()); 
		}else{
			this.renderLabel.setText("0"); 
		}
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
