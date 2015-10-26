package com.zhicloud.op.service.constant;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zhicloud.op.vo.HttpStatusNoticeVO;

public class AppInconstant {
	
	public static Map<String, Object> cloudHostProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> sysDiskImageProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> agentLogInfo = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, HttpStatusNoticeVO> httpStatusInfo = Collections.synchronizedMap(new LinkedHashMap<String, HttpStatusNoticeVO>()); 

}
