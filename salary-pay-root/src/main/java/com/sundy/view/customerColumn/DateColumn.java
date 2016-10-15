package com.sundy.view.customerColumn;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class DateColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	private JLabel label;
	private DatePickPanel datePickPanel;
	
	
	public DateColumn(){
		this.label=new JLabel();
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.datePickPanel=new DatePickPanel();
	}

	@Override
	public Object getCellEditorValue() {
		return datePickPanel.getFile(); 
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(value!=null)
		{
			label.setText(value.toString());
		}else{
			this.label.setText("");
		}
		return label; 
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		   if(value!=null){
			   datePickPanel.setFile(value.toString());
		   }else{
			   this.datePickPanel.setFile("");
		   }
		   return datePickPanel; 
	}

}
