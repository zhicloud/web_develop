package com.zhicloud.op.service.constant;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zhicloud.op.vo.HttpStatusNoticeVO;
import com.zhicloud.op.vo.PlatformResourceMonitorVO;

public class AppInconstant {
	
	public static Map<String, Object> cloudHostProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> sysDiskImageProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>());
	
	public static Map<String, Object> hostBackupProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> hostResetProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> agentLogInfo = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, HttpStatusNoticeVO> httpStatusInfo = Collections.synchronizedMap(new LinkedHashMap<String, HttpStatusNoticeVO>()); 
	
	public static Map<Integer, PlatformResourceMonitorVO> platformResourceMonitorData = Collections.synchronizedMap(new LinkedHashMap<Integer, PlatformResourceMonitorVO>()); 
	
	public static Map<Integer, Integer> regionAndTaskRelation = Collections.synchronizedMap(new LinkedHashMap<Integer, Integer>()); 
	
	public static Date platformResourceMonitorDataLastGet = new Date(); 

}
