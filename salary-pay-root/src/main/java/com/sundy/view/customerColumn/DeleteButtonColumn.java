package com.sundy.view.customerColumn;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class DeleteButtonColumn extends AbstractCellEditor implements TableCellEditor,TableCellRenderer ,ActionListener{
	private JButton renderButton;
	private JButton editorButton;
	private DefaultTableModel defaultTableMode;
	private int row; 
	private JPanel renderPanel;
	private JPanel editorPanel;
	
	
	public DeleteButtonColumn(){
		renderButton = new JButton();
		editorButton = new JButton();
		editorButton.setFocusable(false);
		editorButton.addActionListener(this);
		
		this.renderPanel=new JPanel();
		this.editorPanel=new JPanel();
		
		this.renderPanel.setLayout(null);
		this.editorPanel.setLayout(null);
		
		this.renderButton.setBounds(50,2,80,20);
		this.editorButton.setBounds(50,2,80,20);
		
		this.renderPanel.add(renderButton);
		this.editorPanel.add(editorButton);
		
		
	}

	@Override
	public Object getCellEditorValue() {
		//TODO 当单元格的编辑状态结束后调用此方法内的代码 
		return null;
	}
	
	

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO
		return super.shouldSelectCell(anEvent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.fireEditingStopped();//立即终止此单元格的编辑状态，使表格模型可以自动更新
	    //defaultTableMode.removeRow(this.row); 
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// TODO 当单元格处于展示状态时
		renderButton.setText("删除");
		return this.renderPanel; 
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editorButton.setText("删除");
		defaultTableMode = (DefaultTableModel)table.getModel();
		this.row = row;
		return this.editorPanel; 
	}

}
