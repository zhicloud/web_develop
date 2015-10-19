package com.zhicloud.ms.app.pool.computePool;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @description 计算资源池
 * @author 张翔
 */
public class ComputeInfo implements JSONBean {

    private String uuid;            // 惟一标识
    private String name;            // 资源池名
    private Integer[] node;         // 资源节点数量,[[停止,告警,故障,正常], [停止,告警,故障,正常],...]
    private Integer[] host;         // 云主机数量,[[停止,告警,故障,正常], [停止,告警,故障,正常],...]
    private Integer[] cpuCount;     // cpu总核心数
    private Double[] cpuUsage;      // cpu利用率
    private Integer[] memory;       // 内存空间，单位：字节，[[可用,总量], [可用,总量], [可用,总量]...]
    private Double[] memoryUsage;   // 内存利用率
    private Integer[] diskVolume;   // 磁盘空间，单位：字节，[[可用,总量], [可用,总量], [可用,总量]...]
    private Double[] disk_usage;    // 磁盘利用率
    private Integer[] status;       // 资源池状态, 0=正常,1=告警,2=故障,3=停止


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getNode() {
        return node;
    }

    public void setNode(Integer[] node) {
        this.node = node;
    }

    public Integer[] getHost() {
        return host;
    }

    public void setHost(Integer[] host) {
        this.host = host;
    }

    public Integer[] getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Integer[] cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Double[] getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double[] cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Integer[] getMemory() {
        return memory;
    }

    public void setMemory(Integer[] memory) {
        this.memory = memory;
    }

    public Double[] getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double[] memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Integer[] getDiskVolume() {
        return diskVolume;
    }

    public void setDiskVolume(Integer[] diskVolume) {
        this.diskVolume = diskVolume;
    }

    public Double[] getDisk_usage() {
        return disk_usage;
    }

    public void setDisk_usage(Double[] disk_usage) {
        this.disk_usage = disk_usage;
    }

    public Integer[] getStatus() {
        return status;
    }

    public void setStatus(Integer[] status) {
        this.status = status;
    }
}
