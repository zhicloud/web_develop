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
	        SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyyMMdd"); 
	        Date currentTime = new java.util.Date();  
	        //创建第一个sheet（页），命名为 :发票导出Excel
	        Sheet sheet = wb.createSheet( "单据主表"); 
	        //创建第二个sheet（页）
	        Sheet sheet2 = wb.createSheet("单据明细表"); 	    
	        //Row 行 Cell 方格 
	        //填充内容
	        
	        for(int i = 0;i < ids.length; i++) {
	        	if (i==0){ //添加表头
	        		//单据号	商品行数	购方名称	购方税号	购方地址电话	购方银行帐号	备注	复核人	收款人	清单行商品名称	单据日期	销方银行帐号	销方银行帐号
	        		Row rowtitle = sheet.createRow(0); 
	        		rowtitle.createCell(0).setCellValue("单据号");
	        		rowtitle.createCell(1).setCellValue("商品行数");
	        		rowtitle.createCell(2).setCellValue("购方名称");
	        		rowtitle.createCell(3).setCellValue("购方税号");
	        		rowtitle.createCell(4).setCellValue("购方地址电话");
	        		rowtitle.createCell(5).setCellValue("购方银行帐号");
	        		rowtitle.createCell(6).setCellValue("备注");
	        		rowtitle.createCell(7).setCellValue("复核人");
	        		rowtitle.createCell(8).setCellValue("收款人");
	        		rowtitle.createCell(9).setCellValue("清单行商品名称");
	        		rowtitle.createCell(10).setCellValue("单据日期");
	        		rowtitle.createCell(11).setCellValue("销方银行帐号");
	        		rowtitle.createCell(12).setCellValue("销方银行帐号");	 
	        		
	        		Row rowtitle2 = sheet2.createRow(0); 
	        		//单据号	序号 	货物名称	计量单位	规格	数量	金额	税率	商品税目	折扣金额	税额	折扣税额	折扣率	单价	价格方式
	        		rowtitle2.createCell(0).setCellValue("单据号");
	        		rowtitle2.createCell(1).setCellValue("序号");
	        		rowtitle2.createCell(2).setCellValue("货物名称");
	        		rowtitle2.createCell(3).setCellValue("计量单位");
	        		rowtitle2.createCell(4).setCellValue("规格");
	        		rowtitle2.createCell(5).setCellValue("数量");
	        		rowtitle2.createCell(6).setCellValue("金额");
	        		rowtitle2.createCell(7).setCellValue("税率");
	        		rowtitle2.createCell(8).setCellValue("商品税目");	        		
	        		rowtitle2.createCell(9).setCellValue("折扣金额");
	        		rowtitle2.createCell(10).setCellValue("税额");
	        		rowtitle2.createCell(11).setCellValue("折扣税额");
	        		rowtitle2.createCell(12).setCellValue("折扣率");	
	        		rowtitle2.createCell(13).setCellValue("单价");	
	        		rowtitle2.createCell(14).setCellValue("价格方式");	
	        	}
	        	
	        	
	        	Row row = sheet.createRow(i+1); 
	        	//单据号
	        	row.createCell(0).setCellValue(StringUtil.getSerialNo(currentTime,i));
	        	
	        	//商品行数-默认写死1
	        	row.createCell(1).setCellValue(1);
	        	
	        	//购方名称
		        row.createCell(2).setCellValue(invoiceList.get(i).getInvoiceTitle());
		        
	        	//购方税号
		        row.createCell(3).setCellValue(invoiceList.get(i).getTaxNumber());
		        
	        	//购方地址电话
		        row.createCell(4).setCellValue(invoiceList.get(i).getAddressTel());
		        
	        	//购方银行帐号
		        row.createCell(5).setCellValue(invoiceList.get(i).getBankNumber());

	        	//备注
		        row.createCell(6).setCellValue(invoiceList.get(i).getRemark());
		        //row.createCell(6).setCellValue("");
		        //sheet.setColumnWidth(6, ("".getBytes().length) * 256 );			        
		        
		        //复核人
		        row.createCell(7).setCellValue(AppProperties.getValue("invoice_drawer", "")); 
		        
		        //收款人
		        row.createCell(8).setCellValue(AppProperties.getValue("invoice_payee", "")); 

		        //清单行商品名称
		        row.createCell(9).setCellValue(AppProperties.getValue("invoice_subject", "")); 

		        //单据日期
		        row.createCell(10).setCellValue(formatter.format(currentTime)); 
		        
		        //单据日期
		        row.createCell(10).setCellValue(formatter2.format(currentTime)); 
		        
		        //清单行商品名称
		        row.createCell(11).setCellValue(AppProperties.getValue("invoice_bank", "")); 
		        
		        //清单行商品名称
		        row.createCell(12).setCellValue(AppProperties.getValue("invoice_address", "")); 
		        
		        
		        Row row2= sheet2.createRow(i+1); 
	        	//单据号
	        	row2.createCell(0).setCellValue(StringUtil.getSerialNo(currentTime,i));
	        	//商品行数-默认写死1
	        	row2.createCell(1).setCellValue(1);
	        	sheet2.setColumnWidth(1, ("1".getBytes().length) * 256 ); 	        	
		        //清单行商品名称
		        row2.createCell(2).setCellValue(AppProperties.getValue("invoice_subject", "")); 
		        //计量单位
		        row2.createCell(3).setCellValue("套"); 
		        //规格
		        row2.createCell(4).setCellValue("");
	        	//数量
	        	row2.createCell(5).setCellValue(1);
	        	
	        	//金额
	        	row2.createCell(6).setCellValue( invoiceList.get(i).getTotalAmount().doubleValue());
	        	
	        	//税率
	        	row2.createCell(7).setCellValue( AppProperties.getValue("tax_rate", "0.03"));
	        	
		        //财务科目
		        row2.createCell(8).setCellValue(AppProperties.getValue("Financial_title", "")); 

		        //折扣金额
	        	row2.createCell(9).setCellValue(0.00);
	        	
		        //税额
	        	row2.createCell(10).setCellValue(0.00);
	        	
		        //折扣税额
	        	row2.createCell(11).setCellValue(0.00);
		        //折扣率
	        	row2.createCell(12).setCellValue(0.00);

		        //单价
	        	row2.createCell(13).setCellValue( invoiceList.get(i).getTotalAmount().doubleValue());
	        	
	        	//价格方式
	        	row2.createCell(14).setCellValue("0");
	        }

	    	String fileName = "发票导出Excel"+formatter.format(currentTime)+".xls";
	    	
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
