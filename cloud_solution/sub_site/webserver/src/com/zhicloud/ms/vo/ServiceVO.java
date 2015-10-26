package com.zhicloud.ms.vo;
import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ServiceVO
 * @Description: 服务实体对象
 * @author 梁绍辉 于 2015年9月9日 下午2:48:28
 */
public class ServiceVO implements JSONBean {
    private String id;      // 序号
    private String name;    // 服务
    private String runtime; // 运行时间
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
    public String getRuntime() {
        return runtime;
    }
    public void setRuntime(String runtime) {
        this.runtime = runtime;
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
