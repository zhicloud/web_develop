package com.zhicloud.ms.vo;

/**
 * @ClassName: MonitorServerVO
 * @Description:服务器监控实体对象
 * @author 张本缘 于 2015年7月2日 上午10:41:27
 */
public class MonitorServerVO {
    private String id;
    private String areaid;
    private String roomid;
    private String rackid;
    private String serverid;
    private String servername;
    private double cpu_usage;
    private double memory_usage;
    private double disk_usage;
    private String shield;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRackid() {
        return rackid;
    }

    public void setRackid(String rackid) {
        this.rackid = rackid;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public double getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(double cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public double getMemory_usage() {
        return memory_usage;
    }

    public void setMemory_usage(double memory_usage) {
        this.memory_usage = memory_usage;
    }

    public double getDisk_usage() {
        return disk_usage;
    }

    public void setDisk_usage(double disk_usage) {
        this.disk_usage = disk_usage;
    }

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }
    
}
