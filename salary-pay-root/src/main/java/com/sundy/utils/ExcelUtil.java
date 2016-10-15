package com.sundy.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ExcelUtil {
	
	public static JFileChooser getFile() {
		  //默认打开D盘
		  JFileChooser file = new MyChooser("D:/");
		  // 下面这句是去掉显示所有文件这个过滤器。
		  file.setAcceptAllFileFilterUsed(false);
		  // 添加excel文件的过滤器
		  file.addChoosableFileFilter(new ExcelFileFilter("xls"));
		  int result = file.showSaveDialog(null);

		  // JFileChooser.APPROVE_OPTION是个整型常量，代表0。就是说当返回0的值我们才执行相关操作，否则什么也不做。
		  if (result == JFileChooser.APPROVE_OPTION) {

		   // 获得你选择的文件绝对路径。并输出。当然，我们获得这个路径后还可以做很多的事。
		   String path = file.getSelectedFile().getAbsolutePath();
		   System.out.println(path);
		  } else {
		   file = null;
		   System.out.println("你已取消并关闭了窗口！");

		  }
		  return file;
		 }
	
	
	private static class ExcelFileFilter extends FileFilter {
		  String ext;

		  ExcelFileFilter(String ext) {
		   this.ext = ext;
		  }

		  public boolean accept(File f) {
		   if (f.isDirectory()) {
		    return true;
		   }
		   String fileName = f.getName();
		   int index = fileName.lastIndexOf('.');

		   if (index > 0 && index < fileName.length() - 1) {
		    String extension = fileName.substring(index + 1).toLowerCase();
		    if (extension.equals(ext))
		     return true;
		   }
		   return false;
		  }

		  public String getDescription() {
		   if (ext.equals("xls")) {
		    return "Microsoft Excel文件(*.xls)";
		   }
		   return "";
		  }

		 }
	
	
	 private static class MyChooser extends JFileChooser {
		  /**
		   * 
		   */
		  private static final long serialVersionUID = 1L;

		  MyChooser(String path) {
		   super(path);
		  }

		  public void approveSelection() {
		   File file = this.getSelectedFile();
		   if (file.exists()) {
		    int copy = JOptionPane.showConfirmDialog(null, "是否要覆盖当前文件？",
		      "保存", JOptionPane.YES_NO_OPTION,
		      JOptionPane.QUESTION_MESSAGE);
		    if (copy == JOptionPane.YES_OPTION)
		     super.approveSelection();
		   } else
		    super.approveSelection();
		  }

		 }

}
