package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.SysLogMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISysLogService;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SysLogVO;

@Service("sysLogService")
@Transactional(readOnly=true)
public class SysLogServiceImpl implements ISysLogService{
	private static final Logger logger = Logger.getLogger(SysLogServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	
	@Override
	public List<SysLogVO> getAll(Map<String,Object> condition) {
		logger.debug("SysLogServiceImpl.getAll()");
		SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
		List<SysLogVO> sysList = sysLogMapper.getAll(condition);
		return sysList;
	}
	@Override
	public List<SysLogVO> getByUserId(String userId) {
		logger.debug("SysLogServiceImpl.getByUserId()");
		SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
		List<SysLogVO> sysList = sysLogMapper.getByUserId(userId);
		return sysList;
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult addLog(SysLogVO operLog,HttpServletRequest request) {
		logger.debug("SysLogServiceImpl.addLog()");
		if(operLog==null){
			return new MethodResult(MethodResult.FAIL,"找不到需要添加的信息");
		}
		if(operLog.getModule()==null){
			operLog.setModule("未知");
		}
		if(operLog.getContent()==null){
			
		}
		if(operLog.getStatus()==null){
			operLog.setStatus(2);
		}
		if(operLog.getLevel()==null){
			operLog.setLevel(1);
		}
		try {
			SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
			Map<String,Object> condition = new LinkedHashMap<>();
			condition.put("id", StringUtil.generateUUID());
			condition.put("module", operLog.getModule());
			condition.put("content", operLog.getContent());
			condition.put("operTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			condition.put("status", operLog.getStatus());
			condition.put("level", operLog.getLevel());
			int n = sysLogMapper.add(condition);
			if(n>0){
				return new MethodResult(MethodResult.SUCCESS,"添加成功");
			}
			return new MethodResult(MethodResult.FAIL,"添加失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"添加失败");
		}
		
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteById(String id) {
		logger.debug("SysLogServiceImpl.deleteById()");
		SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
		int n = sysLogMapper.deleteById(id);
		if(n>0){
			return new MethodResult(MethodResult.SUCCESS,"删除成功");
		}
		return new MethodResult(MethodResult.FAIL,"删除失败");
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteByIds(List<String> ids) {
		logger.debug("SysLogServiceImpl.deleteByIds()");
		if(ids==null || ids.size()<1){
			return new MethodResult(MethodResult.FAIL,"请选择要删除的日志");
		}
		SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
		int n = sysLogMapper.deleteIds(ids);
		if(n>0){
			return new MethodResult(MethodResult.SUCCESS,"删除成功");
		}
		return new MethodResult(MethodResult.FAIL,"删除失败");
	}
	@Override
	public void addLog(SysLogVO operLog) {
		logger.debug("SysLogServiceImpl.addLog()");
		if(operLog==null){
			return;
		}
		if(operLog.getModule()==null){
			operLog.setModule("未知");
		}
		if(operLog.getContent()==null){
			
		}
		if(operLog.getStatus()==null){
			operLog.setStatus(2);
		}
		if(operLog.getLevel()==null){
			operLog.setLevel(1);
		}
		try {
			SysLogMapper sysLogMapper = this.sqlSession.getMapper(SysLogMapper.class);
			Map<String,Object> condition = new LinkedHashMap<>();
			condition.put("id", StringUtil.generateUUID());
			condition.put("module", operLog.getModule());
			condition.put("content", operLog.getContent());
			condition.put("operTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			condition.put("status", operLog.getStatus());
			condition.put("level", operLog.getLevel());
			int n = sysLogMapper.add(condition);
			if(n<1){
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	@Override
	public void addLog(String module, String content, String level,String status) {
		logger.debug("SysLogServiceImpl.addLog()");
		Integer l = null;
		Integer s = null;
		if(level!=null){
			l = Integer.parseInt(level);
		}
		if(status!=null){
			s = Integer.parseInt(status);
		}
		SysLogVO sysLog = new SysLogVO();
		sysLog.setContent(content);
		sysLog.setModule(module);
		sysLog.setLevel(l);
		sysLog.setStatus(s);
		this.addLog(sysLog);
		
	}
	
}
