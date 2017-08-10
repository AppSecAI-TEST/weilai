/**
 * 
 */
package com.chinamobo.ue.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 版本: [1.0]
 * 功能说明: Excel工具类;
 *
 * 作者: WChao
 * 创建时间: 2017年5月5日 下午2:58:50
 */
public class ExcelUtil {
	
	private String fileName = "";
	private HSSFWorkbook hssfWorkbook = null;
	private HSSFSheet sheet = null;
	private HSSFCellStyle style = null;
	public ExcelUtil(){
		
	}
	public ExcelUtil(String fileName){
		this.fileName = fileName;
		// 第一步，创建一个webbook，对应一个Excel文件
		this.hssfWorkbook = createHSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		this.sheet = createSheet(this.fileName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		//this.sheet.createRow(0);
		// 第四步，创建单元格样式，并设置值表头 设置表头居中
		this.style = createCellStyle();
		this.style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	}
	/**
	 * 
		 * 
		 * 功能描述：[创建一个webbook，对应一个Excel文件]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午3:22:09
		 * @return
	 */
	public HSSFWorkbook createHSSFWorkbook(){
		return new HSSFWorkbook();
	}
	/**
	 * 
		 * 
		 * 功能描述：[创建一个Sheet工作区]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午3:21:41
		 * @param fileName
		 * @return
	 */
	public HSSFSheet createSheet(String fileName){
		return  this.hssfWorkbook.createSheet(fileName);
	}
	/**
	 * 
		 * 
		 * 功能描述：[创建单元格样式]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午3:20:47
		 * @return
	 */
	public HSSFCellStyle createCellStyle(){
		return this.hssfWorkbook.createCellStyle();
	}
	/**
	 * 
		 * 
		 * 功能描述：[创建一行]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午3:22:26
		 * @param rownum
		 * @return
	 */
	public HSSFRow creatRow(int rowNum){
		
		return this.sheet.createRow(rowNum);
	}
    /**
     * 
    	 * 
    	 * 功能描述：[创建一个单元格]
    	 *
    	 * 创建者：WChao
    	 * 创建时间: 2017年5月5日 下午3:30:34
    	 * @param row
    	 * @param cellNum
    	 * @return
     */
	public HSSFCell createCell(HSSFRow row ,int cellNum){
		return row.createCell(cellNum);
	}
	/**
	 * 
		 * 
		 * 功能描述：[添加一行数据]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午4:13:51
		 * @param rowNum
		 * @param datas
		 * @return
	 */
	public ExcelUtil insertRowData(int rowNum , Map<Integer,String> datas){
		HSSFRow row = this.creatRow(rowNum);//创建一行;
		for(Integer cellNum : datas.keySet()){
			HSSFCell cell = this.createCell(row, cellNum);//给当前行添加单元格数据;
			cell.setCellValue(datas.get(cellNum));//设置单元格值;
			cell.setCellStyle(this.style);//设置单元格样式；
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[保存生成的excel文件]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年5月5日 下午4:13:44
		 * @param absolutePath
		 * @return
	 */
	public ExcelUtil save(String absolutePath){
		try{
        	File file = new File(absolutePath);
    		if (!file.isDirectory()) {
    			file.mkdirs();
    		}
    		FileOutputStream fout = new FileOutputStream(absolutePath+File.separator+this.fileName);
    		hssfWorkbook.write(fout);  
    		fout.close();  
        }catch (Exception e) {
        	e.printStackTrace();
		}
		return this;
	}
}
