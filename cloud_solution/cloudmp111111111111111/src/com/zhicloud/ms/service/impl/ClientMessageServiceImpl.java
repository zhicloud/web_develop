package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.mapper.ClientMessageMapper;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IClientMessageService;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.ClientMessageVO;
import com.zhicloud.ms.vo.SysUser;

@Service("clientMessageService")
@Transactional(readOnly=true)
public class ClientMessageServiceImpl implements IClientMessageService{
	private static final Logger logger = Logger.getLogger(ClientMessageServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	@Override
	public List<ClientMessageVO> getAll(Map<String,Object> condition) {
		logger.debug("ClientMessageServiceImpl.getAll()");
		ClientMessageMapper clientMessageMapper = this.sqlSession.getMapper(ClientMessageMapper.class);
		List<ClientMessageVO> cmList = clientMessageMapper.getAll(condition);
		return cmList;
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult add(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("ClientMessageServiceImpl.add()");
		//获取用户名
		String userName = StringUtil.trim(request.getParameter("user_name"));
		//获取旧密码
		String password = StringUtil.trim(request.getParameter("password"));
		//获取反馈内容
		String content = StringUtil.trim(request.getParameter("content"));
		//获取反馈类型
		String type = StringUtil.trim(request.getParameter("type"));
		
		ClientMessageMapper clientMessageMapper = this.sqlSession.getMapper(ClientMessageMapper.class);
		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
		userCondition.put("username", userName);
		userCondition.put("password", password); 
		userCondition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		//返回查询结果
		SysUser user =  userMapper.checkLogin(userCondition);
		if(user==null){
			//如果用户名登录查不到结果，我们认为是使用别名登录
			//通过别名查询用户
			user = userMapper.queryUserByAlias(userName);
			//如果用户存在，取出用户名再和密码进行查询
			if(user!=null){
				String name = user.getUsername();
				Map<String,Object> condition = new LinkedHashMap<String,Object>();
				condition.put("username", name);
				condition.put("password", password); 
				condition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
				//返回结果
				user = userMapper.checkLogin(condition);
				if(user==null){
					return new MethodResult(MethodResult.FAIL, "not found user info");
				}else{
					Map<String,Object> data = new LinkedHashMap<String,Object>();
					String id = StringUtil.generateUUID();
					data.put("userName", user.getUsername());
					data.put("content", content);
					data.put("type", type);
					data.put("id", id);  
					data.put("createTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					data.put("status", 2);
					int i = clientMessageMapper.add(data);
					if(i>0){
						return new MethodResult(MethodResult.SUCCESS, "success");				
					}else{
						return new MethodResult(MethodResult.FAIL, "DB operator fail");
					}
				}
			}else{
				return new MethodResult(MethodResult.FAIL, "not found user info");
			}	
		}else{
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			String id = StringUtil.generateUUID();
			data.put("userName", user.getUsername());
			data.put("content", content);
			data.put("type", type);
			data.put("id", id);  
			data.put("createTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			data.put("status", 2);
			int i = clientMessageMapper.add(data);
			if(i>0){
				return new MethodResult(MethodResult.SUCCESS, "success");				
			}else{
				return new MethodResult(MethodResult.FAIL, "DB operator fail");
			}
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteByIds(List<String> ids) {
		logger.debug("ClientMessageServiceImpl.deleteByIds()");
		try {
			if(ids==null || ids.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择要删除的信息");
			}
			ClientMessageMapper clientMessageMapper = this.sqlSession.getMapper(ClientMessageMapper.class);
			int n = clientMessageMapper.deleteIds(ids);
			if(n>0){
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult markRead(List<String> ids) {
		logger.debug("ClientMessageServiceImpl.markRead()");
		if(ids==null || ids.size()<1){
			return new MethodResult(MethodResult.FAIL,"请选择要标记的信息");
		}
		ClientMessageMapper clientMessageMapper = this.sqlSession.getMapper(ClientMessageMapper.class);
		int n = clientMessageMapper.markRead(ids);
		if(n>0){
			return new MethodResult(MethodResult.SUCCESS,"标记成功");
		}
		return new MethodResult(MethodResult.FAIL,"标记失败");
	}
	
}
