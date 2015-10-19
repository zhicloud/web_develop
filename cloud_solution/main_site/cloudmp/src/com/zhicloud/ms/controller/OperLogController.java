package com.zhicloud.ms.controller;


import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.OperLogVO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/operlog")
public class OperLogController {
	
	
	public static final Logger logger = Logger.getLogger(OperLogController.class);

	@Resource
	IOperLogService operLogService;
	
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
      String username = request.getParameter("name");
      String statusStr = request.getParameter("status");
      String levelStr = request.getParameter("level");
      String flag = request.getParameter("flag");

      Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
      Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);


      if ("1".equals(flag)) {
          page = 0;
      }


      Integer status = null;
      Integer level = null;

      if(StringUtil.isBlank(username)){
          username = null;
      }else{
          username = new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8");
          model.addAttribute("name", username);
          username = "%"+username+"%";
      }

      if(StringUtil.isBlank(statusStr)){
          status = null;
      } else{
          status = Integer.valueOf(statusStr);
      }

      if(StringUtil.isBlank(levelStr)){
          level = null;
      } else{
          level = Integer.valueOf(levelStr);
      }
      model.addAttribute("status", status);
      model.addAttribute("level", level);

      Map<String,Object> condition = new LinkedHashMap<>();
      condition.put("param", username);
      condition.put("level", level);
      condition.put("status", status);
      condition.put("start_row", page * rows);
      condition.put("row_count", rows);

      List<OperLogVO> logList = operLogService.getByPage(condition);
      Integer count = operLogService.getPageCount(condition);
      model.addAttribute("operLogList", logList);
      model.addAttribute("page", page + 1);
      model.addAttribute("count", (count/rows));
      return "oper_log_manage";
  }
	
	@RequestMapping(value = "/download", method=RequestMethod.GET)
	public void download(HttpServletResponse response,HttpServletRequest request) throws IOException {
		try {

			//创建excel工作簿 
	        Workbook wb = new HSSFWorkbook(); 
	        //创建第一个sheet（页)
	        Sheet sheet = wb.createSheet("用户操作日志"); 
	        //Row 行 Cell 方格 
	        // 创建创建表头 
	        Row row = sheet.createRow(0);
	        
	        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        
	        HSSFCell cell = (HSSFCell) row.createCell((short) 0);  
	        cell.setCellValue("操作时间");  
	        cell.setCellStyle(style);  
	        cell = (HSSFCell) row.createCell((short) 1);  
	        cell.setCellValue("操作用户");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 2);  
	        cell.setCellValue("用户IP");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 3);  
	        cell.setCellValue("操作模块");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 4);  
	        cell.setCellValue("操作内容");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 5);  
	        cell.setCellValue("操作结果");  
	        cell.setCellStyle(style);     
	        cell = (HSSFCell) row.createCell((short) 6);  
	        cell.setCellValue("操作等级");  
	        cell.setCellStyle(style);  
	        
	        
	        String username = request.getParameter("name");
			String level = request.getParameter("level");
			String status = request.getParameter("status");
			if(StringUtil.isBlank(username)){
				username = null;
			}else{
				username = "%"+username+"%";
			}
			if(StringUtil.isBlank(level)){
				level = null;
			}
			if(StringUtil.isBlank(status)){
				status = null;
			}
	        Map<String,Object> condition = new LinkedHashMap<>();
	        condition.put("username",username);
	        condition.put("status",status);
	        condition.put("level",level);
	        List<OperLogVO> logList = operLogService.getAll(condition);
	        int i = 0;
	        for(OperLogVO log : logList){
	        	row = sheet.createRow(++i);
	        	row.createCell((short) 0).setCellValue(log.getOperTimeFormat());  
	        	row.createCell((short) 1).setCellValue(log.getUsername()==null?log.getUserId():log.getUsername());  
	        	row.createCell((short) 2).setCellValue(log.getUserIp()==null?"":log.getUserIp());  
	        	row.createCell((short) 3).setCellValue(log.getModule()==null?"":log.getModule());  
	        	row.createCell((short) 4).setCellValue(log.getContent()==null?"":log.getContent());  
	        	row.createCell((short) 5).setCellValue(log.getStatusFormat()==null?"失败":log.getStatusFormat());  
	        	row.createCell((short) 6).setCellValue(log.getLevelFormat()==null?"一般":log.getLevelFormat());  
	        }
	        String fileName = "oper_log.xls";
			String path = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/log_download";
			String filePath = path + "/" + fileName;
			File fp = new File(path);
			
	        //若无该文件夹自动创建
	        if(!fp.exists()){
	        	fp.mkdirs();
	        }
	        
	        File file = new File(filePath);
	        FileOutputStream fileOut = new FileOutputStream(filePath); 
	        // 把上面创建的工作簿输出到文件中 
	        wb.write(fileOut); 
	        //关闭输出流 
	        fileOut.close(); 
	        wb.close();
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
			e.printStackTrace();
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
	
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteLog(@PathVariable("id") String id){
		MethodResult mr = operLogService.deleteById(id);
		return mr;
	}
	
	@RequestMapping(value="/deleteLogByIds",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteByIds(@RequestParam("ids[]") String[] ids){
		List<String> idList = Arrays.asList(ids);
		MethodResult mr = operLogService.deleteByIds(idList);
		return mr;
	}
}
