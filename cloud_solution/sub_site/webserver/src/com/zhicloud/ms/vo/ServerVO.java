package com.zhicloud.ms.vo;
import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ServerVO
 * @Description: 服务器实体对象
 * @author 梁绍辉 于 2015年9月9日 下午2:48:28
 */
public class ServerVO implements JSONBean {
    private String name;    // 服务器名
    private String type;    // 类型
    private String model;   // 厂家型号
    private String ip;      // IP地址
    private String mem;     // 内存
    private String cpucore; // CPU核数
    private String disk;    // 磁盘大小
    private String remark;  // 备注
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getMem() {
        return mem;
    }
    public void setMem(String mem) {
        this.mem = mem;
    }
    public String getCpucore() {
        return cpucore;
    }
    public void setCpucore(String cpucore) {
        this.cpucore = cpucore;
    }
    public String getDisk() {
        return disk;
    }
    public void setDisk(String disk) {
        this.disk = disk;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
