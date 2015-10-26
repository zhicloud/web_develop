package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ResUsageVO
 * @Description: 资源使用实体对象
 * @author 梁绍辉 于 2015年9月9日 下午2:48:28
 */
public class ResUsageVO implements JSONBean {
    private String cpuper;  // CPU使用率
    private String memper;  // 内存使用率
    private String diskper; // 磁盘使用率
    private String data;    // 数据吞吐量
    private String remark;  // 备注
    public String getCpuper() {
        return cpuper;
    }
    public void setCpuper(String cpuper) {
        this.cpuper = cpuper;
    }
    public String getMemper() {
        return memper;
    }
    public void setMemper(String memper) {
        this.memper = memper;
    }
    public String getDiskper() {
        return diskper;
    }
    public void setDiskper(String diskper) {
        this.diskper = diskper;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
}
