package com.sundy.view.comboRender;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.sundy.domain.StyleItem;

public class ItemRenderer extends BasicComboBoxRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList list, Object value, int index,
	        boolean isSelected, boolean cellHasFocus) 
	     {
	      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	      if (value != null) {
	    	  StyleItem item = (StyleItem) value;
	            setText(item.getValue());
	      }

	      System.out.println("index=="+index);
	      if (index==-1) {
	          setText("");
	      }else{
	    	  StyleItem item = (StyleItem) value;
	          setText("" + item.getValue());
	      }
	      return this;
	    }

}
