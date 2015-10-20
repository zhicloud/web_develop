package com.zhicloud.ms.vo; 

import java.text.ParseException;

import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class VpcOuterIpVO implements JSONBean{
	
	private String id;
	private String vpcId;
	private String createTime;
	private String ip;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getCraeteTimeFormat(){
		try {
			return DateUtil.formatDateString(this.createTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

}

