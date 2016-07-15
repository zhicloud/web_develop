package com.zhicloud.ms.constant;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import com.zhicloud.ms.vo.HttpStatusNoticeVO;
import com.zhicloud.ms.vo.PlatformResourceMonitorVO;

public class AppInconstant {
	
	public static Map<String, Object> cloudHostProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> sysDiskImageProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, Object> agentLogInfo = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	public static Map<String, HttpStatusNoticeVO> httpStatusInfo = Collections.synchronizedMap(new LinkedHashMap<String, HttpStatusNoticeVO>());

	public static Map<String, String> nameMap = Collections.synchronizedMap(new LinkedHashMap<String, String>());

	public static Map<Integer, JSONArray> serverRoomInfo = Collections.synchronizedMap(new LinkedHashMap<Integer, JSONArray>());

	public static Map<String, JSONArray> serverRackInfo = Collections.synchronizedMap(new LinkedHashMap<String, JSONArray>());

	public static Map<String, JSONArray> serverInfo = Collections.synchronizedMap(new LinkedHashMap<String, JSONArray>());
	
	public static Date platformResourceMonitorDataLastGet = new Date(); 
	
	public static Map<Integer, PlatformResourceMonitorVO> platformResourceMonitorData = Collections.synchronizedMap(new LinkedHashMap<Integer, PlatformResourceMonitorVO>());
	
	public static Map<Integer, Integer> regionAndTaskRelation = Collections.synchronizedMap(new LinkedHashMap<Integer, Integer>());
	
	//主机备份进度
	public static Map<String, Object> hostBackupProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>());

	public static Map<String, Object> hostResetProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
	//主机迁移
	public static Map<String, Object> migrateProgress = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 
	
    public static Map<String, Object> cloudHostMigrate = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); 

	
	public static String productName = "";
	
	public static boolean isTimerForBackUp = false;

	public static Map<String,Object> serviceEnableResult = Collections.synchronizedMap(new HashMap<String,Object>());

	public static Map<String,Object> serviceDisableResult = Collections.synchronizedMap(new HashMap<String,Object>());
	
	public static String initUser = "false";
	
	public static BigInteger chunkSize = new BigInteger("524288000");

    // 默认资源池最大并发创建数
    public static final int init_maxcreating = 2;
}
