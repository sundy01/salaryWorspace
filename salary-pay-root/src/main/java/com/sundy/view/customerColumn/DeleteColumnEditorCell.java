package com.sundy.view.customerColumn;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class DeleteColumnEditorCell extends AbstractCellEditor implements TableCellEditor ,ActionListener{
	  private JPanel panel;
      private JButton editorButton;
   	
      
      public DeleteColumnEditorCell()
      {
          this.initButton();

          this.initPanel();

          // 添加按钮。
          this.panel.add(this.editorButton);
      }

       private void initPanel() {
	     this.panel = new JPanel();
	     // panel使用绝对定位，这样button就不会充满整个单元格。
	     this.panel.setLayout(null);
			
		}

		private void initButton() {
			
			this.editorButton = new JButton();
			 // 设置按钮的大小及位置。
			this.editorButton.setBounds(50,2,80,20);
					
		}
      

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editorButton.setText("删除");
		return this.panel;
	}

}
