package com.zhicloud.ms.vo;

import java.math.BigDecimal;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ResourceStatisticsVO
 * @Description: 资源使用情况实体对象
 * @author 梁绍辉 于 2015年9月15日 下午2:48:28
 */
public class ResourceStatisticsVO  implements JSONBean {
    private Integer id;
    private BigDecimal cpuUsage;
    private BigDecimal memoryUsage;
    private BigDecimal diskUsage;
    private BigDecimal throughput;
    private String createTime;
    private String dataDate;
    private Integer dataHours;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }
    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    public BigDecimal getMemoryUsage() {
        return memoryUsage;
    }
    public void setMemoryUsage(BigDecimal memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    public BigDecimal getDiskUsage() {
        return diskUsage;
    }
    public void setDiskUsage(BigDecimal diskUsage) {
        this.diskUsage = diskUsage;
    }
    public BigDecimal getThroughput() {
        return throughput;
    }
    public void setThroughput(BigDecimal throughput) {
        this.throughput = throughput;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getDataDate() {
        return dataDate;
    }
    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }
    public Integer getDataHours() {
        return dataHours;
    }
    public void setDataHours(Integer dataHours) {
        this.dataHours = dataHours;
    }
    
}
