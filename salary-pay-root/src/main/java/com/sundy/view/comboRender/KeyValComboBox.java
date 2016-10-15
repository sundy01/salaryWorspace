package com.sundy.view.comboRender;

import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.sundy.domain.Item;
import com.sundy.domain.StyleItem;

public class KeyValComboBox extends JComboBox{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KeyValComboBox(Vector values){
		super(values);
		rendererData();
	}
	
	public void rendererData(){
		ListCellRenderer render=new DefaultListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus)
			{
				super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
				if (value instanceof StyleItem){  
					StyleItem item = (StyleItem) value;  
                    this.setText(item.getValue());  
               }  
               return this; 
			}
		};
		
		 this.setRenderer(render);  
	}
	
	//修改Combox中的数据  
	public void updateData(Vector values){  
         setModel(new DefaultComboBoxModel(values));  
         rendererData();  
    }  
	
	public void setSelectedItem(Object anObject){ //选中text与传入的参数相同的项  
        if (anObject != null){  
             if (anObject instanceof StyleItem){  
                   super.setSelectedItem(anObject);  
             }  
             if(anObject instanceof String){  
                   for (int index = 0; index < getItemCount(); index++){  
                	      StyleItem po = (StyleItem) getItemAt(index);  
                          if (po.getValue().equals(anObject.toString())){  
                                 super.setSelectedIndex(index);  
                          }  
                   }  
             }  
        }else{  
               super.setSelectedItem(anObject);  
        }  
    }  
	
	public void setSelectedValue(Object anObject){ //选中value与传入的参数相同的项  
        if(anObject != null){  
            if(anObject instanceof StyleItem){  
                  super.setSelectedItem(anObject);  
            }  
            if(anObject instanceof String){  
                  for(int index = 0; index < getItemCount(); index++){  
                	   StyleItem po = (StyleItem) getItemAt(index);  
                        if(po.getKey().equals(anObject.toString())){  
                           super.setSelectedIndex(index);  
                        }  
                  }  
            }  
        }else{  
             super.setSelectedItem(anObject);  
        }  
    }  
	
	
	  //获得选中项的键值  
    public Integer getSelectedValue(){  
           if(getSelectedItem() instanceof StyleItem){  
        	   StyleItem po = (StyleItem)this.getSelectedItem(); 
                return po.getKey();
           }  
           return (Integer) ((getSelectedItem() != null) ? getSelectedItem() : null);  
      }  
  
      //获得选中项的显示文本  
    public String getSelectedText(){  
             if(getSelectedItem() instanceof StyleItem){  
            	 StyleItem po = (StyleItem)getSelectedItem();  
                return po.getValue();  
           }  
           return (getSelectedItem() != null) ? getSelectedItem().toString() : null; 
      }  

}
