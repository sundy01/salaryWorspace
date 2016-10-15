package com.sundy.view.customerColumn;

import java.awt.Component;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.sundy.domain.Item;
import com.sundy.view.comboRender.KeyValComboBox;

public class ComboColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	 private JComboBox renderCbo;
	 private JComboBox editorCbo;
	 private JLabel renderLabel;
	 
	 
	 public ComboColumn(Vector<Item> vector){
		 renderCbo = new JComboBox(vector);
		 editorCbo = new JComboBox(vector); 
		 
		 this.renderLabel=new JLabel();
    	 this.renderLabel.setOpaque(true);
    	 this.renderLabel.setHorizontalAlignment(SwingConstants.CENTER);
	 }

	@Override
	public Object getCellEditorValue() {
		Object value=editorCbo.getSelectedItem(); 
		if(value instanceof Item){
			return ((Item)value).getKey();
		}
		return value;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		renderLabel.setBackground(isSelected?table.getSelectionBackground():table.getBackground());
		renderLabel.setForeground(isSelected?table.getSelectionForeground():table.getForeground());
		renderLabel.setBackground(hasFocus?table.getSelectionBackground():table.getBackground());
		
		System.out.println("getTableCellRendererComponent "+value);
		
		if(value==null){
			  renderLabel.setText("");
		}
		
	     for (int index = 0; index < renderCbo.getItemCount(); index++){  
 	             Item po = (Item) renderCbo.getItemAt(index);  
	           if (po.getKey().equals(value)){  
	        	   renderLabel.setText(po.getValue());
	        	   break;
	           }  
            } 
		
		return renderLabel; 
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		System.out.println("getTableCellEditorComponent"+value);
		
		 for (int index = 0; index < editorCbo.getItemCount(); index++){  
             Item po = (Item) editorCbo.getItemAt(index);  
             
          if (po.getKey().equals(value)){  
       	      this.editorCbo.setSelectedItem(po);
       	      break;
          }  
       } 
	 return editorCbo; 
	}

}
