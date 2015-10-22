package com.zhicloud.op.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.AppHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FileUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.MyFileMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserDictionaryMapper;
import com.zhicloud.op.mybatis.mapper.UserOrderMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.CloudUserService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.service.constant.AppInconstant;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.MyFileVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.UserDictionaryVO;
import com.zhicloud.op.vo.UserOrderVO;

@Transactional(readOnly = true)
public class CloudUserServiceImpl extends BeanDirectCallableDefaultImpl implements CloudUserService {
	
	public static final Logger logger = Logger.getLogger(CloudHostServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	@Callable
	public String baseInfoPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("TerminalUserServiceImpl.baseInfoPage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		String id = loginInfo.getUserId();
		
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getBaseInfoById(id);
		if(terminalUser==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		if (terminalUser.getPhone()!=null&&terminalUser.getPhone().length()>4) {
			String phone = terminalUser.getPhone();
			String begin = phone.substring(0, 3);
			String end   = phone.substring(7,11);
			String newPhone = begin+"****"+end;
			terminalUser.setPhone(newPhone);
		}
		if (terminalUser.getEmail()!=null&&terminalUser.getEmail().length()>4) {
			String email     = terminalUser.getEmail();
			String myEmail[] = email.split("@");
			String beging    = myEmail[0].substring(0, 3);
			String newEmail  = beging+"****"+"@"+myEmail[1];
			terminalUser.setEmail(newEmail);
		}
		
		//权限判断
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		else
		{
			request.setAttribute("terminalUser", terminalUser);
			return "/security/user/terminal_user_base_info.jsp";
		}
	}
	
	/**
	 * 我的云主机
	 */
	@Callable
	@Transactional(readOnly = false)
	public String myCloudHostPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.myCloudHostPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		String userId = loginInfo.getUserId();
		String region = request.getParameter("region");
		String newRegion = region;
		if("0".equals(region)){
			newRegion = null;
		}
		// 参数处理
//		Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
//		Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
		
		// 查询数据库
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId", userId);
		condition.put("region", newRegion);
//		condition.put("start_row", page * rows);
//		condition.put("row_count", rows);
		
//		String name = request.getParameter("cloudHostName");
//		Map<String, Object> parameter = new LinkedHashMap<String, Object>();
//		parameter.put("userId", userId);
//		if (name != null && name.length() > 0)
//		{
//			parameter.put("name", "%" + name + "%");
//		}
		if(region == null){
			region = "0";
		}
		List<CloudHostVO> myCloudHostList = cloudHostMapper.getByUserIdAndRegion(condition);
		List<CloudHostVO> myNewCloudHostList = new ArrayList<CloudHostVO>();
		String currentTime = StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS");
		Integer currentTiemInteger = Integer.parseInt(currentTime.substring(0,10));
		List<Map<String,String>> realHostIdList = new ArrayList<>(); 
 		for(CloudHostVO cloudHost : myCloudHostList){
   			if(cloudHost.getType()==null || cloudHost.getType()==5 || cloudHost.getType()==6){
 				continue;
 			}
 			if("停机".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getInactivateTime()!=null){
 				Date date = null;
 				try {
 					date = StringUtil.stringToDate(cloudHost.getInactivateTime(),"yyyyMMddHHmmssSSS");
 				} catch (ParseException e) {
 					e.printStackTrace();
 				}
 				Calendar calendar = Calendar.getInstance();
 				calendar.setTime(date);
 				calendar.add(Calendar.DAY_OF_MONTH,7);
 				String lastTime = StringUtil.dateToString(calendar.getTime(),"yyyyMMddHHmmssSSS");
 				String nowTime = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
 				if(Long.parseLong(nowTime) - Long.parseLong(lastTime) > 0){
 					continue;
 				}else{
 					myNewCloudHostList.add(cloudHost);
 				}
 			}else if("创建中".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getCreateTime()!=null){
 				Integer oldTime = Integer.parseInt(cloudHost.getCreateTime().substring(0,10));
 				if( (currentTiemInteger - oldTime) >= 100){
 					cloudHost.setProcessStatus(2);
 				}
 				myNewCloudHostList.add(cloudHost);
 			}else if("关机".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getRealHostId()!=null){
 				if("backup_true".equals(AppInconstant.hostBackupProgress.get(cloudHost.getRealHostId()+"_backup"))){
 					Map<String,String> idMap = new HashMap<>();
 					idMap.put("type", "backup");
 					idMap.put("id", cloudHost.getId());
 					idMap.put("realId", cloudHost.getRealHostId());
 					realHostIdList.add(idMap);
 				}else if("resume_true".equals(AppInconstant.hostBackupProgress.get(cloudHost.getRealHostId()+"_resume"))){
 					Map<String,String> idMap = new HashMap<>();
 					idMap.put("type", "resume");
 					idMap.put("id", cloudHost.getId());
 					idMap.put("realId", cloudHost.getRealHostId());
 					realHostIdList.add(idMap);
 				}else if("reset_true".equals(AppInconstant.hostResetProgress.get(cloudHost.getRealHostId()+"_reset"))){
 					Map<String,String> idMap = new HashMap<>();
 					idMap.put("type", "reset");
 					idMap.put("id", cloudHost.getId());
 					idMap.put("realId", cloudHost.getRealHostId());
 					realHostIdList.add(idMap);
 				}
 				myNewCloudHostList.add(cloudHost);
 			}else{
 				myNewCloudHostList.add(cloudHost);
 			}
//			"backup_true".equals(AppInconstant.hostBackupProgress.get(uuid+"backup"))
		}
 		//判断用户创建云主机之后是否首次进入主机列表
		if(myNewCloudHostList!=null&&myNewCloudHostList.size()>0){
			UserDictionaryMapper userDictionaryMapper = this.sqlSession.getMapper(UserDictionaryMapper.class);
			Map<String, Object> dictionaryData = new LinkedHashMap<String, Object>();
			dictionaryData.put("userId", userId);
			dictionaryData.put("key", "is_first_access_host_list");
			dictionaryData.put("value", "false");
			UserDictionaryVO dictionary = userDictionaryMapper.getUserDictionaryByKey(dictionaryData);
			if(dictionary==null){
				Map<String, Object> newDictionaryData = new LinkedHashMap<String, Object>();
				newDictionaryData.put("id", StringUtil.generateUUID());
				newDictionaryData.put("userId", userId);
				newDictionaryData.put("key", "is_first_access_host_list");
				newDictionaryData.put("value", "false");
				userDictionaryMapper.addUserDictionary(newDictionaryData);
				request.setAttribute("tips", "yes");				
			}
			
		}
		request.setAttribute("myCloudHostList", myNewCloudHostList);
		request.setAttribute("region", region);
		request.setAttribute("realIdList", realHostIdList);
		return "/security/user/my_cloud_host.jsp";
	}
	
	/**
	 * 我的云主机的查询结果
	 */
	@Callable
	public String myCloudHostQueryResultPartPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.myCloudHostQueryResultPartPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		
		// 参数处理
		String userId = loginInfo.getUserId();
		String name = request.getParameter("cloudHostName");
		Map<String, Object> parameter = new LinkedHashMap<String, Object>();
		parameter.put("userId", userId);
		if (name != null && name.length() > 0)
		{
			parameter.put("name", "%" + name + "%");
		}
		List<CloudHostVO>myCloudHostList = cloudHostMapper.getByUserIdAndRegion(parameter);
		request.setAttribute("myCloudHostList", myCloudHostList);
		UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
		List<UserOrderVO> orderList = userOrderMapper.getOrderById(userId);
		
		request.setAttribute("orderList", orderList);
		return "/security/user/my_cloud_host_query_part.jsp";
	}
	
	/**
	 * 我的历史订单
	 */
	@Callable
	public String myHistoryOrderPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.myHistoryOrderPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/user/my_history_order.jsp";
	}
	/**
	 * 历史订单记录
	 */
	@SuppressWarnings("null")
	@Callable
	public void myHistoryOrderQuery(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.myHistoryOrder()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			
			// 参数处理
			String userId = loginInfo.getUserId();
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = userOrderMapper.queryPageCount(condition); // 总行数
			List<UserOrderVO> userOrderList = userOrderMapper.queryPage(condition);// 分页结果
			List<UserOrderVO> newUserOrderList = new ArrayList<UserOrderVO>();
			try
			{
				for (UserOrderVO userOrder : userOrderList)
				{
					userOrder.setCreateTime(StringUtil.formatDateString(userOrder.getCreateTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd HH:mm:ss"));
					newUserOrderList.add(userOrder);
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, newUserOrderList);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 我的文件
	 */
	@Callable
	public String myFileManagePage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudUserServiceImpl.myFileManagePage()");

		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if (AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo
				.getUserType()) == false) {
			return "/public/have_not_access.jsp";
		}
		return "/security/user/uploaded_file_manage.jsp";
	}

	/**
	 * 查看详情页面
	 */
	@Callable
	public String userOrderViewDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.userOrderViewDetailPage()");
		
//		// 权限判断
//		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
		
		// 参数处理
		String orderId = StringUtil.trim(request.getParameter("orderId"));
		 
		
		List<UserOrderVO> detail = (List<UserOrderVO>)userOrderMapper.getOrderDetailByOrderId(orderId);
		UserOrderVO order = userOrderMapper.getOrderByOrderId(orderId);
		String isPaid = "未付费";
		if(order.getIsPaid()!=null&&order.getIsPaid()==2)
		{
			isPaid = "已付费";
		}
		request.setAttribute("isPaid", isPaid);
		request.setAttribute("detail", detail);
		request.setAttribute("orderId",orderId);
		try {
			request.setAttribute("createTime", StringUtil.formatDateString(request.getParameter("createTime"), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			logger.error("",e);
		}
		request.setAttribute("totalPrice", request.getParameter("totalPrice"));
		if( detail ==null )
		{
			request.setAttribute("message", "找不到详细信息");
			return "/public/warning_dialog.jsp";
		} 
		
		return "/security/user/user_order_detail.jsp";
	}

	@Callable
	public String uploadFilePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudUserServiceImpl.uploadFilePage()");
		try
		{
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			String userId = loginInfo.getUserId();
			String path = AppHelper.getServerHome() + "/projects/" + AppHelper.APP_NAME + "/user_upload/" + userId;
			
			File fp = new File(path);
			
			Double sizeByte = new Double(FileUtil.getDirSize(fp));
			
			Integer sizeInt = sizeByte.intValue();
			
			BigInteger size = new BigInteger(sizeInt.toString());
			
			String totalStr = AppProperties.getValue("upload_total_file_upper_limit", "");
			
			BigInteger totalSize = CapacityUtil.fromCapacityLabel(totalStr + "MB");
			
			request.setAttribute("user_id", userId);
			request.setAttribute("size", size);
			request.setAttribute("total_size", totalSize);
			
			return "/security/user/upload_file.jsp";
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException(e);
		}
	}
	

	@Callable
	public String uploadedFileDetailPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudUserServiceImpl.uploadedFileDetailPage()");
		
		String id = request.getParameter("fileId");
		
		MyFileMapper myFileMapper = this.sqlSession.getMapper(MyFileMapper.class);
		
		MyFileVO myFile = myFileMapper.getMyFileById(id);
		
		request.setAttribute("myFile", myFile);

		return "/security/user/uploaded_file_detail.jsp";
	}

	@Callable
	public void queryMyFile(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudUserServiceImpl.queryMyFile()");
		try {
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);

			MyFileMapper myFileMapper = this.sqlSession.getMapper(MyFileMapper.class);

			// 参数处理
			String userId = loginInfo.getUserId();
			String fileName = StringUtil.trim(request.getParameter("fileName"));
			Integer page = StringUtil.parseInteger(
					request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(
					request.getParameter("rows"), 10);

			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("fileName", "%" + fileName + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = myFileMapper.queryPageCount(condition); // 总行数
			List<MyFileVO> myFileList = myFileMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, myFileList);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addMyFile(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		String _fileName = "";
		String _userId = "";
		logger.debug("CloudUserServiceImpl.addMyFile()");
		
		try {
			String id = (String) parameter.get("id");
			String userId = (String) parameter.get("userId");
			String fileName = (String) parameter.get("fileName");
			String filePath = (String) parameter.get("filePath");
			BigInteger size = (BigInteger) parameter.get("size");
			String uploadTime = (String) parameter.get("uploadTime");
			_fileName = fileName;
			_userId = userId;
			int uploadResult = 0;
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			
			data.put("id", id);
			data.put("userId", userId);
			data.put("fileName",fileName);
			data.put("filePath",  filePath);
			data.put("size",  size);
			data.put("uploadTime", uploadTime);
			
			MyFileMapper myFileMapper = this.sqlSession.getMapper(MyFileMapper.class);
			
			uploadResult = myFileMapper.addMyFile(data);
			
			if(uploadResult > 0){
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "保存成功");
			}
			
			return new MethodResult(MethodResult.FAIL, "保存失败");
			
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("保存失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", _userId);
				operLogData.put("content", "上传文件:"+_fileName);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", _fileName);
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteUploadedFileByIds(List<String> fileIds) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String _fileName = "";
		logger.debug("CloudUserServiceImpl.deleteUploadedFileByIds()");
		try {
			if( fileIds == null || fileIds.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "fileIds不能为空");
			}
			
			MyFileMapper fileMapper = this.sqlSession.getMapper(MyFileMapper.class);
			
			List<String> paths = new ArrayList<String>();
			
			for(String fileId: fileIds) {
				String path =  fileMapper.getMyFileById(fileId).getFilePath();
				paths.add(path);
			}
 			int n = fileMapper.deleteMyFIleByIds((fileIds.toArray(new String[0])));
			
			if(n>0) {
				for(String path: paths) {
					File file = new File(path);
					if(!_fileName.equals("")){
						_fileName = _fileName+",";
					}
					_fileName = _fileName+file.getName();
					file.delete();
				}
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			return new MethodResult(MethodResult.FAIL, "删除失败");
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("删除失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "删除文件"+_fileName);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}

	@Callable
	public MyFileVO getMyFileById(String id) {
		logger.debug("CloudUserServiceImpl.getMyFileById()");
		MyFileMapper myFileMapper = this.sqlSession.getMapper(MyFileMapper.class);
		return myFileMapper.getMyFileById(id);
	}
	
	@Callable
	public MethodResult getMyFileByUserIdAndFileName(String fileName, String userId) {
		logger.debug("CloudUserServiceImpl.getMyFileByUserIdAndFileName()");
		try {
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("userId", userId);
			data.put("fileName",fileName);
			
			MyFileMapper myFileMapper = this.sqlSession.getMapper(MyFileMapper.class);
			if(myFileMapper.getMyFileByUserIdAndFileName(data) != null){
				return new MethodResult(MethodResult.SUCCESS, "文件已存在");
			}
			return new MethodResult(MethodResult.FAIL, "文件不存在");
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
		
	}
		
	@Callable
	public MethodResult getCurrentFileSize(String userId)
	{
		logger.debug("CloudUserServiceImpl.getCurrentFileSize()");
		try
		{
			String path = AppHelper.getServerHome() + "/projects/" + AppHelper.APP_NAME + "/user_upload/" + userId;
			
			File fp = new File(path);
			
			Double sizeByte = new Double(FileUtil.getDirSize(fp));
			
			Integer sizeInt = sizeByte.intValue();
			
			BigInteger size = new BigInteger(sizeInt.toString());
			
			String totalStr = AppProperties.getValue("upload_total_file_upper_limit", "");
			
			BigInteger totalSize = CapacityUtil.fromCapacityLabel(totalStr + "MB");
			
			MethodResult result = new MethodResult(MethodResult.SUCCESS, "获取成功");
			result.put("size", size);
			result.put("totalSize", totalSize);
			
			return result;
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException(e);
		}
		
	}

}
