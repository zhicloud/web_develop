package com.zhicloud.ms.vo;

  
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import com.zhicloud.ms.common.util.json.JSONBean;
 
public class IpResourcePoolDetailVO implements JSONBean {
	
	private String uuid; //资源池uuid 
	private String ip;   //起始ip地址
	private int status;  //ip资源状态 
	private BigInteger[] count; //ip资源总数，可用
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	} 
	public int getStatus() {
		return status;
	}
	public String getStatusText(){
		if(this.status == 0){
			return "禁用";
		}else if(this.status == 1){
			return "启用";
		} 
		return "";
	}
	public void setStatus(int status) {
		this.status = status;
	} 
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public BigInteger[] getCount() {
		return count;
	}
	public void setCount(BigInteger[] count) {
		this.count = count;
	} 

	public BigDecimal getIpUsageFormat(){
		MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);
		try{
			return new BigDecimal(count[1]).subtract(new BigDecimal(count[0])).divide(new BigDecimal(count[1]),mc).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
		}catch (Throwable e){
			return new BigDecimal(0);
		}
 	}
	
	

}
