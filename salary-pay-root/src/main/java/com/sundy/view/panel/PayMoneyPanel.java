package com.sundy.view.panel;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.jws.soap.SOAPBinding.Style;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileFilter;
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
import com.sundy.service.EmployeeInfoService;
import com.sundy.service.ExportReportExcelService;
import com.sundy.service.PayMoneyService;
import com.sundy.utils.ExcelUtil;
import com.sundy.view.comboRender.ItemRenderer;
import com.sundy.view.comboRender.KeyValComboBox;
import com.sundy.view.customerColumn.DatePickPanel;
import com.sundy.view.customerColumn.MoneyColumn;
import com.sundy.view.customerColumn.NumberColumn;
import com.sundy.view.customerColumn.StyleComboColumn;

public class PayMoneyPanel extends JFrame {
	private static final Logger log=Logger.getLogger(PayMoneyPanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=500; //每页显示20条
	private static Vector headData; //列头
	private JTextField styleCombox;
	private KeyValComboBox employeeCombox;
	private DatePickPanel startDateTextField;
	private DatePickPanel endDateTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PayMoneyPanel frame = new PayMoneyPanel();
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
			headData.add("款式编码");
			headData.add("员工名称");
			headData.add("工序名称");
			headData.add("工序价格");
			headData.add("完成的件数");
			headData.add("创建时间");
			headData.add("工资");
			
	}

	/**
	 * Create the frame.
	 */
	public PayMoneyPanel() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		/*****************查询条件*********************/
		JLabel label = new JLabel("款式编码:");
		label.setBounds(80, 29, 63, 15);
		contentPane.add(label);
		
		styleCombox = new JTextField();
		styleCombox.setBounds(158, 26, 112, 21);
		contentPane.add(styleCombox);
		styleCombox.setColumns(10);
		
		JLabel label_1 = new JLabel("员工名称:");
		label_1.setBounds(319, 29, 69, 15);
		contentPane.add(label_1);
		
		//设置员工下拉框
		EmployeeInfoService employeeService=DataBaseUtil.getEmployeeInfoService();
		Vector<StyleItem> employeeComboxData=employeeService.queryAllStudentConvertItem();
		StyleItem defaultItem=new StyleItem();
		defaultItem.setKey(-1);
		defaultItem.setValue("--请选择--");
		employeeComboxData.add(0,defaultItem);
		employeeCombox = new KeyValComboBox(employeeComboxData);
		employeeCombox.setBounds(378, 26, 112, 21);
		contentPane.add(employeeCombox);
		
		JLabel lblNewLabel = new JLabel("日期从:");
		lblNewLabel.setBounds(526, 29, 63, 15);
		contentPane.add(lblNewLabel);
		
		startDateTextField = new DatePickPanel();
		startDateTextField.setBounds(599, 26, 100, 21);
		contentPane.add(startDateTextField);
		
		JLabel lblNewLabel_1 = new JLabel("日期到:");
		lblNewLabel_1.setBounds(718, 29, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		endDateTextField = new DatePickPanel();
		endDateTextField.setBounds(782, 26, 100, 21);
		contentPane.add(endDateTextField);
		
		JButton btnNewButton_2 = new JButton("查询");
		btnNewButton_2.setBounds(263, 75, 93, 23);
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				queryData();
			}
		});
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("重置");
		btnNewButton_3.setBounds(452, 75, 93, 23);
		btnNewButton_3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startDateTextField.setFile("");
				endDateTextField.setFile("");
				styleCombox.setText("");
				employeeCombox.setSelectedIndex(-1);
			}
		});
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("导出Excel");
		btnNewButton_4.setBounds(845, 608, 93, 23);
		btnNewButton_4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				processExportExcelMethod();
			}
		});
		contentPane.add(btnNewButton_4);
		
		
		//查询款式信息
		PayMoneyService service=DataBaseUtil.getPayMoneyService();
		    
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
		
		jscrollPanel.setBounds(41, 128, 943,458);
		contentPane.add(jscrollPanel);
		
		String allPageMessage="共"+allNum+"条记录";
	    allPageLabel = new JLabel(allPageMessage);
		allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		allPageLabel.setBounds(42, 606, 75, 26);
		contentPane.add(allPageLabel);
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
	    currentPageLabel = new JLabel(allCurrentPageMessage);
	    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
	    currentPageLabel.setBounds(158, 612, 94, 15);
		contentPane.add(currentPageLabel);
		
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
				
				
			}
		});
		btnNewButton.setBounds(400, 608, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(279, 608, 93, 23);
		
		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prePageButtonListen();
			}
		});
		
		contentPane.add(preButton);
		
		
		
		textField = new JTextField();
		textField.setBounds(518, 609, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(599, 608, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton reflashButton = new JButton("刷新");
		reflashButton.setBounds(710, 608, 93, 23);
		contentPane.add(reflashButton);
		
		reflashButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				refushDataTable();
				
			}
		});
		
	
		
	}
	
	/**
	 * 处理导出Excel文件
	 */
	private void processExportExcelMethod(){
		
		Map<String,Object> paramter=this.getParamter();
		
		String employeeName=(String) paramter.get("employeeName");
		
		ExportReportExcelService service=DataBaseUtil.getExportReportExcelService();
		
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("确定");  
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  //设置只选择目录
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		
			String filePath=chooser.getSelectedFile().getAbsolutePath();
			String fileName="正浩服饰";
			
			if(StringUtils.hasText(employeeName)){
				fileName=employeeName;
			}
		
		    try {
				service.processExportFile(fileName, filePath, paramter);
			} catch (Exception e) {
				log.error(e.getMessage());
				JOptionPane.showMessageDialog(null, "导出Excel失败!");
			}
		}
		
		
		
	}
	
	
	/**
	 * 上一页
	 */
	private void prePageButtonListen(){
		
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
	
	/**
	 * 查询数据
	 */
	private void queryData(){
       
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
	     }
	  
	}
	
	
	
	private Map<String,Object> getParamter(){
		 Map<String,Object> allCountMap=new HashMap<String,Object>();
		String styleCode=this.styleCombox.getText();
		StyleItem employeeItem=(StyleItem) this.employeeCombox.getSelectedItem();
		
		if(employeeItem!=null){
			Integer employeeId=employeeItem.getKey();
			if(employeeId!=null &&  employeeId.intValue()>0){
				allCountMap.put("employeeId", employeeId);
				allCountMap.put("employeeName",employeeItem.getValue());
			}
		}
	    
	    
		String startPayDate=this.startDateTextField.getFile();
		String endPayDate=this.endDateTextField.getFile();
		
		if(StringUtils.hasText(styleCode)){
			allCountMap.put("styleCode", styleCode);
		}
		
		
		//默认加载本月的结算信息
		SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = formate.format(calendar.getTime());
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String lastDay = formate.format(calendar.getTime());
        
		if(StringUtils.hasText(startPayDate)){
			allCountMap.put("startPayDate", startPayDate);
		}else{
			allCountMap.put("startPayDate", firstDay);
		}
		if(StringUtils.hasText(endPayDate)){
			allCountMap.put("endPayDate", endPayDate);
		}else{
			allCountMap.put("endPayDate", lastDay);
		}
		
		
		 return allCountMap;
		
		
	}
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
		
		  Map<String,Object> paramterMap=this.getParamter();
			
		  PayMoneyService service=DataBaseUtil.getPayMoneyService();
			
			
			int allCount=service.queryAllRecord(paramterMap);
			
			totalPage=(allCount+pageSize-1)/pageSize; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
			Map<String,Object> mapParamter=new HashMap<String,Object>();
			mapParamter.put("rowNum", rowNum);
			mapParamter.put("pageSize",pageSize);
			mapParamter.putAll(paramterMap);
			Vector dataList=service.queryPayMoneyBean(mapParamter);
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,headData){
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
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
