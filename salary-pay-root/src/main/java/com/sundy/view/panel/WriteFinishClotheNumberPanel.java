package com.sundy.view.panel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.sundy.domain.ProcessBean;
import com.sundy.domain.ProcessBeanVo;
import com.sundy.domain.ProcessFinishNumBean;
import com.sundy.service.ClotheProcessService;
import com.sundy.service.DataBaseUtil;
import com.sundy.service.EmployeeInfoService;
import com.sundy.service.ProcessFinishNumService;

public class WriteFinishClotheNumberPanel extends JFrame {
	private static final Logger log=Logger.getLogger(WriteFinishClotheNumberPanel.class);
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JButton saveButton;
	private static int currentPageNum=1; //当前第几页
	private static int totalPage; //总共多少页
	private JLabel allPageLabel; //共多少页
	private JLabel currentPageLabel; //当前第几页
	private static int pageSize=800; //每页显示20条
	private Vector headData; //列头
    private int headNumer=15;
	private Integer styelId; //款式id
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WriteFinishClotheNumberPanel frame = new WriteFinishClotheNumberPanel(1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	public WriteFinishClotheNumberPanel(Integer styleId) {
		
		this.styelId=styleId;
		//构建表头数据
		ClotheProcessService processService=DataBaseUtil.getClotheProcessService();
		headData=new Vector();
		headData.add("员工编码");
		headData.add("员工姓名");
		List<ProcessBean> processHeadData=processService.getProcessDataByStyleId(styleId);
		if(processHeadData!=null && processHeadData.size()>0){
			for(ProcessBean bean : processHeadData){
				headData.add(bean.getId()+"|"+bean.getProcessName()+"|"+bean.getProcessPrice());
			}
		}
		
		
		
		log.info("构建款面板");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		    
		Map<String,Object> returnMap=this.getDefaultTableModelByQuery(1);
		
		DefaultTableModel tableMode=(DefaultTableModel) returnMap.get("tableMode");
		int allNum=(Integer) returnMap.get("allCount");
	
		table = new JTable();
		table.setModel(tableMode);
	
		JScrollPane jscrollPanel=new JScrollPane();
		
		jscrollPanel.setViewportView(table);
		
		table.setRowHeight(25);
		
		table.getTableHeader().setReorderingAllowed(false);
         
		if(headData.size()>headNumer){
		  table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		//设置所有的单元格都居中展现
		this.setCellCenter(headData);
		
		
		jscrollPanel.setBounds(10, 10, 974,563);
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
		btnNewButton_5.setBounds(792, 585, 93, 23);
		
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refushDataTable();
			}
		});
		contentPane.add(btnNewButton_5);
		
		saveButton = new JButton("保存");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDataMethod();
			}
		});
		saveButton.setBounds(891, 585, 93, 23);
		
		
		
		
		
		//注册快捷键ctrl+s
	 this.saveButton.registerKeyboardAction(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try{
				  saveDataMethod();
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
		
	
		
		
	}
	
	private void saveDataMethod(){
		DefaultTableModel currentDataModel=(DefaultTableModel) table.getModel();
		Vector<Vector> allData=currentDataModel.getDataVector();
		
		List<ProcessBeanVo> processBeanVoList=new ArrayList<ProcessBeanVo>();
		
		ClotheProcessService processService=DataBaseUtil.getClotheProcessService();
		
		ProcessFinishNumService finishNumService=DataBaseUtil.getProcessFinishNumService();
		
		List<ProcessFinishNumBean> finishListData=new ArrayList<ProcessFinishNumBean>();
		
		if(allData!=null && allData.size()>0){
			for(Vector dataRow: allData){

				Integer employeeId=(Integer) dataRow.get(0);
				String employeeName=(String) dataRow.get(1);
				
				for(int i=0;i<headData.size();i++){
					  
					 if(i!=0 && i!=1){
						 
						 String headValuestr=(String) headData.get(i);
						 String[] headValueList=headValuestr.split("|");
						 Integer processId=Integer.valueOf(headValueList[0]);
						 
						 Object finishNum=dataRow.get(i);
						 
						 System.out.println("员工="+employeeName+" 工序id="+processId+" 完成件数=="+finishNum);
						 
						 ProcessBeanVo vo=new ProcessBeanVo();
						 vo.setEmployeeId(employeeId);
                         vo.setEmployeeName(employeeName);
                         vo.setProcessId(processId);
                         if(finishNum instanceof Integer){
                        	 vo.setFinishNumCount((Integer)finishNum);
                         }else if(finishNum instanceof String){
                        	 Integer currentValue=Integer.valueOf(finishNum.toString());
                        	 vo.setFinishNumCount(currentValue);
                         }else{
                        	 vo.setFinishNumCount(0);
                         }
                         
                         
                         
                     	Map<String,Object> parameterMap=new HashMap<String,Object>();
        				parameterMap.put("styleId", this.styelId);
        				parameterMap.put("employeeId", employeeId);
        				parameterMap.put("processId", processId);
    					List<ProcessFinishNumBean> finishNumList=processService.getProcessFinishNumBeanByMap(parameterMap);
    					if(finishNumList!=null && finishNumList.size()>1){
    						JOptionPane.showMessageDialog(null,"出错了额");
    					}else if(finishNumList!=null && finishNumList.size()==1){
    						ProcessFinishNumBean currentFinishNumBean=finishNumList.get(0);
    						vo.setFinishNumId(currentFinishNumBean.getId());
    						
    					}
                         
    					processBeanVoList.add(vo);
					 }
					 
				}
				
				
				 
			}
		}
		
		if(processBeanVoList!=null && processBeanVoList.size()>0){
			finishNumService.saveOrUpdateProcessFinhsNum(processBeanVoList);
			
			refushDataTable();
		}
		
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
		setCellCenter(headData);
	}
	
	
	
	
	
	public Map<String,Object> getDefaultTableModelByQuery(int pageNum){
		
			
		    
		    Map<String,Object> queryParameter=new HashMap<String,Object>();
		    
		    EmployeeInfoService employeeService=DataBaseUtil.getEmployeeInfoService();
			
			int allCount=employeeService.queryAllRecord(queryParameter);
			
			totalPage=allCount/pageSize +1; //总共的页数
			
			currentPageNum=pageNum;
			int rowNum=(currentPageNum-1)*pageSize; 
		
			
			
			ClotheProcessService processServiceInner=DataBaseUtil.getClotheProcessService();
			
			Vector dataList=processServiceInner.getEmployeeFinishNumData(headData, this.styelId);
			
			
			DefaultTableModel tableMode=new DefaultTableModel(dataList,headData){

				@Override
				public boolean isCellEditable(int row, int column) {
					boolean flag=true;
					if(column==0 || column==1){
						flag=false;
					}
					return flag;
				}
				
			};
			Map<String,Object> returnMap=new HashMap<String,Object>();
			
			returnMap.put("tableMode", tableMode);
			returnMap.put("allCount", allCount);
			returnMap.put("totalPage", totalPage);
			
			 return returnMap;
	}
}
