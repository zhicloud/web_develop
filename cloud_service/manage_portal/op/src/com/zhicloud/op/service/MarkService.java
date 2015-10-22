package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.MarkVO;

public interface MarkService {
	/**
	 * 跳转到标签管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String managePage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 查询所有标签，并返回到页面
	 * @param request
	 * @param response
	 */
	public void queryAllMarks(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 跳转到添加标签页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String addPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 添加标签
	 * 
	 * @param parameter
	 * @return
	 */
	public MethodResult addMark(Map<String, String> parameter);
	
	/**
	 * 跳转到修改标签页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String modPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 修改标签
	 * 
	 * @param parameter
	 * @return
	 */
	public MethodResult modMark(Map<String, String> parameter);
	
	/**
	 * 批量删除标签
	 * 
	 * @param ids
	 * @return
	 */
	public MethodResult deleteMarkByIds(List<String> ids);
	
	/**
	 * @Description:取得特殊的标签对象
	 * @param name
	 * @return MarkVO
	 */
	public MarkVO getSpecialMark(String name);
}
