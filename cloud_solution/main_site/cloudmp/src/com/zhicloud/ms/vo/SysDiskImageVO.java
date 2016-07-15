package com.zhicloud.ms.vo; 

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.util.json.JSONBean;

 
public class SysDiskImageVO implements JSONBean
{
 
	private String id;
	/*
	 * 真实镜像id
	 */
 	private String realImageId;
 	/*
 	 * 镜像名称
 	 */
 	private String name;
 	/*
 	 * 镜像显示名称
 	 */
 	private String displayName;
 	/*
 	 * 主机id
 	 */
 	private String fromHostId;
 	private String tag;
 	/*
 	 * 镜像显示名称描述
 	 */
	private String description;
 	private String groupId;
 	private String userId;
 	private Integer sort;
 	/*
 	 * 1=未验证;2=已验证
 	 */
 	private Integer status;
 	/*
 	 * 0=系统默认;1=从主机创建;2=镜像上传
 	 */
	private Integer type;
	/*
	 * 1=广州;2=成都;4=香港
	 */
	private Integer region = 1;
	private String typeName;
	private String createTime;
	private Date createTimeDate;
	private String iamgeStatus = "镜像未创建成功，不可用";
	private Integer imageType;
	private String imageTypeName;
    /* 状态翻译 */
    private String status_name;
    
    private Integer fileType;
    
    private BigInteger size;
	
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
		if(StringUtil.isBlank(realImageId)){
			this.iamgeStatus = "未创建成功，不可用";
		}else{
			this.iamgeStatus = "可用";
		}
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
		if(type == 0){
			this.typeName = "系统默认";
		}else if(type == 1){
			this.typeName = "从主机创建";			
		}else if(type == 2){
			this.typeName = "镜像上传";			
		}else{
			this.typeName = "";
		}
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
		if(!StringUtil.isBlank(createTime)){			
			try {
				this.createTimeDate = DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
			} catch (ParseException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	}
	public Date getCreateTimeDate() {
		return createTimeDate;
	}
	public void setCreateTimeDate(Date createTimeDate) {
		this.createTimeDate = createTimeDate;
	}
	public String getIamgeStatus() {
		return iamgeStatus;
	}
	public void setIamgeStatus(String iamgeStatus) {
		this.iamgeStatus = iamgeStatus;
	}
	public Integer getImageType() {
		return imageType;
	}
	public void setImageType(Integer imageType) {
		this.imageType = imageType;
		if(imageType == AppConstant.DISK_IMAGE_TYPE_COMMON){
			this.imageTypeName = "通用镜像";
		}else if(imageType == AppConstant.DISK_IMAGE_TYPE_SERVER){
			this.imageTypeName = "云服务器专用镜像";
		}else if(imageType == AppConstant.DISK_IMAGE_TYPE_VPC){
			this.imageTypeName = "专属云专用镜像";
		}else if(imageType == AppConstant.DISK_IMAGE_TYPE_DESKTOP){
			this.imageTypeName = "桌面云专用镜像";
		}
	}
	public String getImageTypeName() {
		return imageTypeName;
	}
	public void setImageTypeName(String imageTypeName) {
		this.imageTypeName = imageTypeName;
	}

    public String getInsert_date() {
        return DateUtil.dateToString(createTimeDate, "yyyy-MM-dd HH:mm:ss");
    }
    
    public String getStatus_name() {
        if (realImageId == null || realImageId.isEmpty()) {
            status_name = "创建中";
        } else {
            status_name = "可用";
        }
        return status_name;
    }
    public Integer getFileType() {
        return fileType;
    }
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
    public BigInteger getSize() {
        return size;
    }
    public void setSize(BigInteger size) {
        this.size = size;
    }
    
    
    
}
