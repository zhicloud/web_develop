package com.zhicloud.ms.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import com.zhicloud.ms.common.util.json.JSONBean;

public class PortResourcePoolVO implements JSONBean  {
    private String name;    // 端口资源池名称
    private String uuid;    // 端口唯一标识
    private int status;     // 端口资源池状态， 0=disable,1=enable
    private BigInteger[] count; // 端口资源数量，可用
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public BigInteger[] getCount() {
        return count;
    }
    public void setCount(BigInteger[] count) {
        this.count = count;
    }
    public String getStatusText(){
        if(this.status == 0){
            return "禁用";
        }else if(this.status == 1){
            return "启用";
        } 
        return "";
    }
    public BigDecimal getPortUsageFormat(){
        MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);
        if(  count[1].equals(BigInteger.ZERO)){
            return new BigDecimal("0");
        } else {
            return new BigDecimal(count[1]).subtract(new BigDecimal(count[0])).divide(new BigDecimal(count[1]),mc).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
    }
}
