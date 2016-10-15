package com.sundy.view.panel;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.sundy.domain.ProcessBean;
import com.sundy.domain.ProcessFinishNumBean;
import com.sundy.domain.StyleItem;
import com.sundy.service.ClotheProcessService;
import com.sundy.service.DataBaseUtil;
import com.sundy.service.EmployeeInfoService;
import com.sundy.service.ProcessFinishNumService;
import com.sundy.view.customerColumn.NumberColumn;
import com.sundy.view.customerColumn.StyleComboColumn;

public class EmployeeFinishClothePanel extends JFrame {
	private static final Logger log=Logger.getLogger(EmployeeFinishClothePanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=200; //每页显示20条
	private static Vector headData; //列头
	private static Vector finishNumHeadData; //完成件数的表头
	private JTextField styleNameField;
	private JTable employeeFinishNumTable;
	private Integer selectProcessId;
	private JButton saveButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeFinishClothePanel frame = new EmployeeFinishClothePanel();
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
			headData.add("款式名称");
			headData.add("工序名称");
			headData.add("工序价格");
			
			
			finishNumHeadData=new Vector<String>();
			finishNumHeadData.add("编码");
			finishNumHeadData.add("员工名称");
			finishNumHeadData.add("完成件数");
	}

	/**
	 * Create the frame.
	 */
	public EmployeeFinishClothePanel() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("员工完成工序件数录入");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(400, 10, 234, 26);
		contentPane.add(label);
		
		
		/***************************构建查询条件***********************************/
			JLabel styleLabel = new JLabel("款式编码:");
			styleLabel.setFont(new Font("宋体", Font.PLAIN, 14));
			styleLabel.setBounds(263, 73, 75, 15);
			contentPane.add(styleLabel);
			
			styleNameField = new JTextField();
			styleNameField.setBounds(363, 70, 199, 21);
			contentPane.add(styleNameField);
			styleNameField.setColumns(10);
			
			JButton queryButton = new JButton("查询");
			queryButton.setBounds(595, 69, 93, 23);
			queryButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Map<String,Object> returnMap=getDefaultTableModelByQuery();
					DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
					table.setModel(tableMode);
					setTableHideAndEditor(table,headData,0);
					
					//将第二个Table数据清空
					 loadChildData(employeeFinishNumTable,-1,1);
				}
			});
			contentPane.add(queryButton);
			
			JButton resetButton = new JButton("重置");
			resetButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					styleNameField.setText("");
				}
			});
			resetButton.setBounds(721, 69, 93, 23);
			contentPane.add(resetButton);
			
		/************第一table处理***************************************/
		
		table = new JTable();
		JScrollPane jscrollPanel=new JScrollPane();
		jscrollPanel.setViewportView(table);
		table.setRowHeight(25);
		
		Map<String,Object> returnMap=getDefaultTableModelByQuery();
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		table.setModel(tableMode);
		
		setTableHideAndEditor(table,headData,0);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		Dimension size = table.getTableHeader().getPreferredSize();
		size.height = 35;//设置新的表头高度40
		table.getTableHeader().setPreferredSize(size);
	
		
		jscrollPanel.setBounds(28, 112, 943,194);
		contentPane.add(jscrollPanel);
		
		
		//处理双击单元格事件
		this.table.addMouseListener(new MouseAdapter() {
			
			 
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) { //单击选择一行
					 int row=table.getSelectedRow();
					 
					 Integer currentSelectPocessId=(Integer) table.getValueAt(row,0);
					 selectProcessId=currentSelectPocessId;
					 System.out.println(selectProcessId);
					 
					 loadChildData(employeeFinishNumTable,currentSelectPocessId,1);
				}
			}
		});
		
		
		/************创建第二个table***************************************/
		
		//创建第二个面板
		  employeeFinishNumTable = new JTable();
          employeeFinishNumTable.setRowHeight(25);
          
        Map<String,Object> secondReturnMap=getSecondTableDefaultTableModelByQuery(1,this.selectProcessId);
  		DefaultTableModel secondTableMode=(DefaultTableModel) secondReturnMap.get("tableMode");
  		
  		employeeFinishNumTable.setModel(secondTableMode);
  		setTableHideAndEditor(employeeFinishNumTable,finishNumHeadData,0);
  		
  		
		
		
		Dimension employeePanelSize = employeeFinishNumTable.getTableHeader().getPreferredSize();
		employeePanelSize.height = 35;//设置新的表头高度40
		employeeFinishNumTable.getTableHeader().setPreferredSize(size);
		
		 JScrollPane jscrollFinishNumPanel=new JScrollPane();
        jscrollFinishNumPanel.setViewportView(employeeFinishNumTable);
        jscrollFinishNumPanel.setBounds(28, 316, 943,259);
		 contentPane.add(jscrollFinishNumPanel);
		 
		 
		
		
		 /**********************创建共多少页，当前第几页码***************************/
		    int allNum=(Integer) secondReturnMap.get("allCount");
			String allPageMessage="共"+allNum+"条记录";
		    allPageLabel = new JLabel(allPageMessage);
			allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
			allPageLabel.setBounds(28, 585, 75, 26);
			contentPane.add(allPageLabel);
			
			String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		    currentPageLabel = new JLabel(allCurrentPageMessage);
		    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		    currentPageLabel.setBounds(152, 591, 94, 15);
			contentPane.add(currentPageLabel);
		
			
	    /**********************创建下一页，上一页,跳转页码***************************/
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
			}
		});
		btnNewButton.setBounds(437, 585, 93, 23);
		contentPane.add(btnNewButton);
		
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(305, 587, 93, 23);
		
		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prePageMethod();
			}
		});
		
		contentPane.add(preButton);
		
		textField = new JTextField();
		textField.setBounds(558, 585, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(645, 585, 93, 23);
		contentPane.add(btnNewButton_1);
		
		
		 /**********************创建新增,删除,修改，保存***************************/
		
		JButton btnNewButton_2 = new JButton("新增");
		btnNewButton_2.setBounds(162, 624, 93, 23);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcessBean processBean=DataBaseUtil.getClotheProcessService().getProcessBeanById(selectProcessId);
				
				if(processBean!=null){
					DefaultTableModel currentDataModel=(DefaultTableModel) employeeFinishNumTable.getModel();
					Vector rowData=new Vector();
					currentDataModel.addRow(rowData);
				}
				
			
			}
		});
		contentPane.add(btnNewButton_2);
		JButton btnNewButton_3 = new JButton("删除");
		btnNewButton_3.setBounds(315, 624, 93, 23);
		btnNewButton_3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				deleteOpertionMethod();
			}
		});
		
		contentPane.add(btnNewButton_3);
		
		 saveButton = new JButton("保存");
		 saveButton.setBounds(471, 624, 93, 23);
		 
		 saveButton.addMouseListener(new MouseAdapter() {
			 
			 @Override
			public void mouseClicked(MouseEvent e) {
				 try{
					 ProcessBean processBean=DataBaseUtil.getClotheProcessService().getProcessBeanById(selectProcessId);
					 
					 if(processBean!=null){
						 saveButton.setEnabled(false);
						 saveOpertionMethod();
						 refushDataTable(employeeFinishNumTable,selectProcessId);
					 }
					 
					 
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
		btnNewButton_5.setBounds(611, 624, 93, 23);
		
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				refushDataTable(employeeFinishNumTable,selectProcessId);
			}
		});
		contentPane.add(btnNewButton_5);
		
		
	}
	
	/**
	 * 隐藏列和设置列不能拖放
	 * @param table
	 * @param headDataParamteter
	 * @param column
	 */
	private void setTableHideAndEditor(JTable table,Vector headDataParamteter,Integer column){
		//设置所有的单元格都居中展现
		setCellCenter(table,headDataParamteter);
		//隐藏第一列
		hideTableColumn(table,column);
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
				
				Map<String,Object> returnMap=getSecondTableDefaultTableModelByQuery(pageNum,selectProcessId);
				
				DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
				int allNum=(Integer) returnMap.get("allCount");
				
				String allPageMessage="共"+allNum+"条记录";
				
				String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
				
				allPageLabel.setText(allPageMessage);
				currentPageLabel.setText(allCurrentPageMessage);
				
				employeeFinishNumTable.setModel(tableMode);
				hideTableColumn(employeeFinishNumTable,0);
				setCellCenter(employeeFinishNumTable,finishNumHeadData);
				
				
				
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
		
		int[] selectRows=employeeFinishNumTable.getSelectedRows();
		if(selectRows.length>0){
			int result=JOptionPane.showConfirmDialog(null,"确定要删除吗?");
			if(result==JOptionPane.OK_OPTION){
				
				ProcessFinishNumService service=DataBaseUtil.getProcessFinishNumService();
				boolean flag=service.deleteRowData(employeeFinishNumTable, selectRows);
				if(flag){
					refushDataTable(employeeFinishNumTable,selectProcessId);
				}
			}
			
		}else{
			JOptionPane.showMessageDialog(null,"请选择要删除的记录!");
		}
		
	}
	
	/**
	 * 保存操作
	 */
	private void saveOpertionMethod()throws Exception{
		
		  List<ProcessFinishNumBean> addStudentRow=getAddRow(); //新增的记录
		 
		  if(addStudentRow!=null && addStudentRow.size()>0){
			  
			  ProcessFinishNumService service=DataBaseUtil.getProcessFinishNumService();
				service.saveOrUpdateProcessFinishNumBean(addStudentRow);
				
				refushDataTable(employeeFinishNumTable,this.selectProcessId);
			  
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
		Map<String,Object> returnMap=getSecondTableDefaultTableModelByQuery(pageNum,selectProcessId);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		employeeFinishNumTable.setModel(tableMode);
		hideTableColumn(employeeFinishNumTable,0);
		setCellCenter(employeeFinishNumTable,finishNumHeadData);
		
	}
	
	/**
	 * 上一页操作
	 */
	private void prePageMethod(){
		
		if(currentPageNum==1){
			JOptionPane.showMessageDialog(null, "已经是第一页了");
			return;
		}
		
		int pageNum=currentPageNum-1;
		currentPageNum=pageNum;
		
		Map<String,Object> returnMap=getSecondTableDefaultTableModelByQuery(pageNum,selectProcessId);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		employeeFinishNumTable.setModel(tableMode);
		hideTableColumn(employeeFinishNumTable,0);
		setCellCenter(employeeFinishNumTable,finishNumHeadData);
		
	}
	
	/**
	 * 设置工序价格table属性
	 * @param headData
	 */
	private void setCellCenter(JTable jtable,Vector<String> headData){
		 //设置单元格字居中
	     DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	     render.setHorizontalAlignment(SwingConstants.CENTER);
	     
	     //设置表头不能拖放
	     jtable.getTableHeader().setReorderingAllowed(false);
	     for(String headName : headData){
	    	 jtable.getColumn(headName).setCellRenderer(render);
	    	 
	    	 
	    	 //特殊处理记件的lie
	    	 if(jtable.equals(this.employeeFinishNumTable)){
	    		
	    		   if(headName.equals("完成件数")){
		    		  
		    		  NumberColumn numberColumn=new NumberColumn();
		    		  employeeFinishNumTable.getColumn(headName).setCellEditor(numberColumn);
		    		  employeeFinishNumTable.getColumn(headName).setCellRenderer(numberColumn);
		    	   }
	    		   
	    		   if(headName.equals("员工名称")){
	    			     EmployeeInfoService employeeService=DataBaseUtil.getEmployeeInfoService();
	    			     
	    			       Vector<StyleItem> comboBoxMode=employeeService.queryAllStudentConvertItem();
	    			       StyleComboColumn column=new StyleComboColumn(comboBoxMode);
	    			       employeeFinishNumTable.getColumn(headName).setCellEditor(column);
	    			       employeeFinishNumTable.getColumn(headName).setCellRenderer(column);
			    		  
			    	   }
	    	 }
	     }
	  
	}
	
	/**
	 * 根据主表加载字表数据
	 * @param jtabel
	 * @param processId
	 * @param currentPageNum
	 */
	private void loadChildData(JTable jtabel,Integer processId,Integer currentPageNum){
        
		Map<String,Object> returnMap=getSecondTableDefaultTableModelByQuery(currentPageNum,processId);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";	
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		jtabel.setModel(tableMode);
		hideTableColumn(jtabel,0);
		setCellCenter(jtabel,finishNumHeadData);
	}
	
	/**
	 * 刷新当前记录
	 */
	private void refushDataTable(JTable jtabel,Integer processId){
		
		
		
		Map<String,Object> returnMap=getSecondTableDefaultTableModelByQuery(currentPageNum,processId);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
		
		String allPageMessage="共"+allNum+"条记录";	
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
		
		allPageLabel.setText(allPageMessage);
		currentPageLabel.setText(allCurrentPageMessage);
		
		jtabel.setModel(tableMode);
		hideTableColumn(jtabel,0);
		setCellCenter(jtabel,finishNumHeadData);
	}
	
	/**
	 * 获取新增的记录
	 * @return
	 */
	private List<ProcessFinishNumBean> getAddRow()throws Exception{
		
		DefaultTableModel currentDataModel=(DefaultTableModel)employeeFinishNumTable.getModel();
		Vector<Vector> allData=currentDataModel.getDataVector();
		
		List<ProcessFinishNumBean> addRowList=new ArrayList<ProcessFinishNumBean>();
		
		  for(Vector rowData : allData){
			
			  ProcessFinishNumBean processBean=new ProcessFinishNumBean();
			
				 Integer id=(Integer) rowData.get(0);
				 Object employeeIdStr=(Object) rowData.get(1);
				 
				 Object finishNumStr=rowData.get(2);
				 if(finishNumStr instanceof String){
					 Integer finishNum=Integer.valueOf((String)finishNumStr);
					 processBean.setFinishNum(finishNum);
				 }else if(finishNumStr instanceof Integer){
					 processBean.setFinishNum((Integer)finishNumStr);
				 }else{
					 throw new Exception("完成件数不能为空!");
				 }
				 
				 if(employeeIdStr instanceof Integer){
					 Integer employeeId=(Integer)employeeIdStr;
					 processBean.setEmployeeId(employeeId);
				 }else if(employeeIdStr instanceof String){
					 
					 Integer employeeId=Integer.valueOf((String)employeeIdStr);
					 processBean.setEmployeeId(employeeId);
				 }else{
					 throw new Exception("选择的员工名称错误!");
				 }
				
				 processBean.setId(id);
				
				 if(this.selectProcessId>0){
					 processBean.setProcessId(this.selectProcessId);
				 }
				 
				
				 
			     addRowList.add(processBean);
				 
		}
		
		return addRowList;
	}
	
	
	/**
	 * 根据款式的code查询该款式的对应的工序信息
	 * @param pageNum
	 * @return
	 */
	public Map<String,Object> getDefaultTableModelByQuery(){
		
		   String styleCode=styleNameField.getText();
		
			
		   ClotheProcessService service=DataBaseUtil.getClotheProcessService();
		  
			Vector dataList=service.queryClotheProcessDataByStyleCode(styleCode);
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,headData){
				@Override
				public boolean isCellEditable(int row, int column) {
					 return false;
				}
			};
			Map<String,Object> returnMap=new HashMap<String,Object>();
			returnMap.put("tableMode", tableMode);
			return returnMap;
	}
	
	
	
	/**
	 * 根据工序的id,获取该工序下员工完成件数
	 * @param pageNum
	 * @return
	 */
	public Map<String,Object> getSecondTableDefaultTableModelByQuery(int pageNum,Integer processId){
		
		
		
		   ProcessFinishNumService service=DataBaseUtil.getProcessFinishNumService();
			
		   Map<String,Object> allCountMap=new HashMap<String,Object>();
		   allCountMap.put("processId",processId);
			
			int allCount=service.queryAllRecord(allCountMap);
			
			totalPage=(allCount+pageSize-1)/pageSize; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
			Map<String,Object> mapParamter=new HashMap<String,Object>();
			mapParamter.put("rowNum", rowNum);
			mapParamter.put("pageSize",pageSize);
			mapParamter.put("processId",processId);
			
			Vector dataList=service.queryProcessFinishNumByProcessId(mapParamter);
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,finishNumHeadData);
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
