package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.zhicloud.op.common.util.json.JSONBean;
public class PackagePriceVO implements JSONBean
{
	private String id;
	/**
	 * type 
	 * 存储类型：
	 * 1：CPU 
	 * 2：内存 
	 * 3：硬盘 
	 * 4：带宽
	 * 5：套餐
	 */
	private Integer type;
	private Integer cpuCore;
	private BigInteger memory = BigInteger.ZERO;
	private BigInteger dataDisk = BigInteger.ZERO;
	private BigInteger bandwidth = BigInteger.ZERO;
	/**
	 * price
	 * 单个选项或套餐的价格
	 * 根据type的值而定
	 */
	private BigDecimal price;
	private BigDecimal vpcPrice;
	/**
	 * region
	 * 地域
	 * 1：广州
	 * 2：成都
	 * 3：北京
	 * 4：香港
	 */
	private Integer region;
	private BigDecimal percentOff; 
	private String description;
	/**
	 * status
	 * 通途类型
	 * 1：通用
	 * 2：仅vpc使用
	 * 3：仅普通用户使用
	 */
	private Integer status;
	private Integer priceStatus;
	private BigDecimal monthlyPrice;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}
	public BigInteger getMemory() {
		return memory;
	}
	public void setMemory(BigInteger memory) {
		this.memory = memory;
	}
	public BigInteger getDataDisk() {
		return dataDisk;
	}
	public void setDataDisk(BigInteger dataDisk) {
		this.dataDisk = dataDisk;
	}
	public BigInteger getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(BigInteger bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public BigDecimal getPercentOff() {
		return percentOff;
	}
	public void setPercentOff(BigDecimal percentOff) {
		this.percentOff = percentOff;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getVpcPrice() {
		return vpcPrice;
	}
	public void setVpcPrice(BigDecimal vpcPrice) {
		this.vpcPrice = vpcPrice;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(Integer priceStatus) {
		this.priceStatus = priceStatus;
	}
	public BigDecimal getMonthlyPrice() {
		return monthlyPrice;
	}
	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	
	
}
