/**
 * Project Name:op
 * File Name:VpcBaseInfoVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月1日下午3:29:07
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import java.math.BigDecimal;
import java.text.ParseException;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.json.JSONBean;

/**
 * ClassName: VpcBaseInfoVO 
 * Function: Vpc基本信息  
 * date: 2015年4月1日 下午3:29:07 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class VpcBaseInfoVO implements JSONBean{
	
	private String id;
	private String userId;
	private String createTime;
	private String modifyTime;
	private String realVpcId;
	private String name;
	private String displayName;
	private String description;
	private Integer status;
	private Integer region;
	private BigDecimal monthlyPrice;
	private Integer hostAmount;
	private Integer ipAmount;
	private String realHostId;
 	private String userName; 
	private Integer type = 1;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getRealVpcId() {
		return realVpcId;
	}
	public void setRealVpcId(String realVpcId) {
		this.realVpcId = realVpcId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public BigDecimal getMonthlyPrice() {
		if(monthlyPrice == null){
			return BigDecimal.ZERO;
		}
		return monthlyPrice;
	}
	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	public Integer getHostAmount() {
		return hostAmount;
	}
	public void setHostAmount(Integer hostAmount) {
		this.hostAmount = hostAmount;
	}
	public Integer getIpAmount() {
		return ipAmount;
	}
	public void setIpAmount(Integer ipAmount) {
		this.ipAmount = ipAmount;
	} 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealHostId() {
		return realHostId;
	}
	public void setRealHostId(String realHostId) {
		this.realHostId = realHostId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
 
	
	
	public String getStatusFormat(){
		if(this.status==1){
			return "启用";
		}else if(this.status==2){
			return "停止";
		}else {
			return "关闭";
		}
	}
	public String getRegionFormat(){
		if(this.region==1){
			return "广州";
		}else if(this.region==2){
			return "成都";
		}else if(this.region==3){
			return "北京";
		}else {
			return "香港";
		}
	}
	
	public String getCraeteTimeFormat(){
		try {
			return DateUtil.formatDateString(this.createTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

