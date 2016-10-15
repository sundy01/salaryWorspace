package com.sundy.view.customerColumn;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DeleteColumnRender implements TableCellRenderer {
	        private JPanel panel;
	     	private JButton button;
	     	
	     	
	     	 public DeleteColumnRender()
	         {
	             this.initButton();

	             this.initPanel();

	             // 添加按钮。
	             this.panel.add(this.button);
	         }

	private void initPanel() {
		     this.panel = new JPanel();

	        // panel使用绝对定位，这样button就不会充满整个单元格。
	        this.panel.setLayout(null);
				
			}

	private void initButton() {
		
		this.button = new JButton();

        // 设置按钮的大小及位置。
        this.button.setBounds(50,2,80,20);
				
			}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		    this.button.setText("删除");
	        return this.panel;
	}

}
