package com.sundy.view.panel;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;

import com.sundy.domain.ClotheStyleBean;
import com.sundy.service.ClotheStyleService;
import com.sundy.service.DataBaseUtil;
import com.sundy.view.customerColumn.NumberColumn;

public class ClotheStylePanel extends JFrame {
	private static final Logger log=Logger.getLogger(ClotheStylePanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=20; //每页显示20条
	private static Vector headData; //列头
	private JTextField queryStyleField;
	private JButton saveButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClotheStylePanel frame = new ClotheStylePanel();
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
			headData.add("款式编码");
			headData.add("款式件数");
			headData.add("月份");
	}

	/**
	 * Create the frame.
	 */
	public ClotheStylePanel() {
		log.info("构建款面板");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("款式信息");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(467, 10, 104, 26);
		contentPane.add(label);
		
		
		queryStyleField = new JTextField();
		queryStyleField.setBounds(339, 46, 154, 21);
		contentPane.add(queryStyleField);
		queryStyleField.setColumns(10);
		
		JButton btnNewButton_6 = new JButton("查询");
		btnNewButton_6.setBounds(562, 46, 93, 23);
		
		btnNewButton_6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				queryStyleData();
			}
		});
		contentPane.add(btnNewButton_6);
		
		
		
		    
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
		
		
		//处理双击单元格事件
		this.table.addMouseListener(new MouseAdapter() {
			
			 
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getButton() == MouseEvent.BUTTON3) {
					rowDoubleClickChange();
				}
			}
		});
		
		
		Dimension size = table.getTableHeader().getPreferredSize();
		size.height = 35;//设置新的表头高度40
		table.getTableHeader().setPreferredSize(size);
		
		//隐藏第一列
		hideTableColumn(table,0);
		
		jscrollPanel.setBounds(41, 77, 943,452);
		contentPane.add(jscrollPanel);
		
		String allPageMessage="共"+allNum+"条记录";
	    allPageLabel = new JLabel(allPageMessage);
		allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		allPageLabel.setBounds(41, 552, 75, 26);
		contentPane.add(allPageLabel);
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
	    currentPageLabel = new JLabel(allCurrentPageMessage);
	    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
	    currentPageLabel.setBounds(171, 558, 94, 15);
		contentPane.add(currentPageLabel);
		
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
				
				
			}
		});
		btnNewButton.setBounds(453, 554, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(297, 554, 93, 23);
		
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
		textField.setBounds(598, 555, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(690, 554, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("新增");
		btnNewButton_2.setBounds(172, 614, 93, 23);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
				Vector rowData=new Vector();
				currentDataModel.addRow(rowData);
				
			}
		});
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("删除");
		btnNewButton_3.setBounds(317, 614, 93, 23);
		
		btnNewButton_3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				deleteOpertionMethod();
				
			}
		});
		
		contentPane.add(btnNewButton_3);
		
	    saveButton = new JButton("保存");
		saveButton.setBounds(478, 614, 93, 23);
		
		this.saveButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try{
					
					saveButton.setEnabled(false);
					saveOpertionMethod();
					refushDataTable();
					
				}catch(DuplicateKeyException exce){
					 exce.getStackTrace();
					 JOptionPane.showMessageDialog(null,"输入的款式编码已经存在了!", "标题",JOptionPane.ERROR_MESSAGE); 
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
		btnNewButton_5.setBounds(647, 614, 93, 23);
		
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refushDataTable();
			}
		});
		contentPane.add(btnNewButton_5);
		
		JLabel lblNewLabel = new JLabel("款式名称:");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel.setBounds(226, 52, 83, 15);
		contentPane.add(lblNewLabel);
		
		JButton resetData = new JButton("重置");
		resetData.setBounds(713, 45, 93, 23);
		resetData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				queryStyleField.setText("");
			}
		});
		
		contentPane.add(resetData);
		
		
	}
	
	/**
	 * 查询款式信息数据
	 */
	private void queryStyleData() {
      
		Map<String,Object> returnMap=getDefaultTableModelByQuery(1);
		
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
				
				ClotheStyleService service=DataBaseUtil.getClotheStyleService();
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
	private void saveOpertionMethod()throws Exception{
		
		  List<ClotheStyleBean> addStudentRow=getAddRow(); //新增的记录
		 
		  if(addStudentRow!=null && addStudentRow.size()>0){
			  
			   ClotheStyleService service=DataBaseUtil.getClotheStyleService();
				service.saveOrUpdateClotheStyleBean(addStudentRow);
			  
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
	    	  
	    	  if(headName.equals("款式件数")){
	    		  NumberColumn numberColumn=new NumberColumn();
	    		  table.getColumn(headName).setCellEditor(numberColumn);
	    		  table.getColumn(headName).setCellRenderer(numberColumn);
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
	private List<ClotheStyleBean> getAddRow() throws Exception{
		DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
		Vector<Vector> allData=currentDataModel.getDataVector();
		
		List<ClotheStyleBean> addRowList=new ArrayList<ClotheStyleBean>();
		
		for(Vector rowData : allData){
			
			 ClotheStyleBean styleBean=new ClotheStyleBean();
			
			 Integer id=(Integer) rowData.get(0);
			 String styleName=(String) rowData.get(1);
			
			 if(!StringUtils.hasText(styleName)){
				  throw new Exception("新增的款式名称不能为空!");
			 }
			 String styleCode=(String)rowData.get(2);
			 
			 if(!StringUtils.hasText(styleCode)){
				  throw new Exception("新增的款式编码不能为空!");
			 }
			 
			 Object object=rowData.get(3);
			 
			 if(object==null){
				  throw new Exception("新增的款式件数不能为空!");
			 }
			 
			 if(object instanceof String){
				  if(!StringUtils.hasText(object.toString())){
					  throw new Exception("新增的款式件数不能为空!");
				  }else{
					  styleBean.setStyleNum(Integer.valueOf(object.toString())); 
				  }
				 
			 }else if(object instanceof Integer){
				 Integer styleNum=(Integer) rowData.get(3);
				 styleBean.setStyleNum(styleNum);
			 }
			
			 
			 String monthName=(String) rowData.get(4);
			
			 styleBean.setId(id);
			 styleBean.setStyleName(styleName);
			 styleBean.setStyleCode(styleCode);
			 styleBean.setMonthName(monthName);
			 
			 addRowList.add(styleBean);
				 
		}
		
		return addRowList;
	}
	
	
	/**
	 * 双击表格行
	 */
	private void rowDoubleClickChange(){
		int row=this.table.getSelectedRow();
		if(row==-1){
			return;
		}
		Integer id=(Integer) this.table.getValueAt(row,0);
		
		if(id!=null && id.intValue()!=0){
			
			
			JDialog childDialog=new JDialog(this,"工序信息",true);
			childDialog.setBounds(20,40,1010, 705);
			childDialog.setModal(true);
			
			ClotheProcessPanel processPanel=new ClotheProcessPanel(id);
			childDialog.getContentPane().add(processPanel.getComponent(0));
			childDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
		
			
			
			int windowWidth = this.getWidth(); //获得窗口宽

			int windowHeight = this.getHeight(); //获得窗口高

			Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包

			Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸

			int screenWidth = screenSize.width; //获取屏幕的宽

			int screenHeight = screenSize.height; //获取屏幕的高

			childDialog.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
			
			childDialog.setVisible(true);
			
		}
		
		
	}
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
			
		 ClotheStyleService service=DataBaseUtil.getClotheStyleService();
			
		
		    String queryStyleName=this.queryStyleField.getText();
		    
		    Map<String,Object> queryParameter=new HashMap<String,Object>();
		    
		    if(StringUtils.hasText(queryStyleName)){
		    	queryParameter.put("styleName", queryStyleName);
		    }
			
			int allCount=service.queryAllRecord(queryParameter);
			
			totalPage=allCount/pageSize +1; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
			Map<String,Object> mapParamter=new HashMap<String,Object>();
			mapParamter.put("rowNum", rowNum);
			mapParamter.put("pageSize",pageSize);
			
			if(StringUtils.hasText(queryStyleName)){
				mapParamter.put("styleName",queryStyleName);
			}
			Vector dataList=service.queryClotheStyleData(mapParamter);
			
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
