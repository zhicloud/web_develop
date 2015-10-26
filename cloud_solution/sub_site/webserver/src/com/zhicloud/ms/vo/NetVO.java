package com.zhicloud.ms.vo;
import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: NetVO
 * @Description: 网络实体对象
 * @author 梁绍辉 于 2015年9月9日 下午2:48:28
 */
public class NetVO implements JSONBean {
    private String id;      // 序号
    private String name;    // 名称
    private String address; // IP地址
    private String mask;    // 掩码
    private String speed;   // 速率
    private String status;  // 状态
    private String remark;  // 备注
    

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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getMask() {
        return mask;
    }
    public void setMask(String mask) {
        this.mask = mask;
    }
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
