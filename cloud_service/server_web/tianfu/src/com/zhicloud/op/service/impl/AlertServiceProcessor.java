package com.zhicloud.op.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.service.AbstractAlertServiceProcessor;

/**
 * @author 
 * 故障监控
 */
public class AlertServiceProcessor extends AbstractAlertServiceProcessor {
	public static final Logger logger = Logger.getLogger(CloudHostServiceImpl.class);
	private static long firstTime = 0L;
	private static Map<Long,List<ServiceInfoExt>> exceptionMap = new HashMap<Long,List<ServiceInfoExt>>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//存放当前所有异常,避免已存在的异常重复发送通知。
	private Map<String,Long> checkAllMap = new HashMap<String,Long>();
	@Override
	public synchronized void exceptionStatusEvent(List<ServiceInfoExt> serviceList) {
		if(firstTime == 0){
			for(ServiceInfoExt s : serviceList){
				if(s.getName().contains("node")){
					s.setCheckName("node");
				}
				//通过(name+region)的key查询异常是否已存在
				if(checkAllMap.get(s.getCheckName()+s.getRegion()) == null){
					//若不存在，说明有新故障，更新故障时间，然后1分钟后再获取所有新故障发送故障信息
					firstTime = System.currentTimeMillis();
					break;
				}
			}
		}else if(System.currentTimeMillis() - firstTime >= 1000*60){
			if(serviceList.size() > 0){
				StringBuilder sbMail = new StringBuilder();
				StringBuilder sbSms = new StringBuilder();
				List<ServiceInfoExt> sieList = new ArrayList<ServiceInfoExt>();
				//定义临时时间变量，保证两个Map存入的时间相同(用于后面的查询)
				Long currentTime = System.currentTimeMillis();
				//过滤重复的异常
				for(ServiceInfoExt s : serviceList){
					if(s.getName().contains("node")){
						s.setCheckName("node");
					}
					//通过(name+region)的key查询异常是否已存在
					if(checkAllMap.get(s.getCheckName()+s.getRegion()) == null){
						//若不存在将异常存入临时list用以发送信息
						sieList.add(s);
						//将新的异常存入异常Map中用于下次检查
						checkAllMap.put(s.getCheckName()+s.getRegion(), currentTime);
					}
				}
				//为保险，这里还是判断下是否有新的故障(理论上是一定有的)
				if(sieList.size()>0){
					//将每轮异常发送的时间做为key，异常集合做为value存入Map，实现间隔一小时发送一次
					exceptionMap.put(currentTime,sieList);
					serviceList = sieList;
				}else{
					firstTime = 0L;
					return;
				}
				for(ServiceInfoExt sie : serviceList){
					logger.info(sie.getRegion()+"--Failure Monitoring：the server ["+sie.getName()+"] status is "+sie.getStatus()+"and fault type is "+sie.getType());
					int region = sie.getRegion();
					int status = sie.getStatus();
					int type   = sie.getType();
					String reg = "";
					if(region==1){
						reg = "广州";
					}else if(region==2){
						reg = "成都";
					}else if(region==3){
						reg = "北京";
					}else if(region==4){
						reg = "香港";
					}
					String sta = "";
					if(status==1){
						sta = "告警";
					}else if(status==2){
						sta = "故障";
					}else if(status==3){
						sta = "停止";
					}
					String _type = "";
					if(type==0){
						_type = "未知服务";
					}else if(type==1){
						_type = "DATA_SERVER";
					}else if(type==2){
						_type = "CONTROL_SERVER";
					}else if(type==3){
						_type = "NODE_CLIENT";
					}else if(type==4){
						_type = "STORAGE_SERVER";
					}else if(type==5){
						_type = "STATISTIC_SERVER";
					}else if(type==6){
						_type = "MANAGE_TERMINAL";
					}else if(type==7){
						_type = "HTTP_GATEWAY";
					}else if(type==8){
						_type = "DATA_INDEX";
					}else if(type==9){
						_type = "DATA_NODE";
					}else if(type==10){
						_type = "STORAGE_MANAGER";
					}else if(type==11){
						_type = "STORAGE_CLIENT";
					}else if(type==12){
						_type = "STORAGE_PROTAL";
					}else if(type==13){
						_type = "STORAGE_OBJECT";
					}else if(type==14){
						_type = "INTELLIGENT_REOUTER";
					}
					sbMail.append(reg+"地域的"+_type+"出现"+sta+"(名称："+sie.getName()+"，ip:"+sie.getIp()+"，时间:"+sdf.format(new Date(sie.getExceptionTime()))+")<br>");
					sbSms.append(reg+"地域的"+_type+"出现"+sta+"，");
				}
				firstTime = 0L;
				warningNotice(sbMail.toString(),sbSms.toString(),"1");
			}
		}
		
		//-----------------------------
		
		if(exceptionMap.size()>0){
			//取出所有故障组的时间
			Set<Long> keys = exceptionMap.keySet();
			//查询出超过一个小时还未修复的故障，再次发送故障信息
			for(Long time : keys){
				if(System.currentTimeMillis()-time >= 1000*60*60){
					StringBuilder sbMail = new StringBuilder();
					StringBuilder sbSms = new StringBuilder();
					//定义临时的List存放故障组
					List<ServiceInfoExt> curList = exceptionMap.get(time);
					//合并成一条信息发送
					for (ServiceInfoExt server : curList) {
						logger.info(server.getRegion()+"--Failure Monitoring：the server ["+server.getName()+"] status is "+server.getStatus()+"and fault type is "+server.getType());
						int region = server.getRegion();
						int status = server.getStatus();
						int type   = server.getType();
						String reg = "";
						if(region==1){
							reg = "广州";
						}else if(region==2){
							reg = "成都";
						}else if(region==3){
							reg = "北京";
						}else if(region==4){
							reg = "香港";
						}
						String sta = "";
						if(status==1){
							sta = "告警";
						}else if(status==2){
							sta = "故障";
						}else if(status==3){
							sta = "停止";
						}
						String _type = "";
						if(type==0){
							_type = "未知服务";
						}else if(type==1){
							_type = "DATA_SERVER";
						}else if(type==2){
							_type = "CONTROL_SERVER";
						}else if(type==3){
							_type = "NODE_CLIENT";
						}else if(type==4){
							_type = "STORAGE_SERVER";
						}else if(type==5){
							_type = "STATISTIC_SERVER";
						}else if(type==6){
							_type = "MANAGE_TERMINAL";
						}else if(type==7){
							_type = "HTTP_GATEWAY";
						}else if(type==8){
							_type = "DATA_INDEX";
						}else if(type==9){
							_type = "DATA_NODE";
						}else if(type==10){
							_type = "STORAGE_MANAGER";
						}else if(type==11){
							_type = "STORAGE_CLIENT";
						}else if(type==12){
							_type = "STORAGE_PROTAL";
						}else if(type==13){
							_type = "STORAGE_OBJECT";
						}else if(type==14){
							_type = "INTELLIGENT_REOUTER";
						}
						sbMail.append(reg+"地域的"+_type+"出现"+sta+"(名称："+server.getName()+"，ip:"+server.getIp()+"，时间:"+sdf.format(new Date(server.getExceptionTime()))+")<br>");
						sbSms.append(reg+"地域的"+_type+"出现"+sta+"，");
					}
					//保持两个Map时间的同步
					Set<String> timeSet = checkAllMap.keySet();
					Long nowTime = System.currentTimeMillis();
					for(String s : timeSet){
						if(checkAllMap.get(s) == time){
							checkAllMap.put(s, nowTime);
						}
					}
					//更新时间(先删除再添加)
					exceptionMap.remove(time);
					exceptionMap.put(nowTime, curList);
					//发送故障监控通知
					warningNotice(sbMail.toString(),sbSms.toString(),"1");
				}
			}
		}
		/*if(count!=0){
			StringBuilder sbMail = new StringBuilder();
			StringBuilder sbSms = new StringBuilder();
			Map<String,String> checkMap = new HashMap<String,String>();
			long current = System.currentTimeMillis();
			if(current-count >= 1000*5){
				for (ServiceInfoExt server : serviceList) {
					logger.info(server.getRegion()+"--Failure Monitoring：the server ["+server.getName()+"] status is "+server.getStatus()+"and fault type is "+server.getType());
					int region = server.getRegion();
					int status = server.getStatus();
					int type   = server.getType();
					String reg = "";
					if(region==1){
						reg = "广州";
					}else if(region==2){
						reg = "成都";
					}else if(region==3){
						reg = "北京";
					}else if(region==4){
						reg = "香港";
					}
					String sta = "";
					if(status==1){
						sta = "告警";
					}else if(status==2){
						sta = "故障";
					}else if(status==3){
						sta = "停止";
					}
					String _type = "";
					if(type==0){
						_type = "未知服务";
					}else if(type==1){
						_type = "DATA_SERVER";
					}else if(type==2){
						_type = "CONTROL_SERVER";
					}else if(type==3){
						_type = "NODE_CLIENT";
					}else if(type==4){
						_type = "STORAGE_SERVER";
					}else if(type==5){
						_type = "STATISTIC_SERVER";
					}else if(type==6){
						_type = "MANAGE_TERMINAL";
					}else if(type==7){
						_type = "HTTP_GATEWAY";
					}else if(type==8){
						_type = "DATA_INDEX";
					}else if(type==9){
						_type = "DATA_NODE";
					}else if(type==10){
						_type = "STORAGE_MANAGER";
					}else if(type==11){
						_type = "STORAGE_CLIENT";
					}else if(type==12){
						_type = "STORAGE_PROTAL";
					}else if(type==13){
						_type = "STORAGE_OBJECT";
					}else if(type==14){
						_type = "INTELLIGENT_REOUTER";
					}
					if("yes".equals(checkMap.get(reg+_type))){
						continue;
					}
					sbMail.append(reg+"地域的"+_type+"出现"+sta+"<br>");
					sbSms.append(reg+"地域的"+_type+"出现"+sta+"，");
					checkMap.put(reg+_type,"yes");
				}
//				warningNotice(sbMail.toString(),sbSms.toString(),"1");
				count = System.currentTimeMillis();
			}
		}else{
			firstTime = System.currentTimeMillis();
			StringBuilder sbMail = new StringBuilder();
			StringBuilder sbSms = new StringBuilder();
			Map<String,String> checkMap = new HashMap<String,String>();
			for (ServiceInfoExt server : serviceList) {
				logger.info(server.getRegion()+"--Failure Monitoring：the server ["+server.getName()+"] status is "+server.getStatus()+"and fault type is "+server.getType());
				int region = server.getRegion();
				int status = server.getStatus();
				int type   = server.getType();
				String reg = "";
				if(region==1){
					reg = "广州";
				}else if(region==2){
					reg = "成都";
				}else if(region==3){
					reg = "北京";
				}else if(region==4){
					reg = "香港";
				}
				String sta = "";
				if(status==1){
					sta = "告警";
				}else if(status==2){
					sta = "故障";
				}else if(status==3){
					sta = "停止";
				}
				String _type = "";
				if(type==0){
					_type = "未知服务";
				}else if(type==1){
					_type = "DATA_SERVER";
				}else if(type==2){
					_type = "CONTROL_SERVER";
				}else if(type==3){
					_type = "NODE_CLIENT";
				}else if(type==4){
					_type = "STORAGE_SERVER";
				}else if(type==5){
					_type = "STATISTIC_SERVER";
				}else if(type==6){
					_type = "MANAGE_TERMINAL";
				}else if(type==7){
					_type = "HTTP_GATEWAY";
				}else if(type==8){
					_type = "DATA_INDEX";
				}else if(type==9){
					_type = "DATA_NODE";
				}else if(type==10){
					_type = "STORAGE_MANAGER";
				}else if(type==11){
					_type = "STORAGE_CLIENT";
				}else if(type==12){
					_type = "STORAGE_PROTAL";
				}else if(type==13){
					_type = "STORAGE_OBJECT";
				}else if(type==14){
					_type = "INTELLIGENT_REOUTER";
				}
				if("yes".equals(checkMap.get(reg+_type))){
					continue;
				}
				sbMail.append(reg+"地域的"+_type+"出现"+sta+"<br>");
				sbSms.append(reg+"地域的"+_type+"出现"+sta+"，");
				checkMap.put(reg+_type,"yes");
			}
//			warningNotice(sbMail.toString(),sbSms.toString(),"1");
			count = System.currentTimeMillis();
		}*/
	}

	@Override
	public synchronized void recoverEvent(List<ServiceInfoExt> serviceList) {
		//循环所有恢复的故障
		for(ServiceInfoExt sie : serviceList){
			//查询异常表中对应的时间
			if(sie.getName().contains("node")){
				sie.setCheckName("node");
			}
			Long t = checkAllMap.get(sie.getCheckName()+sie.getRegion());
			if(t!=null){
				//通过时间查询故障所在组
				List<ServiceInfoExt> l = exceptionMap.get(t);
				for(int i=0;i<l.size();i++){
					//判断故障是否对应，对应则从故障组中删除
					if((l.get(i).getCheckName()+l.get(i).getRegion()).equals(sie.getName()+sie.getRegion())){
						l.remove(i);
					}
				}
				//如果一个组中的故障已全部恢复，则删除该组，否则更新该组
				if(l.isEmpty()){
					exceptionMap.remove(t);
				}else{
					//更新故障组Map
					exceptionMap.put(t, l);
				}
				//从故障列表(Map)中删除已恢复的故障
				checkAllMap.remove(sie.getCheckName()+sie.getRegion());
			}
		}
		StringBuilder sbMail = new StringBuilder();
		StringBuilder sbSms = new StringBuilder();
		for (ServiceInfoExt server : serviceList) {
			logger.info("Failure Monitoring：the server ["+server.getName()+"] status is "+server.getStatus()+"and fault type is "+server.getType());
			int region = server.getRegion();
			int status = server.getStatus();
			int type   = server.getType();
			String reg = "";
			if(region==1){
				reg = "广州";
			}else if(region==2){
				reg = "成都";
			}else if(region==3){
				reg = "北京";
			}else if(region==4){
				reg = "香港";
			}
			String sta = "";
			if(status==1){
				sta = "告警";
			}else if(status==2){
				sta = "故障";
			}else if(status==3){
				sta = "停止";
			}
			String _type = "";
			if(type==0){
				_type = "未知服务";
			}else if(type==1){
				_type = "DATA_SERVER";
			}else if(type==2){
				_type = "CONTROL_SERVER";
			}else if(type==3){
				_type = "NODE_CLIENT";
			}else if(type==4){
				_type = "STORAGE_SERVER";
			}else if(type==5){
				_type = "STATISTIC_SERVER";
			}else if(type==6){
				_type = "MANAGE_TERMINAL";
			}else if(type==7){
				_type = "HTTP_GATEWAY";
			}else if(type==8){
				_type = "DATA_INDEX";
			}else if(type==9){
				_type = "DATA_NODE";
			}else if(type==10){
				_type = "STORAGE_MANAGER";
			}else if(type==11){
				_type = "STORAGE_CLIENT";
			}else if(type==12){
				_type = "STORAGE_PROTAL";
			}else if(type==13){
				_type = "STORAGE_OBJECT";
			}else if(type==14){
				_type = "INTELLIGENT_REOUTER";
			}
			sbMail.append(reg+"地域的"+_type+sta+"<br>");
			sbSms.append(reg+"地域的"+_type+sta+"，");
		}
		warningNotice(sbMail.toString(),sbSms.toString(),"2");
	}
	private void warningNotice(String info,String infoSms,String flag){
		SendMail sm = new SendMail();
		SendSms ss = new SendSms();
		String phone1 = AppProperties.getValue("monitor_phone1", "");
		String phone2 = AppProperties.getValue("monitor_phone2", "");
		String phone3 = AppProperties.getValue("monitor_phone3", "");
		String notification_on_off = AppProperties.getValue("notification_on_off", "yes");
		if("yes".equals(notification_on_off)){
			sm.sendHttpGatewayDetail(info,flag); 
			ss.sendHttpGatewayDetail(infoSms, flag, phone1);
			ss.sendHttpGatewayDetail(infoSms, flag, phone2);
			ss.sendHttpGatewayDetail(infoSms, flag, phone3);
		}
	}

}
