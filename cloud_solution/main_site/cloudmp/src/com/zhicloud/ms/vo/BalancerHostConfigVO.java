package com.zhicloud.ms.vo; 


import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class BalancerHostConfigVO implements JSONBean{
	
	private String id;
	private String forwordId; //转发服务ID
	private String associatedHostId; //后台主机id
	private Integer weight; //权重(1-256,值大优先)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getForwordId() {
		return forwordId;
	}
	public void setForwordId(String forwordId) {
		this.forwordId = forwordId;
	}
	public String getAssociatedHostId() {
		return associatedHostId;
	}
	public void setAssociatedHostId(String associatedHostId) {
		this.associatedHostId = associatedHostId;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	
}

