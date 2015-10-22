/**
 * Project Name:op
 * File Name:CloudDiskBillDetailVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月23日上午10:21:06
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import java.math.BigDecimal;
import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

/**
 * ClassName: CloudDiskBillDetailVO 
 * Function: 云硬盘账单
 * date: 2015年4月23日 上午10:21:06 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class CloudDiskBillDetailVO  implements JSONBean{
	
	private String id;
	private String diskId;
	private BigInteger disk; 
	private String startTime;
	private String endTime;
	private String createTime;
	private BigDecimal fee;
	private Integer isPaid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDiskId() {
		return diskId;
	}
	public void setDiskId(String diskId) {
		this.diskId = diskId;
	}
	public BigInteger getDisk() {
		return disk;
	}
	public void setDisk(BigInteger disk) {
		this.disk = disk;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public Integer getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}
	
	

}

