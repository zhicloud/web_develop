/**
 * Project Name:ms
 * File Name:SysGroupVO.java
 * Package Name:com.zhicloud.ms.vo
 * Date:Mar 16, 20152:23:04 PM
 * 
 *
 */

package com.zhicloud.ms.vo;


/**
 * ClassName: SysGroupVO 
 * Function: 定义群组VO对象. 
 * date: Mar 16, 2015 2:23:04 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public class SysGroupVO {

	private String id;
	private String groupName;
	private Integer amount;
	private Integer cloudHostAmount;
	private String description;
	private String parentId;
	private String createTime;
	private String modifiedTime;
	private String parentName;
	
	public SysGroupVO() {
	
	}
	
	public SysGroupVO(String id, String groupName, String parentId) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.parentId = parentId;
	}
	/**
	 * id.
	 *
	 * @return  the id
	 * @since   JDK 1.7
	 */
	public String getId() {
		return id;
	}
	/**
	 * id.
	 *
	 * @param   id    the id to set
	 * @since   JDK 1.7
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * groupName.
	 *
	 * @return  the groupName
	 * @since   JDK 1.7
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * groupName.
	 *
	 * @param   groupName    the groupName to set
	 * @since   JDK 1.7
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * amount.
	 *
	 * @return  the amount
	 * @since   JDK 1.7
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * amount.
	 *
	 * @param   amount    the amount to set
	 * @since   JDK 1.7
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * description.
	 *
	 * @return  the description
	 * @since   JDK 1.7
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * description.
	 *
	 * @param   description    the description to set
	 * @since   JDK 1.7
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * createTime.
	 *
	 * @return  the createTime
	 * @since   JDK 1.7
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * createTime.
	 *
	 * @param   createTime    the createTime to set
	 * @since   JDK 1.7
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * modifiedTime.
	 *
	 * @return  the modifiedTime
	 * @since   JDK 1.7
	 */
	public String getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * modifiedTime.
	 *
	 * @param   modifiedTime    the modifiedTime to set
	 * @since   JDK 1.7
	 */
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * cloudHostAmount.
	 *
	 * @return  the cloudHostAmount
	 * @since   JDK 1.7
	 */
	public Integer getCloudHostAmount() {
		return cloudHostAmount;
	}
	/**
	 * cloudHostAmount.
	 *
	 * @param   cloudHostAmount    the cloudHostAmount to set
	 * @since   JDK 1.7
	 */
	public void setCloudHostAmount(Integer cloudHostAmount) {
		this.cloudHostAmount = cloudHostAmount;
	}
	/**
	 * parentId.
	 *
	 * @return  the parentId
	 * @since   JDK 1.7
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * parentId.
	 *
	 * @param   parentId    the parentId to set
	 * @since   JDK 1.7
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	   /**
     * parentName.
     *
     * @return  the parentName
     * @since   JDK 1.7
     */
    public String getParentName() {
        return parentName;
    }
    /**
     * parentName.
     *
     * @param   parentName    the parentName to set
     * @since   JDK 1.7
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
	
}

