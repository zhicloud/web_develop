/**
 * Project Name:op
 * File Name:VpcOuterIpVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月1日下午3:33:12
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import com.zhicloud.op.common.util.json.JSONBean;

/**
 * ClassName: VpcOuterIpVO 
 * Function: Vpcip信息 
 * date: 2015年4月1日 下午3:33:12 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
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
	
	

}

