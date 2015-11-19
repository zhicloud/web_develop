
package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

/**
 * @description Qos VO对象，与数据库表映射
 * @author 张翔
 */
public class QosVO {

    private String id;                      //ID
    private String uuid;                    //云主机真实ID
    private String hostName;                //云主机名
    private String name;                    //规则名
    private int type;                    //1：云桌面 2：云服务器 3：专属云
    private String ip;                      //云主机IP
    private String serverIp;                //所属服务器
    private BigInteger inboundBandwidth;    //入口带宽，单位：字节
    private BigInteger outboundBandwidth;   //出口带宽，单位：字节
    private Integer maxIops;                //云主机最大iops,0代表不限制
    private Integer priority;               //cpu优先级
    private String createTime;              //创建时间
    private Date createTimeDate;

    private String bandwidth;               //带宽翻译
    private String maxIops_name;            //硬盘翻译
    private String priority_name;           //vcpu优先级翻译

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public BigInteger getInboundBandwidth() {
        return inboundBandwidth;
    }

    public void setInboundBandwidth(BigInteger inboundBandwidth) {
        this.inboundBandwidth = inboundBandwidth;
    }

    public BigInteger getOutboundBandwidth() {
        return outboundBandwidth;
    }

    public void setOutboundBandwidth(BigInteger outboundBandwidth) {
        this.outboundBandwidth = outboundBandwidth;
    }

    public Integer getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(Integer maxIops) {
        this.maxIops = maxIops;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        if(!StringUtil.isBlank(createTime)){
            try {
                this.createTimeDate = DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
                this.createTime = createTime;
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
    }

    public Date getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(Date createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    /**
     * @Description:导出需要，特殊处理
     * @return String
     */
    public String getBandwidth() {
        bandwidth = "上行:" + outboundBandwidth + "MB/下行" + inboundBandwidth + "MB";
        return bandwidth;
    }

    /**
     * @Description:导出需要，特殊处理
     * @return String
     */
    public String getMaxIops_name() {
        maxIops_name = "IOPS:" + maxIops;
        return maxIops_name;
    }

    /**
     * @Description:导出需要，特殊处理
     * @return String
     */
    public String getPriority_name() {
        if (priority == 0) {
            priority_name = "高优先级";
        } else if (priority == 1) {
            priority_name = "中级优先";
        } else if (priority == 2) {
            priority_name = "低级优先";
        }
        return priority_name;
    }
    
}
