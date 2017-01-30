package com.sundy.view.panel;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.jws.soap.SOAPBinding.Style;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.sundy.domain.ClotheStyleBean;
import com.sundy.domain.ProcessBean;
import com.sundy.domain.StyleItem;
import com.sundy.service.ClotheProcessService;
import com.sundy.service.ClotheStyleService;
import com.sundy.service.DataBaseUtil;
import com.sundy.view.customerColumn.MoneyColumn;
import com.sundy.view.customerColumn.NumberColumn;
import com.sundy.view.customerColumn.StyleComboColumn;

public class ClotheProcessPanel extends JFrame {
	private static final Logger log=Logger.getLogger(ClotheProcessPanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=20; //每页显示20条
	private static Vector headData; //列头
	private Integer styleId;
	private ClotheStyleBean clotheStyleBean;
	private JButton saveButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClotheProcessPanel frame = new ClotheProcessPanel(2);
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
	}

	/**
	 * Create the frame.
	 */
	public ClotheProcessPanel(final Integer styleId) {
		this.styleId=styleId;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("工序信息");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(453, 10, 104, 26);
		contentPane.add(label);
		
		
		//查询款式信息
		ClotheStyleService service=DataBaseUtil.getClotheStyleService();
		 
		ClotheStyleBean styleBean=service.getClotheStyleBeanById(styleId);
		
		this.clotheStyleBean=styleBean;
		
		    
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
		
		jscrollPanel.setBounds(10, 46, 974,500);
		contentPane.add(jscrollPanel);
		
		String allPageMessage="共"+allNum+"条记录";
	    allPageLabel = new JLabel(allPageMessage);
		allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		allPageLabel.setBounds(20, 556, 75, 26);
		contentPane.add(allPageLabel);
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
	    currentPageLabel = new JLabel(allCurrentPageMessage);
	    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
	    currentPageLabel.setBounds(153, 562, 94, 15);
		contentPane.add(currentPageLabel);
		
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
				
				
			}
		});
		btnNewButton.setBounds(477, 556, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(324, 556, 93, 23);
		
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
		textField.setBounds(622, 559, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(711, 556, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("新增");
		btnNewButton_2.setBounds(272, 614, 93, 23);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
				Vector rowData=new Vector();
				rowData.add(null);
				rowData.add(clotheStyleBean.getStyleName());
				currentDataModel.addRow(rowData);
				
			}
		});
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("删除");
		btnNewButton_3.setBounds(435, 614, 93, 23);
		
		btnNewButton_3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				deleteOpertionMethod();
				
			}
		});
		
		contentPane.add(btnNewButton_3);
		
		 saveButton = new JButton("保存");
		 saveButton.setBounds(578, 614, 93, 23);
		 
		 saveButton.addMouseListener(new MouseAdapter() {
			 
			 @Override
			public void mouseClicked(MouseEvent e) {
				 saveButton.setEnabled(false);
				 
				 try{
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
		 
			//注册快捷键ctrl+s
		 this.saveButton.registerKeyboardAction(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try{
					  saveOpertionMethod();
					  refushDataTable();
				}catch(Exception es){
					
					 log.error(es.getMessage());
					 JOptionPane.showMessageDialog(null,es.getMessage(), "标题",JOptionPane.ERROR_MESSAGE); 
				}finally{
					saveButton.setEnabled(true);
				}
				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW); 
	
		contentPane.add(saveButton);
		
		JButton btnNewButton_5 = new JButton("刷新");
		btnNewButton_5.setBounds(711, 614, 93, 23);
		
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
				
				ClotheProcessService service=DataBaseUtil.getClotheProcessService();
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
		
		  List<ProcessBean> addStudentRow=getAddRow(); //新增的记录
		 
		  if(addStudentRow!=null && addStudentRow.size()>0){
			  
			   ClotheProcessService service=DataBaseUtil.getClotheProcessService();
				service.saveOrUpdateProcessBean(addStudentRow);
			  
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
	     
	     //设置表头不能拖放
	     table.getTableHeader().setReorderingAllowed(false);
	     
	     
	     for(String headName : headData){
	    	   table.getColumn(headName).setCellRenderer(render);
	    	  if(headName.equals("工序价格")){
	    		  MoneyColumn moneyColumn=new MoneyColumn();
	    		  table.getColumn(headName).setCellEditor(moneyColumn);
	    		  table.getColumn(headName).setCellRenderer(moneyColumn);
	    	  }
	    	  
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
	 */
	private List<ProcessBean> getAddRow() throws Exception{
		
		DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
		Vector<Vector> allData=currentDataModel.getDataVector();
		
		List<ProcessBean> addRowList=new ArrayList<ProcessBean>();
		
		  for(Vector rowData : allData){
			
			  ProcessBean processBean=new ProcessBean();
			
				 Integer id=(Integer) rowData.get(0);
			
			 if(styleId==null){
				    throw new Exception("新增的款式名称不能为空!");
			 }
			
			 String processName=(String)rowData.get(2);
			 
			 if(!StringUtils.hasText(processName)){
				  throw new Exception("新增的工序名称不能为空!");
			 }
			 
			 Object object=(Object)rowData.get(3); //工序价格
			 
			 if(object==null){
				 throw new Exception("输入的工序价格不能为空");
			 }
			 
			 if(object instanceof Double){
				 BigDecimal processPrice=new BigDecimal((Double)object);
				 processBean.setProcessPrice(processPrice);
			 }else if(object instanceof Integer){
				 BigDecimal processPrice=new BigDecimal((Integer)object);
				 processBean.setProcessPrice(processPrice);
			 }else if(object instanceof String){
				 throw new Exception("输入的工序价格不对");
			 }
			 
			 
			
			 processBean.setProcessName(processName);
			 processBean.setStyleId(styleId);
			 processBean.setCreateDate(new Date());
			 processBean.setUpdateDate(new Date());
			 processBean.setId(id);
			 
			 addRowList.add(processBean);
				 
		}
		
		return addRowList;
	}
	
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
		
		
			
		ClotheProcessService service=DataBaseUtil.getClotheProcessService();
			
		   Map<String,Object> allCountMap=new HashMap<String,Object>();
		   allCountMap.put("styleId",this.styleId);
			
			int allCount=service.queryAllRecord(allCountMap);
			
			totalPage=allCount/pageSize +1; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
			Map<String,Object> mapParamter=new HashMap<String,Object>();
			mapParamter.put("rowNum", rowNum);
			mapParamter.put("pageSize",pageSize);
			mapParamter.put("styleId",this.styleId);
			Vector dataList=service.queryClotheProcessData(mapParamter);
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,headData){
				@Override
				public boolean isCellEditable(int row, int column) {
					 if(column==1){
						 return false; //款式名称不能编辑
					 }
					return super.isCellEditable(row, column);
				}
			};
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
