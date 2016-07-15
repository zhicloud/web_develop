
package com.zhicloud.ms.vo;

/**
 * @ClassName: SysWarnRuleVO
 * @Description: 预警规则设置
 * @author 张本缘 于 2015年7月13日 上午10:37:18
 */
public class SysWarnRuleVO {

    private String id;
    private String name;
    private String code;
    private String content;
    private Integer isnotify;
    private Integer realtime;
    private Integer ruletype;
    private String sendtime;
    private String notify_phone;
    private String notify_email;
    private String insert_user;
    private String insert_date;
    private String username;
    private String frequency;
    private Integer sampletime;
    private String edittype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsnotify() {
        return isnotify;
    }

    public void setIsnotify(Integer isnotify) {
        this.isnotify = isnotify;
    }

    public Integer getRealtime() {
        return realtime;
    }

    public void setRealtime(Integer realtime) {
        this.realtime = realtime;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getNotify_phone() {
        return notify_phone;
    }

    public void setNotify_phone(String notify_phone) {
        this.notify_phone = notify_phone;
    }

    public String getNotify_email() {
        return notify_email;
    }

    public void setNotify_email(String notify_email) {
        this.notify_email = notify_email;
    }

    public String getInsert_user() {
        return insert_user;
    }

    public void setInsert_user(String insert_user) {
        this.insert_user = insert_user;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRuletype() {
        return ruletype;
    }

    public void setRuletype(Integer ruletype) {
        this.ruletype = ruletype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getSampletime() {
        return sampletime;
    }

    public void setSampletime(Integer sampletime) {
        this.sampletime = sampletime;
    }

    public String getEdittype() {
        return edittype;
    }

    public void setEdittype(String edittype) {
        this.edittype = edittype;
    }

}
