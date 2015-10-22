package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface SuggestionService {
	public String managePage(HttpServletRequest request,HttpServletResponse response); 
	public void querySuggestion(HttpServletRequest request, HttpServletResponse response);
	public String addSuggestionPage(HttpServletRequest request,HttpServletResponse response);
	public String suggestionDetailPage(HttpServletRequest request,HttpServletResponse response);
	public MethodResult addSuggestion(Map<String, Object> parameter);
	public MethodResult updateSuggestion(Map<String, Object> parameter);

}
