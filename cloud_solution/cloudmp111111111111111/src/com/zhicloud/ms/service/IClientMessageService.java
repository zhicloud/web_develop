package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.ClientMessageVO;


public interface IClientMessageService {
	/**
	 * 获取所有反馈信息
	 * @return
	 */
	public List<ClientMessageVO> getAll(Map<String,Object> condition);
	
	/**
	 * 添加一条反馈信息
	 * @param cm
	 * @return
	 */
	public MethodResult add(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 批量删除反馈信息
	 * @param ids
	 * @return
	 */
	public MethodResult deleteByIds(List<String> ids);
	
	/**
	 * 批量标记为已读
	 * @param ids
	 * @return
	 */
	public MethodResult markRead(List<String> ids);
	
} 
