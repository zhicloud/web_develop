package com.zhicloud.ms.vo;

import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;

public class TerminalInformationPushVO {
    
    private String id;
    
    private String groupId;
    
    private String title;
    
    private String content;
    
    private String createTime;
    
    private String groupName;
    
    /* 区域 */
    private String region;

    /* 行业 */
    private String industry;
    
    /* 日期类型的时间,方便转换 */
    private Date createDate;
    
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
        // return DateUtil.dateToString(DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS"), "yyyy-MM-dd HH:mm:ss");
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() throws ParseException {
        if (createTime != null && !createTime.isEmpty()) {
            return DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
        }
        return createDate;
    }
    
}
