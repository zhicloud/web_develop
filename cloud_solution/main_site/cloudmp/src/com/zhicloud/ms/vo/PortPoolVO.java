package com.zhicloud.ms.vo;

import com.zhicloud.ms.util.json.JSONBean;
public class PortPoolVO implements JSONBean{
	private String uuid;//资源池ID
	private String name;//资源池名
	private Integer[] count;//资源节点数[可用，总量]
	private int status;//资源池状态
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer[] getCount() {
		return count;
	}
	public Integer getAllCount(){
		Integer n = 0;
		for(Integer i : this.count){
			n += i;
		}
		return n;
	}
	public void setCount(Integer[] node) {
		this.count = node;
	}
	public int getStatus() {
		return status;
	}
	public String getStatusText(){
		if(this.status == 0){
			return "不可用";
		}else{
			return "可用";
		}
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
