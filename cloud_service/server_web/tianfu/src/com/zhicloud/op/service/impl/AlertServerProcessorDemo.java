package com.zhicloud.op.service.impl;

import java.util.List;

import com.zhicloud.op.app.pool.serverInfoPool.ServerInfoExt;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.service.AbstractAlertServerProcessor;

/**
 * 
 * demo
 *
 */
public class AlertServerProcessorDemo extends AbstractAlertServerProcessor {
//	private static long count = 0L;
//	@Override
//	public void serverExceptionStatusEvent(List<ServerInfoExt> serverList) {
//		if(count!=0){
//			long current = System.currentTimeMillis();
//			if(current-count > 1000*60*30){
//				for (ServerInfoExt server : serverList) {
//					int region = server.getRegion();
//					int status = server.getStatus();
//					String reg = "";
//					if(region==1){
//						reg = "广州";
//					}else if(region==2){
//						reg = "成都";
//					}else if(region==3){
//						reg = "北京";
//					}else if(region==4){
//						reg = "香港";
//					}
//					String sta = "";
//					if(status==1){
//						sta = "告警";
//					}else if(status==2){
//						sta = "故障";
//					}else if(status==3){
//						sta = "停止";
//					}
//					warningNotice(reg,"1",sta);
//				}
//			}
//		}else{
//			for (ServerInfoExt server : serverList) {
//				int region = server.getRegion();
//				int status = server.getStatus();
//				String reg = "";
//				if(region==1){
//					reg = "广州";
//				}else if(region==2){
//					reg = "成都";
//				}else if(region==3){
//					reg = "北京";
//				}else if(region==4){
//					reg = "香港";
//				}
//				String sta = "";
//				if(status==1){
//					sta = "告警";
//				}else if(status==2){
//					sta = "故障";
//				}else if(status==3){
//					sta = "停止";
//				}
//				warningNotice(reg,"1",sta);
//			}
//		}
//	}
//
//	@Override
//	public void serverRecoverEvent(List<ServerInfoExt> serverList) {
//		for (ServerInfoExt server : serverList) {
//			int region = server.getRegion();
//			int status = server.getStatus();
//			String reg = "";
//			if(region==1){
//				reg = "广州";
//			}else if(region==2){
//				reg = "成都";
//			}else if(region==3){
//				reg = "北京";
//			}else if(region==4){
//				reg = "香港";
//			}
//			String sta = "";
//			if(status==1){
//				sta = "告警";
//			}else if(status==2){
//				sta = "故障";
//			}else if(status==3){
//				sta = "停止";
//			}
//			warningNotice(reg,"2",sta);
//		}
//		count = 0L;
//	}
//	private void warningNotice(String region,String flag,String status){
//		SendMail sm = new SendMail();
////		sm.sendHttpGatewayDetail(region,flag,status);
//		SendSms ss = new SendSms();
//		String phone1 = AppProperties.getValue("monitor_phone1", "");
//		String phone2 = AppProperties.getValue("monitor_phone2", "");
//		String phone3 = AppProperties.getValue("monitor_phone3", "");
//		ss.sendHttpGateWayStatus(region, flag, phone1);
//		ss.sendHttpGateWayStatus(region, flag, phone2);
//		ss.sendHttpGateWayStatus(region, flag, phone3);
//		count = System.currentTimeMillis();
//	}

}
