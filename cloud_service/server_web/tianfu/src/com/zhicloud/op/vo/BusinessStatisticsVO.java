package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class BusinessStatisticsVO implements JSONBean{
	/**
	 * 1:云主机 2： 云硬盘 3：总计
	 */
	private Integer type;
	
	private BigDecimal gzStatistics;
	
	private BigDecimal bjStatistics;
	
	private BigDecimal cdStatistics;
	
	private BigDecimal xgStatistics;
	
	private BigDecimal totalStatistics;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getGzStatistics() {
		return gzStatistics;
	}

	public void setGzStatistics(BigDecimal gzStatistics) {
		if(gzStatistics==null){
			gzStatistics = BigDecimal.ZERO;
		}
		this.gzStatistics = gzStatistics;
	}

	public BigDecimal getBjStatistics() {
		return bjStatistics;
	}

	public void setBjStatistics(BigDecimal bjStatistics) {
		if(bjStatistics==null){
			bjStatistics = BigDecimal.ZERO;
		}
		this.bjStatistics = bjStatistics;
	}

	public BigDecimal getCdStatistics() {
		return cdStatistics;
	}

	public void setCdStatistics(BigDecimal cdStatistics) {
		if(cdStatistics==null){
			cdStatistics = BigDecimal.ZERO;
		}
		this.cdStatistics = cdStatistics;
	}

	public BigDecimal getXgStatistics() {
		return xgStatistics;
	}

	public void setXgStatistics(BigDecimal xgStatistics) {
		if(xgStatistics==null){
			xgStatistics = BigDecimal.ZERO;
		}
		this.xgStatistics = xgStatistics;
	}

	public BigDecimal getTotalStatistics() {
		return totalStatistics;
	}

	public void setTotalStatistics() {
		this.totalStatistics = this.gzStatistics.add(cdStatistics).add(bjStatistics).add(xgStatistics);
	}
	
	

	

}
