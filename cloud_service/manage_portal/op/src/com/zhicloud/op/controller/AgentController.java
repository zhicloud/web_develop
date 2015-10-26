package com.zhicloud.op.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.AppHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.BillService;
import com.zhicloud.op.service.BusinessStatisticsService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.BillVO;
import com.zhicloud.op.vo.BusinessStatisticsVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;

@Controller
public class AgentController
{
	
	
	public static final Logger logger = Logger.getLogger(AgentController.class);
	

	@RequestMapping("/agent.do")
	public String publicAgent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("AgentController.publicAgent()");
		
		boolean isLogin = LoginHelper.isLogin(request,AppConstant.SYS_USER_TYPE_AGENT);
		if( isLogin==false )
		{
			return "/public/agent/login.jsp";
		}
		else
		{
			LoginInfo user = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_AGENT);
			if(user.getUserType()!=3){	
				request.setAttribute("message", "您不是代理商，不能访问本页面！");
				return "/public/warning.jsp";
			}
			return "/security/agent/business_graphics.jsp";
		}
	}
	
	
	@RequestMapping(value = "/agentBillExport.do")
	public void export(HttpServletResponse response,HttpServletRequest request) throws IOException { 
		try {
			Pattern pattern = Pattern.compile("[\\[,\\]|]");  
			BillService billService = CoreSpringContextManager.getBillService();
			
			List<BillVO> billList = billService.queryAgentMonthlyDetailForExport(request, response);
			
			  //创建excel工作簿 
	        Workbook wb = new HSSFWorkbook();  
	        //创建第一个sheet（页） 
	    	String title =StringUtil.formatDateString( request.getParameter("createTime"), "yyyyMM", "yyyy年MM月")+"业务明细.xls";
	        Sheet sheet = wb.createSheet(title); 
	    
	        //Row 行 Cell 方格 
	        // 创建创建表头 
	        Row row = sheet.createRow(0); 
	        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	  
	        HSSFCell cell = (HSSFCell) row.createCell((short) 0);  
	        cell.setCellValue("资源名");  
	        cell.setCellStyle(style);  
	        cell = (HSSFCell) row.createCell((short) 1);  
	        cell.setCellValue("地域");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 2);  
	        cell.setCellValue("金额(元)");  
	        cell.setCellStyle(style);     
//	        cell = (HSSFCell) row.createCell((short) 3);  
//	        cell.setCellValue("金额(元)");  
//	        cell.setCellStyle(style);     
	   
	  
	        for (int i = 0; i < billList.size(); i++)  
	        {  
	            row = sheet.createRow((int) i + 1);  
	            BillVO bill = (BillVO) billList.get(i);  
	            // 第四步，创建单元格，并设置值  
	            row.createCell((short) 0).setCellValue(bill.getResourceName()==null?"":bill.getResourceName());  
	            if(bill.getRegion()==null){
	            	row.createCell((short) 1).setCellValue("");
	            }else{
	            	if(bill.getRegion()==1){	            	
	            		row.createCell((short) 1).setCellValue("广州");     
	            	}else if(bill.getRegion()==2){
	            		row.createCell((short) 1).setCellValue("成都");     	            	
	            	}else if(bill.getRegion()==4){
	            		row.createCell((short) 1).setCellValue("香港");     	            	
	            	}
	            } 
	            	
	            row.createCell((short) 2).setCellValue(bill.getFee()+"");     
	        }  
	        
	    	String fileName = title;
	    	
	    	String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/bill_export";
	    	
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
			logger.error("AgentController.export() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	@RequestMapping(value = "/agentBusinessStatisticsExport.do")
	public void agentBusinessStatisticsExport(HttpServletResponse response,HttpServletRequest request) throws IOException { 
		try {
			Pattern pattern = Pattern.compile("[\\[,\\]|]");  
			BusinessStatisticsService businessStatisticsService = CoreSpringContextManager.getBusinessStatisticsService();
			
			List<BusinessStatisticsVO> statisticsList = businessStatisticsService.queryBusinessStatistics(request, response);
			String time =  request.getParameter("time");
			String title = "业务统计";
			if("1".equals(time)){
				title = "最近一年"+title;
			}else if("2".equals(time)){
				title = "最近一月"+title;
			}else if("3".equals(time)){
				title = "最近一周"+title;
			}
			title = title+".xls";
			
			//创建excel工作簿 
			Workbook wb = new HSSFWorkbook();  
			//创建第一个sheet（页） 
 			Sheet sheet = wb.createSheet(title); 
			
			//Row 行 Cell 方格 
			// 创建创建表头 
			Row row = sheet.createRow(0); 
			HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			
			HSSFCell cell = (HSSFCell) row.createCell((short) 0);  
			cell.setCellValue("");  
			cell.setCellStyle(style);  
			cell = (HSSFCell) row.createCell((short) 1);  
			cell.setCellValue("广州");  
			cell.setCellStyle(style);     
			cell = (HSSFCell) row.createCell((short) 2);  
			cell.setCellValue("成都");  
			cell.setCellStyle(style);      
			cell = (HSSFCell) row.createCell((short) 3);  
			cell.setCellValue("香港");  
			cell.setCellStyle(style);      
			cell = (HSSFCell) row.createCell((short) 4);  
			cell.setCellValue("总计");  
			cell.setCellStyle(style);      
			
			
			for (int i = 0; i < statisticsList.size(); i++)  
			{  
				row = sheet.createRow((int) i + 1);  
				BusinessStatisticsVO statistics = (BusinessStatisticsVO) statisticsList.get(i);  
				// 第四步，创建单元格，并设置值  
				if(statistics.getType()==1){					
					row.createCell((short) 0).setCellValue("云主机");  
				}else if(statistics.getType()==2){
					row.createCell((short) 0).setCellValue("云硬盘");  					
				}else if(statistics.getType()==3){
					row.createCell((short) 0).setCellValue("专属云");  					
				}else if(statistics.getType()==0){
					row.createCell((short) 0).setCellValue("总计");  					
				}
				row.createCell((short) 1).setCellValue(statistics.getGzStatistics()+""); 
				row.createCell((short) 2).setCellValue(statistics.getCdStatistics()+""); 
				row.createCell((short) 3).setCellValue(statistics.getXgStatistics()+""); 
				row.createCell((short) 4).setCellValue(statistics.getTotalStatistics()+""); 
				 
			}  
			
			String fileName = title;
			
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/statistics_export";
			
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
			logger.error("AgentController.export() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	@RequestMapping(value = "/agentRechargeLogExport.do")
	public void agentRechargeLogExport(HttpServletResponse response,HttpServletRequest request) throws IOException { 
		try {
			Pattern pattern = Pattern.compile("[\\[,\\]|]");  
			AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
			
			List<AccountBalanceDetailVO> rechargeLogs = accountBalanceService.queryRechargeRecordForExport(request, response);
			String time =  request.getParameter("time");
			String title = "充值明细.xls"; 
			
			//创建excel工作簿 
			Workbook wb = new HSSFWorkbook();  
			//创建第一个sheet（页） 
			Sheet sheet = wb.createSheet(title); 
			
			//Row 行 Cell 方格 
			// 创建创建表头 
			Row row = sheet.createRow(0); 
			HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			
			HSSFCell cell = (HSSFCell) row.createCell((short) 0);  
			cell.setCellValue("充值时间");  
			cell.setCellStyle(style);  
			cell = (HSSFCell) row.createCell((short) 1);  
			cell.setCellValue("充值方式");  
			cell.setCellStyle(style);     
			cell = (HSSFCell) row.createCell((short) 2);  
			cell.setCellValue("充值金额(元)");  
			cell.setCellStyle(style);        
			
			
			for (int i = 0; i < rechargeLogs.size(); i++)  
			{  
				row = sheet.createRow((int) i + 1);  
				AccountBalanceDetailVO record = (AccountBalanceDetailVO) rechargeLogs.get(i);  
				// 第四步，创建单元格，并设置值  
				if(record.getChangeTime()!=null){					
					row.createCell((short) 0).setCellValue(StringUtil.formatDateString(record.getChangeTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss"));  
				}else {
					row.createCell((short) 0).setCellValue("");  					
				}
				if(record.getPayType()==1){					
					row.createCell((short) 1).setCellValue("支付宝");  
				}else if(record.getPayType()==2){
					row.createCell((short) 1).setCellValue("银联");  					
				}else if(record.getPayType()==3){
					row.createCell((short) 1).setCellValue("系统赠送");  					
				}else if(record.getPayType()==5){
					row.createCell((short) 1).setCellValue("兑换现金券");  					
				}else{
					row.createCell((short) 1).setCellValue("");  					
					
				}
				row.createCell((short) 2).setCellValue(record.getAmount()+" ");  
				
			}  
			
			String fileName = title;
			
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/recharge_export";
			
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
			logger.error("AgentController.agentRechargeLogExport() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	@RequestMapping(value = "/agentConsumptionLogExport.do")
	public void agentConsumptionLogExport(HttpServletResponse response,HttpServletRequest request) throws IOException { 
		try {
			Pattern pattern = Pattern.compile("[\\[,\\]|]");  
			AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
			
			List<AccountBalanceDetailVO> consumptionLogs = accountBalanceService.queryConsumptionRecordForExport(request, response);
			String time =  request.getParameter("time");
			String title = "消费明细.xls"; 
			
			//创建excel工作簿 
			Workbook wb = new HSSFWorkbook();  
			//创建第一个sheet（页） 
			Sheet sheet = wb.createSheet(title); 
			
			//Row 行 Cell 方格 
			// 创建创建表头 
			Row row = sheet.createRow(0); 
			HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			
			HSSFCell cell = (HSSFCell) row.createCell((short) 0);  
			cell.setCellValue("消费时间");  
			cell.setCellStyle(style);  
			cell = (HSSFCell) row.createCell((short) 1);  
			cell.setCellValue("消费方式");  
			cell.setCellStyle(style);     
			cell = (HSSFCell) row.createCell((short) 2);  
			cell.setCellValue("资源名称");  
			cell.setCellStyle(style);        
			cell = (HSSFCell) row.createCell((short) 3);  
			cell.setCellValue("消费金额(元)");  
			cell.setCellStyle(style);        
			
			
			for (int i = 0; i < consumptionLogs.size(); i++)  
			{  
				row = sheet.createRow((int) i + 1);  
				AccountBalanceDetailVO record = (AccountBalanceDetailVO) consumptionLogs.get(i);  
				// 第四步，创建单元格，并设置值  
				if(record.getChangeTime()!=null){					
					row.createCell((short) 0).setCellValue(StringUtil.formatDateString(record.getChangeTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss"));  
				}else {
					row.createCell((short) 0).setCellValue("");  					
				} 				
			    row.createCell((short) 1).setCellValue("系统扣费");   
			    row.createCell((short) 2).setCellValue(record.getResourceName());  
				row.createCell((short) 3).setCellValue(record.getAmount()+" ");  
				
			}  
			
			String fileName = title;
			
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/consumption_export";
			
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
			logger.error("AgentController.agentConsumptionLogExport() > ["+Thread.currentThread().getId()+"] ",e);
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
	
	@RequestMapping("/agentforgetpassword.do")
	public String publicUserForgetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("AgentController.publicUserForgetPassword()");  
		return "/security/agent/forgetpassword.jsp";
	}
	
	
}
