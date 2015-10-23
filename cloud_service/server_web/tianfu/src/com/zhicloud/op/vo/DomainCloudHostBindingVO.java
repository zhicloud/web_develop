/**
 * Project Name:tianfu
 * File Name:DomainCloudHostBinding.java
 * Package Name:com.zhicloud.op.vo
 * Date:Apr 29, 20152:41:24 PM
 * 
 *
*/

package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

/**
 * ClassName:DomainCloudHostBinding
 * Date:     Apr 29, 2015 2:41:24 PM
 * @author   sean
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class DomainCloudHostBindingVO implements JSONBean{
	
	private String id;
	private String domain;
	private String hostId;
	private String name;
	private String adminName;
	private String phone;
	private String email;
	private String createTime;
	private String modifiedTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
}

