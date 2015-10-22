package com.zhicloud.op.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.zhicloud.op.common.util.json.JSONBean;

@XmlAccessorType(XmlAccessType.FIELD)
public class SysDiskImageVO implements JSONBean
{

	@XmlElement(name="image_id")
	private String id;
	@XmlTransient
	private String realImageId;
	@XmlElement(name="image_name")
	private String name;
	@XmlTransient
	private String displayName;
	@XmlTransient
	private String fromHostId;
	@XmlTransient
	private String tag;
	private String description;
	@XmlTransient
	private String groupId;
	@XmlTransient
	private String userId;
	@XmlTransient
	private Integer sort;
	private Integer region;
	private Integer status;
	private Integer type;
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getRealImageId()
	{
		return realImageId;
	}
	public void setRealImageId(String realImageId)
	{
		this.realImageId = realImageId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getFromHostId()
	{
		return fromHostId;
	}
	public void setFromHostId(String fromHostId)
	{
		this.fromHostId = fromHostId;
	}
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getGroupId()
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public Integer getSort()
	{
		return sort;
	}
	public void setSort(Integer sort)
	{
		this.sort = sort;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public Integer getRegion()
	{
		return region;
	}
	public void setRegion(Integer region)
	{
		this.region = region;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
	
}
