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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;

import com.sundy.domain.ClotheStyleBean;
import com.sundy.domain.ProcessBean;
import com.sundy.service.ClotheProcessService;
import com.sundy.service.ClotheStyleService;
import com.sundy.service.DataBaseUtil;
import com.sundy.view.customerColumn.NumberColumn;

public class WriteClotheStylePanel extends JFrame {
	private static final Logger log=Logger.getLogger(WriteClotheStylePanel.class);
	
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
	private JTextField styleCodeField;
	private JTextField styleMonthNameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WriteClotheStylePanel frame = new WriteClotheStylePanel();
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
	public WriteClotheStylePanel() {
		log.info("构建款面板");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		queryStyleField = new JTextField();
		queryStyleField.setBounds(201, 19, 119, 21);
		contentPane.add(queryStyleField);
		queryStyleField.setColumns(10);
		
		
		
		
		
		JLabel label = new JLabel("款式编码:");
		label.setBounds(339, 22, 77, 15);
		contentPane.add(label);
		
		styleCodeField = new JTextField();
		styleCodeField.setBounds(426, 19, 93, 21);
		contentPane.add(styleCodeField);
		styleCodeField.setColumns(10);
		
		JLabel label_1 = new JLabel("月份:");
		label_1.setBounds(541, 22, 40, 15);
		contentPane.add(label_1);
		
		styleMonthNameField = new JTextField();
		styleMonthNameField.setBounds(598, 19, 81, 21);
		contentPane.add(styleMonthNameField);
		
		
		
		JButton btnNewButton_6 = new JButton("查询");
		btnNewButton_6.setBounds(711, 18, 93, 23);
		
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
		
		jscrollPanel.setBounds(10, 77, 974,496);
		contentPane.add(jscrollPanel);
		
		String allPageMessage="共"+allNum+"条记录";
	    allPageLabel = new JLabel(allPageMessage);
		allPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		allPageLabel.setBounds(36, 583, 93, 26);
		contentPane.add(allPageLabel);
		
		String allCurrentPageMessage="当前第"+currentPageNum+"/"+totalPage+"页";
	    currentPageLabel = new JLabel(allCurrentPageMessage);
	    currentPageLabel.setFont(new Font("宋体", Font.PLAIN, 14));
	    currentPageLabel.setBounds(163, 589, 94, 15);
		contentPane.add(currentPageLabel);
		
		JButton btnNewButton = new JButton("下一页");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		         nextPageMethod();
				
				
			}
		});
		btnNewButton.setBounds(443, 585, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton preButton = new JButton("上一页");
		preButton.setBounds(295, 585, 93, 23);
		
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
		textField.setBounds(598, 586, 49, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("跳转");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goJumpNextPage();
			}
		});
		btnNewButton_1.setBounds(689, 585, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_5 = new JButton("刷新");
		btnNewButton_5.setBounds(814, 585, 93, 23);
		
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refushDataTable();
			}
		});
		contentPane.add(btnNewButton_5);
		
		JLabel lblNewLabel = new JLabel("款式名称:");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewLabel.setBounds(125, 22, 66, 15);
		contentPane.add(lblNewLabel);
		
		JButton resetData = new JButton("重置");
		resetData.setBounds(829, 18, 93, 23);
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
	 * 双击表格行
	 */
	private void rowDoubleClickChange(){
		int row=this.table.getSelectedRow();
		if(row==-1){
			return;
		}
		Integer id=(Integer) this.table.getValueAt(row,0);
		
		if(id!=null && id.intValue()!=0){
			
			
			JDialog childDialog=new JDialog(this,"录入信息",true);
			childDialog.setBounds(20,40,1010, 705);
			childDialog.setModal(true);
			
			ClotheProcessService processService=DataBaseUtil.getClotheProcessService();
			List<ProcessBean> processHeadData=processService.getProcessDataByStyleId(id);
			
			 if(processHeadData==null || processHeadData.size()==0){
				 JOptionPane.showMessageDialog(null, "当前款式未输入工序信息");
				 return;
			 }
			
			WriteFinishClotheNumberPanel processPanel=new WriteFinishClotheNumberPanel(id);
			childDialog.getContentPane().add(processPanel.getComponent(0));
			childDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
		
			
			
			int windowWidth = this.getWidth(); //获得窗口宽

			int windowHeight = this.getHeight(); //获得窗口高

			Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包

			Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸

			int screenWidth = screenSize.width; //获取屏幕的宽

			int screenHeight = screenSize.height; //获取屏幕的高

			childDialog.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
			childDialog.setResizable(false);
			childDialog.setVisible(true);
			
		}
		
		
	}
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
			
		 ClotheStyleService service=DataBaseUtil.getClotheStyleService();
			
		
		    String queryStyleName=this.queryStyleField.getText();
		    String styleCode=this.styleCodeField.getText();
		    String monthName=this.styleMonthNameField.getText();
		    
		    Map<String,Object> queryParameter=new HashMap<String,Object>();
		    
		    if(StringUtils.hasText(queryStyleName)){
		    	queryParameter.put("styleName", queryStyleName);
		    }
		    if(StringUtils.hasText(styleCode)){
		    	queryParameter.put("styleCode", styleCode);
		    }
		    if(StringUtils.hasText(monthName)){
		    	queryParameter.put("monthName", monthName);
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
			 if(StringUtils.hasText(styleCode)){
				 mapParamter.put("styleCode", styleCode);
			    }
		    if(StringUtils.hasText(monthName)){
		    	mapParamter.put("monthName", monthName);
		    }
			
			Vector dataList=service.queryClotheStyleData(mapParamter);
			
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
