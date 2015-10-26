package com.zhicloud.ms.vo;
import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: BcastVO
 * @Description: 组播地址实体对象
 * @author 梁绍辉 于 2015年9月9日 下午2:48:28
 */
public class BcastVO implements JSONBean{
    private String id;          // 序号
    private String name;        // 名称
    private String bcastaddr;   // 组播地址
    private String status;      // 状态
    private String remark;      // 备注
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
    public String getBcastaddr() {
        return bcastaddr;
    }
    public void setBcastaddr(String bcastaddr) {
        this.bcastaddr = bcastaddr;
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
