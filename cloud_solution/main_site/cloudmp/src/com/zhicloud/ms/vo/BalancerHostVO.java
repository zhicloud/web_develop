package com.zhicloud.ms.vo; 


import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class BalancerHostVO implements JSONBean{
	
	private String id;
	private String balancerHostId; //负载均衡器的主机ID
	private String associatedHostId; //关联的负载主机的id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBalancerHostId() {
		return balancerHostId;
	}
	public void setBalancerHostId(String balancerHostId) {
		this.balancerHostId = balancerHostId;
	}
	public String getAssociatedHostId() {
		return associatedHostId;
	}
	public void setAssociatedHostId(String associatedHostId) {
		this.associatedHostId = associatedHostId;
	}
	
	
}

