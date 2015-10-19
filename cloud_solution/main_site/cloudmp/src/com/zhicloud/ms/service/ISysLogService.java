package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SysLogVO;


public interface ISysLogService {
	/**
	 * 查询所有操作日志
	 * @return
	 */
	public List<SysLogVO> getAll(Map<String,Object> condition);
	
	/**
	 * 根据userId查询操作日志
	 * @param userId
	 * @return
	 */
	public List<SysLogVO> getByUserId(String userId);
	
	/**
	 * 添加操作日志
	 * @param operLog
	 * @return
	 */
	public MethodResult addLog(SysLogVO operLog,HttpServletRequest request);
	
	/**
	 * 根据Id删除操作日志
	 * @param id
	 * @return
	 */
	public MethodResult deleteById(String id);
	
	/**
	 * 批量删除操作日志
	 * @param ids
	 * @return
	 */
	public MethodResult deleteByIds(List<String> ids);
	
	
	/**
	 * 添加日志
	 * @param operLog
	 */
	public void addLog(SysLogVO operLog);
	
	/**
	* @param module 模块名
	 * @param content 内容
	 * @param level 操作等级 1：一般 2：敏感 3：高危
	 * @param status 操作状态 1：成功 2：失败 3：异步 (不能立即获得返回值的为异步)
	 */
	public void addLog(String module,String content,String level,String status);
	
} 
