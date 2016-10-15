package com.sundy.view.panel;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.sundy.domain.Item;
import com.sundy.domain.Student;
import com.sundy.service.DataBaseUtil;
import com.sundy.service.EmployeeInfoService;
import com.sundy.view.comboRender.ItemRenderer;
import com.sundy.view.comboRender.KeyValComboBox;
import com.sundy.view.customerColumn.ComboColumn;
import com.sundy.view.customerColumn.DateColumn;
import com.sundy.view.customerColumn.PhoneColumn;

public class EmployeeInfoPanel extends JFrame {
	private static final Logger log=Logger.getLogger(EmployeeInfoPanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=20; //每页显示20条
	private static Vector headData; //列头
	private JButton saveButton; //保存

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeInfoPanel frame = new EmployeeInfoPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	static{
		    headData=new Vector<String>();
			headData.add("编码");
			headData.add("姓名");
			headData.add("性别");
			headData.add("出生日期");
			headData.add("手机号");
//			headData.add("操作");
//			headData.add("是否选择");
//			headData.add("价格");
	}

	/**
	 * Create the frame.
	 */
	public EmployeeInfoPanel() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("员工信息");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(455, 10, 104, 26);
		contentPane.add(label);
		
		
		    
		Map<String,Object> returnMap=this.getDefaultTableModelByQuery(1);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
	
		table = new JTable();
		table.setModel(tableMode);
	
		JScrollPane jscrollPanel=new JScrollPane();
		
		jscrollPanel.setViewportView(table);
		
		table.setRowHeight(25);
		
		//设置所有的单元格都居中展现
		this.setCellCenter(headData);
		
		
		Dimension size = table.getTableHeader().getPreferredSize();
		size.height = 35;//设置新的表头高度40
		table.getTableHeader().setPreferredSize(size);
		
		//隐藏第一列
		hideTableColumn(table,0);
		
		jscrollPanel.setBounds(30, 46, 943,500);
		contentPane.add(jscrollPanel);
		
		String allPageMessage="共"+allNum+"条记录";
	    allPageLabel = new JLabel(allPageMessage);
		allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		allPageLabel.setBounds(40, 556, 75, 26);
		contentPane.add(allPageLabel);
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
	    currentPageLabel = new JLabel(allCurrentPageMessage);
	    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
	    currentPageLabel.setBounds(189, 562, 94, 15);
		contentPane.add(currentPageLabel);
		
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
				
				
			}
		});
		btnNewButton.setBounds(474, 558, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(334, 558, 93, 23);
		
		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(currentPageNum==1){
					JOptionPane.showMessageDialog(null, "已经是第一页了");
					return;
				}
				
				int pageNum=currentPageNum-1;
				currentPageNum=pageNum;
				
				Map<String,Object> returnMap=getDefaultTableModelByQuery(pageNum);
				DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
				int allNum=(Integer) returnMap.get("allCount");
				
				String allPageMessage="共"+allNum+"条记录";
				
				String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
				allPageLabel.setText(allPageMessage);
				currentPageLabel.setText(allCurrentPageMessage);
				
				table.setModel(tableMode);
				hideTableColumn(table,0);
				setCellCenter(headData);
				
			}
		});
		
		contentPane.add(preButton);
		
		
		
		textField = new JTextField();
		textField.setBounds(609, 559, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(695, 558, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("新增");
		btnNewButton_2.setBounds(173, 614, 93, 23);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
				Vector rowData=new Vector();
				currentDataModel.addRow(rowData);
				
			}
		});
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("删除");
		btnNewButton_3.setBounds(319, 614, 93, 23);
		
		btnNewButton_3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				deleteOpertionMethod();
				
			}
		});
		
		contentPane.add(btnNewButton_3);
		
		saveButton = new JButton("保存");
		saveButton.setBounds(502, 614, 93, 23);
		
		saveButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try{
					saveButton.setEnabled(false);
					saveOpertionMethod();
					refushDataTable();
					
				}catch(Exception es){
					
					log.error(es.getMessage());
					JOptionPane.showMessageDialog(null,es.getMessage(), "标题",JOptionPane.ERROR_MESSAGE); 
				}finally{
					saveButton.setEnabled(true);
				}
			
				
				
			}
		});
		
		contentPane.add(saveButton);
		
		JButton btnNewButton_5 = new JButton("刷新");
		btnNewButton_5.setBounds(665, 614, 93, 23);
		
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refushDataTable();
			}
		});
		contentPane.add(btnNewButton_5);
		
	}
	
	/**
	 * 跳转到某一页
	 */
	private void goJumpNextPage(){
		String jumpText=textField.getText();
		if(StringUtils.hasText(jumpText)){
			try{
				
				int jumpPage=Integer.valueOf(jumpText);
				
				if(jumpPage>totalPage){
					JOptionPane.showMessageDialog(null,"输入的页码数不能大于总页数");
					textField.setText("");
					return;
				}else if(jumpPage<1){
					JOptionPane.showMessageDialog(null,"输入的页码数不能小于1");
					textField.setText("");
					return;
				}
				
				
				int pageNum=jumpPage;
				
				Map<String,Object> returnMap=getDefaultTableModelByQuery(pageNum);
				
				DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
				int allNum=(Integer) returnMap.get("allCount");
				
				String allPageMessage="共"+allNum+"条记录";
				
				String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
				
				allPageLabel.setText(allPageMessage);
				currentPageLabel.setText(allCurrentPageMessage);
				
				table.setModel(tableMode);
				hideTableColumn(table,0);
				setCellCenter(headData);
				
				
				
			}catch(Exception es){
				JOptionPane.showMessageDialog(null,"请输入正确的页码数");
			}
			
			
		}else{
			JOptionPane.showMessageDialog(null,"请输入跳转的页数");
		}
		
	}
	
	/**
	 * 删除操作
	 */
	private void deleteOpertionMethod(){
		
		int[] selectRows=table.getSelectedRows();
		
		if(selectRows.length>0){
			int result=JOptionPane.showConfirmDialog(null,"确定要删除吗?");
			if(result==JOptionPane.OK_OPTION){
				EmployeeInfoService service=DataBaseUtil.getEmployeeInfoService();
				boolean flag=service.deleteRowData(table, selectRows);
				if(flag){
					refushDataTable();
				}
			}
			
		}else{
			JOptionPane.showMessageDialog(null,"请选择要删除的记录!");
		}
		
	}
	
	/**
	 * 保存操作
	 */
	private void saveOpertionMethod() throws Exception{
		
		  List<Student> addStudentRow=getAddRow(); //新增的记录
		 
		  if(addStudentRow!=null && addStudentRow.size()>0){
			  
			   EmployeeInfoService service=DataBaseUtil.getEmployeeInfoService();
			    service.saveOrUpdateStudent(addStudentRow);
		  }
	}
	
	/**
	 * 下一页操作
	 */
	private void nextPageMethod(){
		
		int pageNum=currentPageNum+1;
		
		if(pageNum>totalPage){
			JOptionPane.showMessageDialog(null, "已经是最后一页了");
			return;
		}
		Map<String,Object> returnMap=getDefaultTableModelByQuery(pageNum);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		table.setModel(tableMode);
		hideTableColumn(table,0);
		setCellCenter(headData);
		
	}
	
	/**
	 * 设置单元格居中
	 * @param headData
	 */
	private void setCellCenter(Vector<String> headData){
		 //设置单元格字居中
	     DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	     render.setHorizontalAlignment(SwingConstants.CENTER);
	     
	     table.getTableHeader().setReorderingAllowed(false);
	     
	      
	        
	        Vector<Item> comboBoxMode=new Vector<Item>();
	        Item item1=new Item();
	        item1.setKey("F");
	        item1.setValue("女");
	        
	        Item item2=new Item();
		        item2.setKey("M");
		        item2.setValue("男");
	        
	        comboBoxMode.add(item1);
	        comboBoxMode.add(item2);
	        
	     
	     for(String headName : headData){
	    	   table.getColumn(headName).setCellRenderer(render);
	    	  
	    	   if(headName.equals("性别")){
	    		   
	    		   ComboColumn column=new ComboColumn(comboBoxMode);
	    		      table.getColumn(headName).setCellEditor(column);
		    		  table.getColumn(headName).setCellRenderer(column);
	    	   }
	    	  if(headName.equals("出生日期")){
	    		  DateColumn dateColumn=new DateColumn();
	    		  table.getColumn(headName).setCellEditor(dateColumn);
	    		  table.getColumn(headName).setCellRenderer(dateColumn);
	    	  }
	    	  if(headName.equals("手机号")){
	    		  
	    		  PhoneColumn phoneColumn=new PhoneColumn();
	    		  table.getColumn(headName).setCellEditor(phoneColumn);
	    		  table.getColumn(headName).setCellRenderer(phoneColumn);
	    	  }
	    	  
	    	 /* if(headName.equals("操作")){
	    		   DeleteButtonColumn buttonColumn=new DeleteButtonColumn();
	    		   table.getColumn(headName).setCellEditor(buttonColumn);
	    		   table.getColumn(headName).setCellRenderer(buttonColumn);
	    		   
	    	   }
	    	   
	    	  if(headName.equals("是否选择")){
	    		  CheckBoxColumn checkBoxColumn=new CheckBoxColumn();
	    		  table.getColumn(headName).setCellEditor(checkBoxColumn);
	    		   table.getColumn(headName).setCellRenderer(checkBoxColumn);
	    	  }
	    	  
	    	  if(headName.equals("价格")){
	    		  MoneyColumn moneyColumn=new MoneyColumn();
	    		  table.getColumn(headName).setCellEditor(moneyColumn);
	    		  table.getColumn(headName).setCellRenderer(moneyColumn);
	    	  }*/
	    	  
	    	  
	     }
	  
	}
	
	/**
	 * 刷新当前记录
	 */
	private void refushDataTable(){
		
		Map<String,Object> returnMap=getDefaultTableModelByQuery(currentPageNum);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";	
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		table.setModel(tableMode);
		hideTableColumn(table,0);
		setCellCenter(headData);
	}
	
	/**
	 * 获取新增的记录
	 * @return
	 * @throws Exception 
	 */
	private List<Student> getAddRow() throws Exception{
		
		DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
		Vector<Vector> allData=currentDataModel.getDataVector();
		
		List<Student> addRowList=new ArrayList<Student>();
		
		for(Vector rowData : allData){
			  Integer id=(Integer) rowData.get(0);
			 String name=(String) rowData.get(1);
			 String gender=(String) rowData.get(2);
			 
			 String birthday=(String) rowData.get(3);
			 String phone=(String) rowData.get(4);
			 
			 if(!StringUtils.hasText(name)){
				   throw new Exception("新增的姓名不能为空!");
			 }
			 
			 Student student=new Student();
			 student.setId(id);
			 student.setName(name);
			 student.setBirthday(birthday);
			 student.setGender(gender);
			 student.setPhone(phone);
			 student.setCreateTime(new Date());
			 addRowList.add(student);
				 
		}
		
		return addRowList;
	}
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
			
		  EmployeeInfoService service=DataBaseUtil.getEmployeeInfoService();
			
		
			
			int allCount=service.queryAllRecord(null);
			
			totalPage=allCount/pageSize +1; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
			Map<String,Object> mapParamter=new HashMap<String,Object>();
			mapParamter.put("rowNum", rowNum);
			mapParamter.put("pageSize",pageSize);
			Vector dataList=service.queryStudentData(mapParamter);
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,headData);
			Map<String,Object> returnMap=new HashMap<String,Object>();
			
			returnMap.put("tableMode", tableMode);
			returnMap.put("allCount", allCount);
			returnMap.put("totalPage", totalPage);
			
			 return returnMap;
	}
	
	
	public static void hideTableColumn(JTable table, int column)  
    {  
        TableColumnModel columns = table.getColumnModel();  
        TableColumn column_id_data = columns.getColumn(column);  
        column_id_data.setMaxWidth(0);  
        column_id_data.setPreferredWidth(0);  
        column_id_data.setMinWidth(0);  
          
        TableColumn column_id_header = table.getTableHeader().getColumnModel()  
                .getColumn(column);  
        column_id_header.setMaxWidth(0);  
        column_id_header.setPreferredWidth(0);  
        column_id_header.setMinWidth(0);  
    }  
}
