package com.zhicloud.ms.vo;

import com.zhicloud.ms.util.json.JSONBean;

public class HostMigrationVO implements JSONBean{

	/*
	 * 唯一标示
	 */
	private String id;
	
	/**
	 * 主机id
	 */
	private String realHostId;
	
	/*
	 * 主机名
	 */
	private String hostName;
	
	/*
	 * 源NC
	 */
	private String localhostNC;
	
	/*
	 * 目的NC
	 */
	private String toNC;
	
	/*
	 * 迁移完成时间
	 */
	private String time;
	
	/*
	 * 迁移状态:默认为0,1标示正则迁移，2标示迁移完成,3迁移失败
	 */
	private Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getLocalhostNC() {
		return localhostNC;
	}

	public void setLocalhostNC(String localhostNC) {
		this.localhostNC = localhostNC;
	}

	public String getToNC() {
		return toNC;
	}

	public void setToNC(String toNC) {
		this.toNC = toNC;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRealHostId() {
		return realHostId;
	}

	public void setRealHostId(String realHostId) {
		this.realHostId = realHostId;
	}
	
}
