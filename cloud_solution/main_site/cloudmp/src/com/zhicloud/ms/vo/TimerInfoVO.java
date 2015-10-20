package com.zhicloud.ms.vo;
/**
 * 
* @ClassName: TimerInfoVO 
* @Description: 定时器信息
* @author sasa
* @date 2015年7月28日 下午3:38:26 
*
 */
public class TimerInfoVO {
    
    private String id;
    
    private String key;
    
    private Integer mode;
    
    private Integer disk;
    // 1:月 2：周 3：日
    private Integer type;
    // 周几 1-7
    private Integer week;
    // 日  1-31
    private Integer day;
    // 时 1-23
    private Integer hour;
    // 分 1-59
    private Integer minute;
    // 秒 1-59
    private Integer second;    
    //创建时间
    private String createTime;
    //定时器状态 1：启用 2：禁用
    private Integer status;
    //定时对应的对象id
    private String objId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
    
    
    
    
    
    

}
