package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.OperLogVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface IOperLogService {
	/**
	 * 查询所有操作日志
	 * @return
	 */
	public List<OperLogVO> getAll(Map<String,Object> condition);

    /**
     * @function 分页查询所有操作日志
     * @author 张翔
     * @param condition
     * @return
     */
    public List<OperLogVO> getByPage(Map<String,Object> condition);

    /**
     * @function 分页查询所有操作日志条数
     * @author 张翔
     * @param condition
     * @return
     */
    public int getPageCount(Map<String,Object> condition);


    /**
	 * 根据userId查询操作日志
	 * @param userId
	 * @return
	 */
	public List<OperLogVO> getByUserId(String userId);
	
	/**
	 * 添加操作日志
	 * @param operLog
	 * @return
	 */
	public MethodResult addLog(OperLogVO operLog,HttpServletRequest request);
	
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
	 * @param module 模块名
	 * @param content 内容
	 * @param level 操作等级 1：一般 2：敏感 3：高危
	 * @param status 操作状态 1：成功 2：失败 3：异步 (不能立即获得返回值的为异步)
	 */
	public void addLog(String module,String content,String level,String status,HttpServletRequest request);
	
} 
