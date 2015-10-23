package com.zhicloud.op.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.AppHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.service.InvoiceService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.InvoiceVO;

@Controller
public class OperatorController
{
	
	
	public static final Logger logger = Logger.getLogger(OperatorController.class);
	

	@RequestMapping("/operator.do")
	public String publicOperator(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("OperatorController.publicOperator()");
		boolean isLogin = LoginHelper.isLogin(request,AppConstant.SYS_USER_TYPE_OPERATOR);
		if( isLogin==false )
		{
			return "/public/operator/login.jsp";
		}
		else
		{
			LoginInfo user = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_OPERATOR);
			if(user.getUserType()!=2){	
				request.setAttribute("message", "您不是运营商，不能访问本页面！");
				return "/public/warning.jsp";
			}
			return "/security/operator/main.jsp";
		}
	}
	
	@RequestMapping(value = "/invoiceExport.do")
	public void export(HttpServletResponse response,HttpServletRequest request) throws IOException {
		logger.debug("OperatorController.export()");
		try {
			Pattern pattern = Pattern.compile("[\\[,\\]|]"); 
			String[] ids = pattern.split(StringUtil.trim(request.getParameter("ids")));
			InvoiceService invoiceService = CoreSpringContextManager.getInvoiceService();
			
			List<InvoiceVO> invoiceList = invoiceService.queryInvoiceByIds(ids);
			
			  //创建excel工作簿 
	        Workbook wb = new HSSFWorkbook(); 
	        //获取当前日期
	        SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");    
	    	Date currentTime = new java.util.Date();  
	        //创建第一个sheet（页），命名为 发票导入Excel
	        Sheet sheet = wb.createSheet( "发票导入Excel "+formatter.format(currentTime)); 
	    
	        //Row 行 Cell 方格 
	        // 创建创建表头 
	        Row row = sheet.createRow(0); 
	        //填充内容
	        
	        for(int i = 0;i < ids.length; i++) {
	        	row = sheet.createRow(i); 
	        	row.createCell(0).setCellValue(AppProperties.getValue("invoice_type", ""));
	        	sheet.setColumnWidth(0, (AppProperties.getValue("invoice_type", "").getBytes().length) * 256 ); 
	        	
		        row.createCell(1).setCellValue(invoiceList.get(i).getInvoiceTitle());
		        sheet.setColumnWidth(1, (invoiceList.get(i).getInvoiceTitle().getBytes().length) * 256 );
		        
		        row.createCell(2).setCellValue(AppProperties.getValue("invoice_drawer", "")); 
		        sheet.setColumnWidth(2, (AppProperties.getValue("invoice_drawer", "").getBytes().length) * 256 );
		        
		        row.createCell(3).setCellValue(AppProperties.getValue("invoice_payee", "")); 
		        sheet.setColumnWidth(3, (AppProperties.getValue("invoice_payee", "").getBytes().length) * 256 );
		        
		        row.createCell(4).setCellValue("地址："+invoiceList.get(i).getAddress()+" 联系电话："+invoiceList.get(i).getPhone());
		        sheet.setColumnWidth(4, (("地址："+invoiceList.get(i).getAddress()+" 联系电话："+invoiceList.get(i).getPhone()).getBytes().length) * 256 );
		        
		        row.createCell(5).setCellValue(AppProperties.getValue("invoice_subject", "")); 
		        sheet.setColumnWidth(5, (AppProperties.getValue("invoice_subject", "").getBytes().length) * 256 );
		        
		        row.createCell(6).setCellValue(invoiceList.get(i).getTotalAmount()+"");
		        sheet.autoSizeColumn(6);  
		        
		        row.createCell(7).setCellValue(1);
		        row.createCell(8).setCellValue("次");
		        
	        }
	        
	    	String fileName = "发票导入Excel"+formatter.format(currentTime)+".xls";
	    	
	    	String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/invoice_export";
	    	
	    	//若无该文件夹自动创建
			File fp = new File(filePath);
			
			if(!fp.exists()){
				fp.mkdirs();
			}
			
			String path = filePath+"/"+fileName;
			
	        FileOutputStream fileOut = new FileOutputStream(path); 
	        // 把上面创建的工作簿输出到文件中 
	        wb.write(fileOut); 
	        //关闭输出流 
	        fileOut.close(); 
	        
	       File file = new File(path);
	        
			InputStream in = new FileInputStream(file);
			OutputStream os = response.getOutputStream(); 
			this.setFileDownloadHeader(request, response, file.getName()); 
			response.addHeader("Content-Length", file.length() + "");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			int data = 0;
			while ((data = in.read()) != -1) {
				os.write(data);
			}
			os.close();
			in.close();
			file.delete();
		} catch (Exception e) {
			logger.error("OperatorController.export() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	
	public  void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String userAgent = request.getHeader("USER-AGENT");
        try {
            String finalFileName = null;
            if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
                finalFileName = URLEncoder.encode(fileName,"UTF8");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (UnsupportedEncodingException e) {
        	throw new AppException(e);
        }
    }
	

	
}
