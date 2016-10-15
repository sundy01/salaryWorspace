package com.sundy.dataModel;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class DataTableModel extends DefaultTableModel {
	
	public DataTableModel(Vector data, Vector columnNames){
		super(data, columnNames);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}


}
