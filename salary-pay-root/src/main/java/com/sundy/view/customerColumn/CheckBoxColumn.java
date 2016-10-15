package com.sundy.view.customerColumn;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class CheckBoxColumn extends AbstractCellEditor implements
		TableCellEditor, TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel renderPanel;
	private JPanel editorPanel;
	private JCheckBox renderCheckBox;
	private JCheckBox editorCheckBox;
	

	public CheckBoxColumn(){
		this.renderPanel=new JPanel();
		this.editorPanel=new JPanel();
		this.renderCheckBox=new JCheckBox();
		this.editorCheckBox=new JCheckBox();
		
		this.renderPanel.setLayout(null);
		this.editorPanel.setLayout(null);
		
		this.renderCheckBox.setBounds(50, 2, 50, 12);
		this.editorCheckBox.setBounds(50, 2, 50, 12);
		
		this.renderPanel.add(this.renderCheckBox);
		
		this.editorPanel.add(this.editorCheckBox);
	}
	
	
	@Override
	public Object getCellEditorValue() {
		return editorCheckBox.isSelected(); 
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(value!=null){
			renderCheckBox.setSelected(Boolean.valueOf(value.toString())); 
		}else{
			renderCheckBox.setSelected(true); 
		}
		
		return renderPanel;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		if(value!=null){
			this.editorCheckBox.setSelected((Boolean)value); 
		}else{
			this.editorCheckBox.setSelected(true); 
		}
		
		return this.editorPanel;
	}

}
