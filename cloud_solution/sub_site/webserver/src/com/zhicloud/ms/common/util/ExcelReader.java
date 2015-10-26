package com.zhicloud.ms.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelReader {
	
    private POIFSFileSystem fs;
    
    private HSSFWorkbook wbXls;
    private HSSFSheet sheetXls;
    private HSSFRow rowXls;
    
    private XSSFWorkbook wbXlsx;
    private XSSFSheet sheetXlsx;
    private XSSFRow rowXlsx;
    
    /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitleWithXls(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wbXls = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheetXls = wbXls.getSheetAt(0);
        rowXls = sheetXls.getRow(0);
        // 标题总列数
        int colNum = rowXls.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            //title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(rowXls.getCell(i));
        }
        return title;
    }
    
   
    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
	public Map<Integer, String> readExcelContentWithXls(InputStream is) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            wbXls = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheetXls = wbXls.getSheetAt(0);
        // 得到总行数
        int rowNum = sheetXls.getLastRowNum();
        rowXls = sheetXls.getRow(0);
        int colNum = rowXls.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            rowXls = sheetXls.getRow(i);
            int j = 0;
            while (j < colNum) {
                str += getCellFormatValue(rowXls.getCell(j)).trim() + "    ";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }


    /**
	 * 根据HSSFCell类型设置数据
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
	    String cellvalue = "";
	    if (cell != null) {
	        // 判断当前Cell的Type
	        switch (cell.getCellType()) {
	        // 如果当前Cell的Type为NUMERIC
	        case HSSFCell.CELL_TYPE_NUMERIC:
	        case HSSFCell.CELL_TYPE_FORMULA: {
	            // 判断当前的cell是否为Date
	            if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                // 如果是Date类型则，转化为Data格式
	                
	                //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
	                //cellvalue = cell.getDateCellValue().toLocaleString();
	                
	                //方法2：这样子的data格式是不带带时分秒的：2011-10-12
	                Date date = cell.getDateCellValue();
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                cellvalue = sdf.format(date);
	                
	            }
	            // 如果是纯数字
	            else {
	                // 取得当前Cell的数值
	            	DecimalFormat df = new DecimalFormat("0"); 
                    cellvalue = df.format(cell.getNumericCellValue());
	            }
	            break;
	        }
	        // 如果当前Cell的Type为STRIN
	        case HSSFCell.CELL_TYPE_STRING:
	            // 取得当前的Cell字符串
	            cellvalue = cell.getRichStringCellValue().getString();
	            break;
	        // 默认的Cell值
	        default:
	            cellvalue = " ";
	        }
	    } else {
	        cellvalue = "";
	    }
	    return cellvalue;
	
	}
	
	 /**
	    * 读取Excel表格表头的内容
	    * @param InputStream
	    * @return String 表头内容的数组
	    */
	   public String[] readExcelTitleWithXlsx(InputStream is) {
	       try {
	           wbXlsx = new XSSFWorkbook(is);
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       sheetXlsx = wbXlsx.getSheetAt(0);
	       rowXlsx = sheetXlsx.getRow(0);
	       // 标题总列数
	       int colNum = rowXlsx.getPhysicalNumberOfCells();
	       String[] title = new String[colNum];
	       for (int i = 0; i < colNum; i++) {
	           //title[i] = getStringCellValue(row.getCell((short) i));
	           title[i] = getCellFormatValue(rowXlsx.getCell(i));
	       }
	       return title;
	   }


	/**
	 * 读取Excel数据内容
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, String> readExcelContentWithXlsx(InputStream is) {
	    Map<Integer, String> content = new HashMap<Integer, String>();
	    String str = "";
	    try {
	        wbXlsx = new XSSFWorkbook(is);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    sheetXlsx = wbXlsx.getSheetAt(0);
	    // 得到总行数
	    int rowNum = sheetXlsx.getLastRowNum();
	    rowXlsx = sheetXlsx.getRow(0);
	    int colNum = rowXlsx.getPhysicalNumberOfCells();
	    // 正文内容应该从第二行开始,第一行为表头的标题
	    for (int i = 1; i <= rowNum; i++) {
	        rowXlsx = sheetXlsx.getRow(i);
	        int j = 0;
	        while (j < colNum) {
	            str += getCellFormatValue(rowXlsx.getCell(j)).trim() + "    ";
	            j++;
	        }
	        content.put(i, str);
	        str = "";
	    }
	    return content;
	}

	private String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case XSSFCell.CELL_TYPE_NUMERIC: {
            	DecimalFormat df = new DecimalFormat("0"); 
            	cellvalue = df.format((cell.getNumericCellValue()));
            }
            case XSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                	DecimalFormat df = new DecimalFormat("0"); 
                    cellvalue = df.format(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case XSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

	public  Map<Integer, String>readExcelContent(String fileName) throws FileNotFoundException{
		
		String suffix = fileName.substring(fileName.lastIndexOf(".")); 
		InputStream in = new FileInputStream(fileName);
		if(".xls".equals(suffix)){
			
			return readExcelContentWithXls(in);
		}
		return readExcelContentWithXlsx(in);
	}
	
public  String[] readExcelTitle(String fileName) throws FileNotFoundException{
		
		String suffix = fileName.substring(fileName.lastIndexOf(".")); 
		InputStream in = new FileInputStream(fileName);
		if(".xls".equals(suffix)){
			
			return readExcelTitleWithXls(in);
		}
		return readExcelTitleWithXlsx(in);
	}

	public static void main(String[] args) {
        try {
            ExcelReader excelReader = new ExcelReader();
            
            String fileName ="/Users/sean/Desktop/test.xlsx";
            String[] title = excelReader.readExcelTitle(fileName);
            System.out.println("获得Excel表格的标题:");
            for (String s : title) {
                System.out.print(s + " ");
            }
            System.out.println();

            // 对读取Excel表格内容测试
//            String fileName = "/Users/sean/Desktop/test.xlsx";
            Map<Integer, String> map = excelReader.readExcelContent(fileName);
            System.out.println("获得Excel表格的内容:");
            for (int i = 2; i <= map.size(); i++) {
            	String row = map.get(i);
                String[] result = row.split(" +");
                for(int j = 0; j < result.length; j+=4){
                	System.out.println("username:"+result[j]);
                	System.out.println("name:"+result[j+1]);
                	System.out.println("email:"+result[j+2]);
                	System.out.println("phone:"+result[j+3]);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }
    }
}